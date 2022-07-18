package com.safr.mastercocktail.presentation.viewmodels

import android.app.Application
import android.util.Log
import androidx.core.os.bundleOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.safr.mastercocktail.core.DataState
import com.safr.mastercocktail.domain.model.api.CategoryNet
import com.safr.mastercocktail.domain.model.api.DrinkNet
import com.safr.mastercocktail.domain.usecases.api.CategoryApiUseCases
import com.safr.mastercocktail.domain.usecases.api.GetRandomApiUseCases
import com.safr.mastercocktail.domain.usecases.api.SearchCocktailsApiUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    val bundle = bundleOf("param" to "tab")

    private val mutCategory: MutableStateFlow<List<CategoryNet>?> = MutableStateFlow(emptyList())
    val categoryLive: StateFlow<List<CategoryNet>?> = mutCategory

    private val mutSearchDataState: MutableStateFlow<List<DrinkNet>?> = MutableStateFlow(listOf(DrinkNet()))
    val searchDataState: StateFlow<List<DrinkNet>?> = mutSearchDataState

    private val mutDataStateRandom: MutableStateFlow<List<DrinkNet>?> = MutableStateFlow(emptyList())
    val getRandomState: StateFlow<List<DrinkNet>?> = mutDataStateRandom

    private val mutIsDataLoading: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isDataLoading: StateFlow<Boolean> = mutIsDataLoading

    private val mutIsError: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isError: StateFlow<Boolean> = mutIsError


    init {
        load()
    }


   private fun load() {
        viewModelScope.launch {
            category.getCategory().onEach { cat ->
                when (cat) {
                    is DataState.Error -> cat.exception.also {
                        mutIsDataLoading.value = false
                        mutIsError.value = true
                    }
                    DataState.Loading -> mutIsDataLoading.value = true
                    is DataState.Success -> {
                        mutCategory.value = cat.data?.cat
                        mutIsDataLoading.value = false
                    }
                }
            }.launchIn(viewModelScope)

        }
    }

    fun rand() {
        viewModelScope.launch {
            getRandom.getCocktails().collect { dataState ->
                when (dataState) {
                    is DataState.Error -> dataState.exception.also {
                        mutIsDataLoading.value = false
                        mutIsError.value = true
                        Log.d("lol", " rand() ${dataState.exception}")
                    }
                    DataState.Loading -> mutIsDataLoading.value = true
                    is DataState.Success -> {
                        mutDataStateRandom.value = dataState.data?.drinks
                        mutIsDataLoading.value = false
                    }
                }
            }
        }
    }

    fun search(name: String) {
        viewModelScope.launch {
            searchCocktail.searchCocktails(name).onEach { dataState ->
                when (dataState) {

                    is DataState.Error -> dataState.exception.also {
                        mutIsDataLoading.value = false
                        mutIsError.value = true
                        Log.d("lol", " rand() ${dataState.exception}")
                    }
                    DataState.Loading -> {
                        mutIsDataLoading.value = true
                    }

                    is DataState.Success -> {
                        Log.d("lol", "search null ${dataState.data?.drinks}")
                        mutIsDataLoading.value = false

                        if (dataState.data?.drinks != null) {
                            mutSearchDataState.value = dataState.data?.drinks
                            Log.d("lol", "search ${dataState.data?.drinks?.get(0)}")
                        }
                        else {
                            Log.d("lol", "CocktailListViewModel search else")
                            mutSearchDataState.value = emptyList()
                        }

                    }

                }
            }.launchIn(viewModelScope)
        }
    }
}