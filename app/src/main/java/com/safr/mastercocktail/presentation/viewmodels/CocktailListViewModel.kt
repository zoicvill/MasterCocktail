package com.safr.mastercocktail.presentation.viewmodels

import android.app.Application
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.safr.mastercocktail.core.DataState
import com.safr.mastercocktail.data.local.model.Drink
import com.safr.mastercocktail.data.local.model.Drinks
import com.safr.mastercocktail.domain.usecases.api.GetCocktailsApiUseCases
import com.safr.mastercocktail.domain.usecases.api.SearchCocktailsApiUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CocktailListViewModel @Inject constructor(
    private val getCocktail: GetCocktailsApiUseCases,
    private val searchCocktail: SearchCocktailsApiUseCases,
    application: Application
) : AndroidViewModel(application) {


    private val dataStateMut: MutableLiveData<List<Drink>> = MutableLiveData()

    val getCocktailState: LiveData<List<Drink>>
        get() = dataStateMut

    private val searchdataStateMut: MutableLiveData<List<Drink>> = MutableLiveData()

    val searchdataState: LiveData<List<Drink>>
        get() = searchdataStateMut

    fun start(view: RelativeLayout) {
        viewModelScope.launch {
//            getCocktail.getCocktails().onEach { dataState -> dataStateMut.value = dataState }
//                .launchIn(viewModelScope)
            getCocktail.getCocktails().onEach { dataState ->
                when (dataState) {
                    is DataState.Error -> DataState.Error(object : Error() {})
                    DataState.Loading -> view.isVisible = true
                    is DataState.Success -> {
                        dataStateMut.value = dataState.data.drinks
                        view.isVisible = false
                    }
                }
            }
                .launchIn(viewModelScope)
        }
    }

    fun search(name: String, view: RelativeLayout, textView: TextView) {
//        viewModelScope.launch {
//            searchCocktail.searchCocktails(name)
//                .onEach { dataState -> searchdataStateMut.value = dataState }
//                .launchIn(viewModelScope)
//        }
        viewModelScope.launch {
            searchCocktail.searchCocktails(name)
                .onEach { dataState ->
                    when (dataState) {
                        is DataState.Error ->  DataState.Error(object : Error() {})
                        DataState.Loading -> view.isVisible = true
                        is DataState.Success -> {
                            view.isVisible = false
                            textView.isVisible = false
                            searchdataStateMut.value = dataState.data.drinks
                        }
                    }
                }
                .launchIn(viewModelScope)
        }
    }
}