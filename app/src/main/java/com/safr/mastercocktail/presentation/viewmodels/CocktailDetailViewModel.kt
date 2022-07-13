package com.safr.mastercocktail.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.safr.mastercocktail.core.DataState
import com.safr.mastercocktail.domain.model.api.DetailedDrinkNet
import com.safr.mastercocktail.domain.model.data.DrinkData
import com.safr.mastercocktail.domain.usecases.api.CocktailDetailsApiUseCases
import com.safr.mastercocktail.domain.usecases.db.AddFavouritesDbUseCases
import com.safr.mastercocktail.domain.usecases.db.CheckFavouriteUseCases
import com.safr.mastercocktail.domain.usecases.db.RemoveFavourites
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CocktailDetailViewModel @Inject constructor(
    private val cocktailDetApi: CocktailDetailsApiUseCases,
    private val checkDB: CheckFavouriteUseCases,
    private val addDb: AddFavouritesDbUseCases,
    private val removeDb: RemoveFavourites,
    application: Application
) : AndroidViewModel(application) {

    private val idMut = MutableLiveData<Int>()

    private val mutListDrink: MutableLiveData<DetailedDrinkNet> = MutableLiveData()
    val detailListDrink: LiveData<DetailedDrinkNet>
        get() = mutListDrink

    private val mutFavour: MutableLiveData<Boolean?> = MutableLiveData()
    val favourites: LiveData<Boolean?>
        get() = mutFavour


    private val mutIsDataLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isDataLoading: LiveData<Boolean> = mutIsDataLoading

    private val mutIsError: MutableLiveData<Boolean> = MutableLiveData()
    val isError: LiveData<Boolean> = mutIsError


    fun start(id: Int) {
        mutIsDataLoading.postValue(true)
        mutIsError.postValue(false)
        idMut.value = id
        viewModelScope.launch {
            cocktailDetApi.getCocktailDetails(idMut.value ?: 15346)
                .onEach { state ->
                    when (state) {
                        is DataState.Error -> state.exception.also {
                            mutIsDataLoading.postValue(false)
                            mutIsError.postValue(true)
                        }
                        is DataState.Loading -> mutIsDataLoading.postValue(true)
                        is DataState.Success -> {
                            mutListDrink.postValue(state.data?.drinks?.last())
                            mutIsDataLoading.postValue(false)
                            checkStar()
                        }
                    }
                }.launchIn(viewModelScope)
        }

    }

    private fun checkStar() {
        viewModelScope.launch {
            checkDB.checkIfFavourite(idMut.value ?: 15346)
                .onEach { state ->
                    when (state) {
                        is DataState.Error -> state.exception.also {
//                            mutIsDataLoading.postValue(false)
//                            mutIsError.postValue(true)
                        }
                        is DataState.Loading -> mutIsDataLoading.postValue(true)
                        is DataState.Success -> {
                            mutFavour.postValue(state.data)
                            mutIsDataLoading.postValue(false)
                        }
                    }
                }.launchIn(viewModelScope)
        }
    }

    fun addCocktailToFavourite() {
        viewModelScope.launch {
            cocktailDetApi.getCocktailDetails(idMut.value ?: 15346)
                .onEach { state ->
                    when (state) {
                        is DataState.Error -> state.exception.also {
                            mutIsDataLoading.postValue(false)
                            mutIsError.postValue(true)
                        }
                        is DataState.Loading -> {
                            mutIsDataLoading.postValue(false)
                        }
                        is DataState.Success -> {
                            addDb.addToFavourites(DrinkData(state.data?.drinks!![0]))
                            checkStar()
                            mutIsDataLoading.postValue(false)
                        }

                    }
                }.launchIn(viewModelScope)
        }
    }


    fun removeCocktailFromFavourite() {
        viewModelScope.launch {
            cocktailDetApi.getCocktailDetails(idMut.value ?: 15346)
                .onEach { state ->
                    when (state) {
                        is DataState.Error -> state.exception.also {
                            mutIsDataLoading.postValue(false)
                            mutIsError.postValue(true)
                        }
                        is DataState.Loading -> {
                            mutIsDataLoading.postValue(false)
                        }
                        is DataState.Success -> {
                            removeDb.removeFromFavourites(DrinkData(state.data?.drinks!![0]))
                            checkStar()
                            mutIsDataLoading.postValue(false)
                        }
                    }
                }.launchIn(viewModelScope)
        }
    }

}