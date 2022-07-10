package com.safr.mastercocktail.presentation.viewmodels

import android.app.Application
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.core.view.isVisible
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

    private val mutFavour: MutableLiveData<
            Boolean?> = MutableLiveData()
    val favourites: LiveData<Boolean?>
        get() = mutFavour


    fun start(
        id: Int,
        view: RelativeLayout,
    ) {
        idMut.value = id
        viewModelScope.launch {
            cocktailDetApi.getCocktailDetails(idMut.value ?: 15346)
                .onEach { state ->
                    when (state) {
                        is DataState.Error -> object : Error() {}
                        DataState.Loading -> view.isVisible = true
                        is DataState.Success -> {
                            mutListDrink.postValue(state.data?.drinks?.last())
                        }
                    }
                }.launchIn(viewModelScope)

//            checkDB.checkIfFavourite(idMut.value ?: 15346)
//                .onEach { state -> mutFavour.postValue(state) }.launchIn(viewModelScope)
            checkStar()
//            checkStar(viewImage, star, unStar)
        }

    }

    private fun checkStar() {
        viewModelScope.launch {
            checkDB.checkIfFavourite(idMut.value ?: 15346)
                .onEach { state ->
                    when (state) {
                        is DataState.Success ->{
                            mutFavour.postValue(state.data)
                        }
                        else -> {}
                    }
                }.launchIn(viewModelScope)
        }
    }

    fun addCocktailToFavourit(view: RelativeLayout) {
        viewModelScope.launch {
            cocktailDetApi.getCocktailDetails(idMut.value ?: 15346)
                .onEach { state ->
                    when (state) {
                        is DataState.Success -> {
                            addDb.addToFavourites(DrinkData(state.data?.drinks!![0]))
                            checkStar()
                        }
                        else -> {}
                    }
                }.launchIn(viewModelScope)
        }
    }


    fun removeCocktailFromFavourit(view: RelativeLayout) {
        viewModelScope.launch {
            cocktailDetApi.getCocktailDetails(idMut.value ?: 15346)
                .onEach { state ->
                    when (state) {
                        is DataState.Success -> {
                            removeDb.removeFromFavourites(DrinkData(state.data?.drinks!![0]))
                            checkStar()
                        }
                        else -> {}
                    }
                }.launchIn(viewModelScope)
        }
    }

}