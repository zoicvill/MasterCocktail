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
import com.safr.mastercocktail.domain.model.api.CategoryNet
import com.safr.mastercocktail.domain.model.api.DrinkNet
import com.safr.mastercocktail.domain.usecases.api.CategoryApiUseCases
import com.safr.mastercocktail.domain.usecases.api.GetRandomApiUseCases
import com.safr.mastercocktail.domain.usecases.api.SearchCocktailsApiUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val category: CategoryApiUseCases,
    private val getRandom: GetRandomApiUseCases,
    private val searchCocktail: SearchCocktailsApiUseCases,
    application: Application
) : AndroidViewModel(application) {

    private val mutCategory: MutableLiveData<List<CategoryNet>> = MutableLiveData()
    val categoryLive: LiveData<List<CategoryNet>>
        get() = mutCategory

    private val searchDataStateMut: MutableLiveData<List<DrinkNet>> = MutableLiveData()
    val searchDataState: LiveData<List<DrinkNet>>
        get() = searchDataStateMut

    private val dataStateMutRandom: MutableLiveData<List<DrinkNet>> = MutableLiveData()
    val getRandomState: LiveData<List<DrinkNet>>
        get() = dataStateMutRandom



    fun categoryState(view: RelativeLayout){
        viewModelScope.launch {
            category.getCategory().onEach { cat ->
                when (cat) {
                    is DataState.Error -> object : Error() {}
                    DataState.Loading -> view.isVisible = true
                    is DataState.Success -> {
                        mutCategory.postValue(cat.data?.cat)
                        view.isVisible = false
                    }
                }
            }.launchIn(viewModelScope)

        }
    }

    fun start(view: RelativeLayout) {
        viewModelScope.launch {
            getRandom.getCocktails().collect { dataState ->
                when (dataState) {
                    is DataState.Error -> DataState.Error(object : Error() {})
                    DataState.Loading -> view.isVisible = true
                    is DataState.Success -> {
                        dataStateMutRandom.value = dataState.data?.drinks
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
                            searchDataStateMut.value = dataState.data.drinks
                            Log.d(
                                "lol",
                                "CocktailListViewModel search ${dataState.data.drinks[0]}"
                            )
                        }
                        else {
                            textView.isVisible = true
                            Log.d("lol", "CocktailListViewModel search else")

                            searchDataStateMut.value = emptyList()
                        }

                    }

                }
            }.launchIn(viewModelScope)
        }
    }
}