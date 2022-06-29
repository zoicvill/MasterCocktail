package com.safr.mastercocktail.data.network.datasourse

import com.safr.mastercocktail.data.local.model.Drinks
import com.safr.mastercocktail.data.network.model.DetailListDrink
import com.safr.mastercocktail.data.network.model.DetailedDrink
import com.safr.mastercocktail.di.RepositoryModule
import dagger.Component
import dagger.Provides
import retrofit2.http.*

interface ApiDataSource {
//    suspend fun getCocktails(type: String = "Cocktail") : Drinks

    suspend fun getCocktails() : Drinks

    suspend fun searchCocktails(type: String = "") : Drinks

    suspend fun getCocktailDetails(type: Int) : DetailListDrink

}