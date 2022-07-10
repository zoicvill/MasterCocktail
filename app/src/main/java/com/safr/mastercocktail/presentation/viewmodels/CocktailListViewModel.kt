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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CocktailListViewModel @Inject constructor(
    private val catDrink: CocktailCategoryUseCases,
    application: Application
) : AndroidViewModel(application) {


//    private val dataStateMut: MutableLiveData<List<DrinkNet>> = MutableLiveData()
//
//    val getRandomState: LiveData<List<DrinkNet>>
//        get() = dataStateMut

//    private val searchdataStateMut: MutableLiveData<List<DrinkNet>> = MutableLiveData()
//
//    val searchDataState: LiveData<List<DrinkNet>>
//        get() = searchdataStateMut


    private val catDrinkMut: MutableLiveData<
            List<DrinkNet>> = MutableLiveData()

    val catDrinkState: LiveData<List<DrinkNet>>
        get() = catDrinkMut

    fun catDrinkFun(str: String?, view: RelativeLayout) {
        viewModelScope.launch {
            catDrink.getCocktailsCategories(str?: "Cocktail").collect { dataState ->
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

}