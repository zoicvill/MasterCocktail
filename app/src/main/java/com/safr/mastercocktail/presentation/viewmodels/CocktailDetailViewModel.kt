package com.safr.mastercocktail.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.safr.mastercocktail.core.DataState
import com.safr.mastercocktail.data.local.model.Drink
import com.safr.mastercocktail.data.network.model.DetailListDrink
import com.safr.mastercocktail.data.network.model.DetailedDrink
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

    private val dataState: MutableLiveData<DataState<DetailListDrink>> = MutableLiveData()

    val dataStateLive: LiveData<DataState<DetailListDrink>>
        get() = dataState

    private val exists: MutableLiveData<DataState<Boolean>> = MutableLiveData()

    val existsLive: LiveData<DataState<Boolean>>
        get() = exists


    fun start(id: Int) {
        idMut.value = id
        viewModelScope.launch {
            cocktailDetApi.getCocktailDetails(idMut.value ?: 0)
                .onEach { state -> dataState.value = state }.launchIn(viewModelScope)

            checkDB.checkIfFavourite(idMut.value ?: 0)
                .onEach { state -> exists.value = state }.launchIn(viewModelScope)
        }

    }

    fun addCocktailToFavourites(cocktail: DetailedDrink) =
        viewModelScope.launch { addDb.addToFavourites(Drink(cocktail)) }

    fun removeCocktailFromFavourites(cocktail: DetailedDrink) =
        viewModelScope.launch { removeDb.removeFromFavourites(Drink(cocktail)) }
}