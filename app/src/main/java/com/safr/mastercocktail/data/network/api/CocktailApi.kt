package com.safr.mastercocktail.data.network.api

import com.safr.mastercocktail.data.network.model.DetailListDrinkNetMod
import com.safr.mastercocktail.data.network.model.DrinkListNetMod
import com.safr.mastercocktail.domain.model.api.DrinkListNet
import retrofit2.http.GET
import retrofit2.http.Query

interface CocktailApi {
    @GET("filter.php")
    suspend fun getCocktails(
        @Query("c") type: String = "Cocktail",
    ): DrinkListNetMod

    @GET("search.php")
    suspend fun searchCocktails(
        @Query("s") type: String = "",
    ): DrinkListNetMod

    @GET("lookup.php")
    suspend fun getCocktailDetails(
        @Query("i") type: Int,
    ): DetailListDrinkNetMod
}