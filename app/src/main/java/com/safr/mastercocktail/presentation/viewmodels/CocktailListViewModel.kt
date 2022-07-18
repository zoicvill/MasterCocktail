package com.safr.mastercocktail.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.safr.mastercocktail.core.DataState
import com.safr.mastercocktail.domain.model.api.DrinkNet
import com.safr.mastercocktail.domain.usecases.api.CocktailCategoryUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CocktailListViewModel @Inject constructor(
    private val catDrink: CocktailCategoryUseCases,
    application: Application
) : AndroidViewModel(application) {

    private val catDrinkMut: MutableStateFlow<List<DrinkNet>?> = MutableStateFlow(listOf())

    val catDrinkState: StateFlow<List<DrinkNet>?>
        get() = catDrinkMut

    private val mutIsDataLoading: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isDataLoading: StateFlow<Boolean> = mutIsDataLoading

    private val mutIsError: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isError: StateFlow<Boolean> = mutIsError

    fun catDrinkFun(str: String?) {
        viewModelScope.launch {
            catDrink.getCocktailsCategories(str ?: "Cocktail").collect { dataState ->
                when (dataState) {
                    is DataState.Error -> dataState.exception.also {
                        mutIsDataLoading.value = false
                        mutIsError.value = true
                    }
                    DataState.Loading -> mutIsDataLoading.value = true
                    is DataState.Success -> {
                        catDrinkMut.value = dataState.data?.drinks
                        mutIsDataLoading.value = false
                    }

                }
            }
        }
    }

}