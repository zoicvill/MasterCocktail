package com.safr.mastercocktail.presentation.viewmodels

import android.app.Application
import android.util.Log
import androidx.core.os.bundleOf
import androidx.lifecycle.*
import com.safr.mastercocktail.core.DataState
import com.safr.mastercocktail.domain.model.api.CategoryNet
import com.safr.mastercocktail.domain.model.api.DrinkNet
import com.safr.mastercocktail.domain.usecases.api.CategoryApiUseCases
import com.safr.mastercocktail.domain.usecases.api.GetRandomApiUseCases
import com.safr.mastercocktail.domain.usecases.api.SearchCocktailsApiUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
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

    private val mutCategory: MutableLiveData<List<CategoryNet>> = MutableLiveData()
    val categoryLive: LiveData<List<CategoryNet>> = mutCategory

    private val mutSearchDataState: MutableLiveData<List<DrinkNet>> = MutableLiveData()
    val searchDataState: LiveData<List<DrinkNet>> = mutSearchDataState

    private val mutDataStateRandom: MutableLiveData<List<DrinkNet>> = MutableLiveData()
    val getRandomState: LiveData<List<DrinkNet>> = mutDataStateRandom

    private val mutIsDataLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isDataLoading: LiveData<Boolean> = mutIsDataLoading

    private val mutIsError: MutableLiveData<Boolean> = MutableLiveData()
    val isError: LiveData<Boolean> = mutIsError


    init {
        load()

    }


    private fun load() {
        mutIsDataLoading.value = true
        mutIsError.value = false
        viewModelScope.launch {
            category.getCategory().onEach { cat ->
                when (cat) {
                    is DataState.Error -> cat.exception.also {
                        mutIsDataLoading.postValue(false)
                        mutIsError.postValue(true)
                    }
                    DataState.Loading -> mutIsDataLoading.postValue(true)
                    is DataState.Success -> {
                        mutCategory.postValue(cat.data?.cat)
                        mutIsDataLoading.postValue(false)
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
                        mutIsDataLoading.postValue(false)
                        mutIsError.postValue(true)
                        Log.d("lol", " rand() ${dataState.exception}")
                    }
                    DataState.Loading -> mutIsDataLoading.postValue(true)
                    is DataState.Success -> {
                        mutDataStateRandom.postValue(dataState.data?.drinks)
                        mutIsDataLoading.postValue(false)
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
                        mutIsDataLoading.postValue(false)
                        mutIsError.postValue(true)
                        Log.d("lol", " rand() ${dataState.exception}")
                    }
                    DataState.Loading -> {
                        mutIsDataLoading.postValue(true)
                    }

                    is DataState.Success -> {
                        Log.d("lol", "search null ${dataState.data?.drinks}")
                        mutIsDataLoading.postValue(false)

                        if (dataState.data?.drinks != null) {
                            mutSearchDataState.value = dataState.data.drinks
                            Log.d("lol", "search ${dataState.data.drinks[0]}")
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