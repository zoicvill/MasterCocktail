package com.safr.mastercocktail.domain.usecases.api

import com.safr.mastercocktail.core.DataState
import com.safr.mastercocktail.data.network.model.DetailListDrink
import com.safr.mastercocktail.domain.repository.CocktailRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CocktailDetailsApiUseCases @Inject constructor(private val rep: CocktailRepository)  {
    suspend fun getCocktailDetails(cocktailId: Int): Flow<DataState<DetailListDrink>>{
        return rep.getCocktailDetails(cocktailId)
    }
}