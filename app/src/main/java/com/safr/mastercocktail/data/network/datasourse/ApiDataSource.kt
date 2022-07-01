package com.safr.mastercocktail.data.network.datasourse

import com.safr.mastercocktail.domain.model.api.DetailListDrinkNet
import com.safr.mastercocktail.domain.model.api.DrinkListNet

interface ApiDataSource {
    suspend fun getCocktails(type: String = "Cocktail") : DrinkListNet

    suspend fun searchCocktails(type: String = "") : DrinkListNet

    suspend fun getCocktailDetails(type: Int) : DetailListDrinkNet

}