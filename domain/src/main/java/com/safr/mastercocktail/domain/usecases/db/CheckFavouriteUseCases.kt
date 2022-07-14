package com.safr.mastercocktail.domain.usecases.db

import com.safr.mastercocktail.core.DataState
import com.safr.mastercocktail.domain.repository.CocktailRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CheckFavouriteUseCases @Inject constructor(private val rep: CocktailRepository)   {
    suspend fun checkIfFavourite(id: Int): Flow<DataState<Boolean>>{
        return rep.checkIfFavourite(id)
    }
}