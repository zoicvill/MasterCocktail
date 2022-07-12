package com.safr.mastercocktail.presentation.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.safr.mastercocktail.core.DataState
import com.safr.mastercocktail.domain.model.api.DrinkNet
import com.safr.mastercocktail.domain.usecases.api.GetRandomApiUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ErrorViewModel @Inject constructor(
    private val getRandom: GetRandomApiUseCases,
    application: Application
) : AndroidViewModel(application) {

    private val mutSuccess: MutableLiveData<Boolean> = MutableLiveData()
    val isSuccess: LiveData<Boolean>
        get() = mutSuccess

    private val mutIsDataLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isDataLoading: LiveData<Boolean> = mutIsDataLoading

    private val mutIsError: MutableLiveData<Boolean> = MutableLiveData()
    val isError: LiveData<Boolean> = mutIsError



    init {
        start()
    }



   fun start() {
        mutIsDataLoading.postValue(true)
        mutIsError.postValue( false)
        mutSuccess.postValue(false)
        viewModelScope.launch {
            getRandom.getCocktails().collect { dataState ->
                when (dataState) {
                    is DataState.Error -> dataState.exception.also {
                        mutIsDataLoading.postValue(false)
                        mutIsError.postValue(true)
                        mutSuccess.postValue(false)
                        Log.d("lol", "start ${dataState.exception}")
                    }
                    DataState.Loading -> {
                        mutIsDataLoading.postValue(true)
                        delay(5000)
                        mutIsDataLoading.postValue(false)

                    }
                    is DataState.Success -> {
                        Log.d("lol", "start() error Success")
                        mutIsDataLoading.postValue(false)
                        mutIsError.postValue( false)
                        mutSuccess.postValue(true)

                    }
                }
            }
        }
    }
}