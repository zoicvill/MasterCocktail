package com.safr.mastercocktail.domain.usecases.db

import com.safr.mastercocktail.data.local.model.Drink
import com.safr.mastercocktail.domain.repository.CocktailRepository
import javax.inject.Inject

class AddFavouritesDbUseCases @Inject constructor(private val rep: CocktailRepository)  {
    suspend fun addToFavourites(cocktail: Drink){
        rep.addToFavourites(cocktail)
    }
}