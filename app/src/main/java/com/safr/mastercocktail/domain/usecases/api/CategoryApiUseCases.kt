package com.safr.mastercocktail.domain.usecases.api

import com.safr.mastercocktail.core.DataState
import com.safr.mastercocktail.domain.model.api.CatModelListNet
import com.safr.mastercocktail.domain.repository.CocktailRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryApiUseCases @Inject constructor(private val rep: CocktailRepository) {
    suspend fun getCategory(): Flow<DataState<CatModelListNet?>> {
        return rep.getCategory()
    }
}