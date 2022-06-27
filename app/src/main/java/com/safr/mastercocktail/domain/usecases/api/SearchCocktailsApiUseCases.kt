package com.safr.mastercocktail.domain.usecases.api

import com.safr.mastercocktail.core.DataState
import com.safr.mastercocktail.data.local.model.Drinks
import com.safr.mastercocktail.domain.repository.CocktailRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchCocktailsApiUseCases @Inject constructor(private val rep: CocktailRepository)  {
    suspend fun searchCocktails(name: String): Flow<DataState<Drinks>> {
        return rep.searchCocktails(name)

    }
}