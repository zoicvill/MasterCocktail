package com.safr.mastercocktail.presentation.viewmodels

import android.app.Application
import android.widget.RelativeLayout
import androidx.core.view.isVisible
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.safr.mastercocktail.core.DataState
import com.safr.mastercocktail.data.network.model.CategoryNetMod
import com.safr.mastercocktail.domain.model.api.CatModelListNet
import com.safr.mastercocktail.domain.model.api.CategoryNet
import com.safr.mastercocktail.domain.usecases.api.CategoryApiUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val category: CategoryApiUseCases,
    application: Application
) : AndroidViewModel(application) {

    private val mutCategory: MutableLiveData<List<CategoryNet>> = MutableLiveData()
    val categoryLive: LiveData<List<CategoryNet>>
        get() = mutCategory


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
}