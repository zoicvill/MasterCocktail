package com.safr.mastercocktail.presentation.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val mutListDrink: MutableStateFlow<DetailedDrinkNet?> =
        MutableStateFlow(DetailedDrinkNet())
    val detailListDrink = mutListDrink.asStateFlow()

    private val mutFavour: MutableStateFlow<Boolean?> = MutableStateFlow(false)
    val favourites = mutFavour.asStateFlow()


    private val mutIsDataLoading: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isDataLoading = mutIsDataLoading.asStateFlow()

    private val mutIsError: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isError = mutIsError.asStateFlow()


    fun start(id: Int) {
        idMut.value = id
        viewModelScope.launch {
            Log.d("lol", " fun start $id value ${idMut.value}")
            cocktailDetApi.getCocktailDetails(idMut.value ?: 15346)
                .onEach { state ->
                    when (state) {
                        is DataState.Error -> state.exception.also {
                            mutIsDataLoading.value = false
                            mutIsError.value = true
                        }
                        is DataState.Loading -> mutIsDataLoading.value = true
                        is DataState.Success -> {
                            Log.d("lol", " Success ${idMut.value}")
                            Log.d("lol", " Success last ${state.data?.drinks?.last()}")

                            mutListDrink.value = state.data?.drinks?.last()
                            mutIsDataLoading.value = false
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
                        is DataState.Loading -> mutIsDataLoading.value = false
                        is DataState.Success -> {
                            mutFavour.value = state.data
                            mutIsDataLoading.value = false
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
                            mutIsDataLoading.value = false
                            mutIsError.value = true
                        }
                        is DataState.Loading -> {
                            mutIsDataLoading.value = false
                        }
                        is DataState.Success -> {
                            addDb.addToFavourites(DrinkData(state.data?.drinks!![0]))
                            checkStar()
                            mutIsDataLoading.value = false
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
                            mutIsDataLoading.value = false
                            mutIsError.value = true
                        }
                        is DataState.Loading -> {
                            mutIsDataLoading.value = false
                        }
                        is DataState.Success -> {
                            removeDb.removeFromFavourites(DrinkData(state.data?.drinks!![0]))
                            checkStar()
                            mutIsDataLoading.value = false
                        }
                    }
                }.launchIn(viewModelScope)
        }
    }

}