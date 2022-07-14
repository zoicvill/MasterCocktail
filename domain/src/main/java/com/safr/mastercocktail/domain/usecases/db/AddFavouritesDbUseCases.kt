package com.safr.mastercocktail.domain.usecases.db

import com.safr.mastercocktail.domain.model.data.DrinkData
import com.safr.mastercocktail.domain.repository.CocktailRepository
import javax.inject.Inject

class AddFavouritesDbUseCases @Inject constructor(private val rep: CocktailRepository) {
    suspend fun addToFavourites(cocktail: DrinkData) {
        rep.addToFavourites(cocktail)
    }
}