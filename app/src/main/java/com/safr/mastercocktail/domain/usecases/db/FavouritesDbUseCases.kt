package com.safr.mastercocktail.domain.usecases.db

import com.safr.mastercocktail.core.DataState
import com.safr.mastercocktail.data.local.model.Drink
import com.safr.mastercocktail.domain.repository.CocktailRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavouritesDbUseCases @Inject constructor(private val rep: CocktailRepository)  {
    fun getFavourites(): Flow<DataState<List<Drink>>>{
        return rep.getFavourites()
    }
}