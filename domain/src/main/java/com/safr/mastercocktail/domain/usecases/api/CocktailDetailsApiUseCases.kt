package com.safr.mastercocktail.domain.usecases.api

import android.util.Log
import com.safr.mastercocktail.core.DataState
import com.safr.mastercocktail.domain.model.api.DetailListDrinkNet
import com.safr.mastercocktail.domain.repository.CocktailRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CocktailDetailsApiUseCases @Inject constructor(private val rep: CocktailRepository) {
    suspend fun getCocktailDetails(cocktailId: Int): Flow<DataState<DetailListDrinkNet?>> {
        Log.d("lol", "CocktailDetailsApiUseCases $cocktailId")
        return rep.getCocktailDetails(cocktailId)
    }
}