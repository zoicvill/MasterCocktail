package com.safr.mastercocktail.data.network.datasourse

import com.safr.mastercocktail.domain.model.api.CatModelListNet
import com.safr.mastercocktail.domain.model.api.DetailListDrinkNet
import com.safr.mastercocktail.domain.model.api.DrinkListNet

interface ApiDataSource {
    suspend fun getRandom() : DrinkListNet?

    suspend fun searchCocktails(type: String = "") : DrinkListNet?

    suspend fun getCocktailDetails(type: Int) : DetailListDrinkNet?

    suspend fun getCategory(): CatModelListNet?

    suspend fun getCocktailsCategories(type: String): DrinkListNet?

}