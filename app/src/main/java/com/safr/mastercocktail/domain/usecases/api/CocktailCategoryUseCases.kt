package com.safr.mastercocktail.domain.usecases.api

import com.safr.mastercocktail.core.DataState
import com.safr.mastercocktail.data.network.model.DrinkListNetMod
import com.safr.mastercocktail.domain.model.api.DrinkListNet
import com.safr.mastercocktail.domain.repository.CocktailRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CocktailCategoryUseCases @Inject constructor(private val rep: CocktailRepository) {
    suspend fun getCocktailsCategories(type: String): Flow<DataState<DrinkListNet?>> {
        return rep.getCocktailsCategories(type)
    }
}