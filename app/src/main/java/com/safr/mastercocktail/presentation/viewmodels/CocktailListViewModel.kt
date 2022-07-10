package com.safr.mastercocktail.presentation.viewmodels

import android.app.Application
import android.util.Log
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.safr.mastercocktail.core.DataState
import com.safr.mastercocktail.data.network.model.DrinkNetMod
import com.safr.mastercocktail.domain.model.api.DrinkNet
import com.safr.mastercocktail.domain.usecases.api.CocktailCategoryUseCases
import com.safr.mastercocktail.domain.usecases.api.GetCocktailsApiUseCases
import com.safr.mastercocktail.domain.usecases.api.SearchCocktailsApiUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CocktailListViewModel @Inject constructor(
    private val getCocktail: GetCocktailsApiUseCases,
    private val searchCocktail: SearchCocktailsApiUseCases,
    private val catDrink: CocktailCategoryUseCases,
    application: Application
) : AndroidViewModel(application) {


    private val dataStateMut: MutableLiveData<List<DrinkNet>> = MutableLiveData()

    val getCocktailState: LiveData<List<DrinkNet>>
        get() = dataStateMut

    private val searchdataStateMut: MutableLiveData<List<DrinkNet>> = MutableLiveData()

    val searchdataState: LiveData<List<DrinkNet>>
        get() = searchdataStateMut


    private val catDrinkMut: MutableLiveData<
            List<DrinkNet>> = MutableLiveData()

    val catDrinkState: LiveData<List<DrinkNet>>
        get() = catDrinkMut

    fun catDrinkFun(str: String?, view: RelativeLayout) {
        viewModelScope.launch {
            catDrink.getCocktailsCategories(str?: "Cocktail").collect() { dataState ->
                when (dataState) {
                    is DataState.Error -> DataState.Error(object : Error() {})
                    DataState.Loading -> view.isVisible = true
                    is DataState.Success -> {
                        catDrinkMut.value = dataState.data?.drinks
                        view.isVisible = false
                    }

                }
            }
        }
    }

    fun start(view: RelativeLayout) {
        viewModelScope.launch {
            getCocktail.getCocktails().collect { dataState ->
                when (dataState) {
                    is DataState.Error -> DataState.Error(object : Error() {})
                    DataState.Loading -> view.isVisible = true
                    is DataState.Success -> {
                        dataStateMut.value = dataState.data?.drinks
                        view.isVisible = false
                    }
                }
            }
        }
    }


    fun search(
        name: String, view: RelativeLayout, textView: TextView
    ) {
        viewModelScope.launch {
            searchCocktail.searchCocktails(name).onEach { dataState ->
                when (dataState) {

                    is DataState.Error -> DataState.Error(object : Error() {})
                    DataState.Loading -> {
                        view.isVisible = true
                    }

                    is DataState.Success -> {
                        Log.d(
                            "lol",
                            "CocktailListViewModel search null ${dataState.data?.drinks}"
                        )
                        view.isVisible = false

                        if (dataState.data?.drinks != null) {
                            textView.isVisible = false
                            searchdataStateMut.value = dataState.data.drinks
                            Log.d(
                                "lol",
                                "CocktailListViewModel search ${dataState.data.drinks[0]}"
                            )
                        }
                        else {
                            textView.isVisible = true
                            Log.d("lol", "CocktailListViewModel search else")

                            searchdataStateMut.value = emptyList()
                        }

                    }

                }
            }.launchIn(viewModelScope)
        }
    }
}