package com.safr.mastercocktail.presentation.viewmodels

import android.app.Application
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.safr.mastercocktail.core.DataState
import com.safr.mastercocktail.data.local.model.Drink
import com.safr.mastercocktail.domain.usecases.db.FavouritesDbUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.android.synthetic.main.fragment_fav_cocktail_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavCocktailListViewModel @Inject constructor(
    private val favour: FavouritesDbUseCases,
    application: Application
) :
    AndroidViewModel(application) {

    private val dataStateMut: MutableLiveData<List<Drink>> = MutableLiveData()

    val favourites: LiveData<List<Drink>>
        get() = dataStateMut

    //    init {
//        run()
//    }
//
    fun run(view: RelativeLayout, textView: TextView) {
        viewModelScope.launch {
//            favour.getFavourites().flowOn(Dispatchers.IO).onEach { dataState ->
//                dataStateMut.value = dataState
//            }.launchIn(viewModelScope)

            favour.getFavourites().flowOn(Dispatchers.IO).onEach { dataState ->
                when (dataState) {
                    is DataState.Error -> DataState.Error(object : Error() {})
                    DataState.Loading -> view.isVisible = true
                    is DataState.Success -> {
                        view.isVisible = false
                        if (dataState.data.isEmpty()) {
                            textView.isVisible = true
                        }
                        else {
                            textView.isVisible = false
                            dataStateMut.postValue(dataState.data)
                            Log.d(
                                "lol",
                                "FavCocktailListFragment subscribeObservers() else ${dataState.data.size} "
                            )

                        }
                    }
                }
            }.launchIn(viewModelScope)
        }
    }
}