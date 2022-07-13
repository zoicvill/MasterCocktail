package com.safr.mastercocktail.presentation.viewmodels

import android.app.Application
import android.widget.RelativeLayout
import androidx.core.view.isVisible
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.safr.mastercocktail.core.DataState
import com.safr.mastercocktail.domain.model.api.DrinkNet
import com.safr.mastercocktail.domain.usecases.api.CocktailCategoryUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CocktailListViewModel @Inject constructor(
    private val catDrink: CocktailCategoryUseCases,
    application: Application
) : AndroidViewModel(application) {

    private val catDrinkMut: MutableLiveData<List<DrinkNet>> = MutableLiveData()

    val catDrinkState: LiveData<List<DrinkNet>>
        get() = catDrinkMut

    private val mutIsDataLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isDataLoading: LiveData<Boolean> = mutIsDataLoading

    private val mutIsError: MutableLiveData<Boolean> = MutableLiveData()
    val isError: LiveData<Boolean> = mutIsError

    fun catDrinkFun(str: String?) {
        mutIsDataLoading.value = true
        mutIsError.value = false
        viewModelScope.launch {
            catDrink.getCocktailsCategories(str ?: "Cocktail").collect { dataState ->
                when (dataState) {
                    is DataState.Error -> dataState.exception.also {
                        mutIsDataLoading.postValue(false)
                        mutIsError.postValue(true)
                    }
                    DataState.Loading -> mutIsDataLoading.postValue(true)
                    is DataState.Success -> {
                        catDrinkMut.postValue(dataState.data?.drinks)
                        mutIsDataLoading.postValue(false)
                    }

                }
            }
        }
    }

}