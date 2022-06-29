package com.safr.mastercocktail.data.network.api

import com.safr.mastercocktail.data.local.model.Drinks
import com.safr.mastercocktail.data.network.model.DetailListDrink
import retrofit2.http.GET
import retrofit2.http.Query

interface CocktailApi {
//    @GET("filter.php")
//    suspend fun getCocktails(
//        @Query("c") type: String = "Cocktail",
//    ): Drinks

    @GET("random.php")
    suspend fun getCocktails(): Drinks

    @GET("search.php")
    suspend fun searchCocktails(
        @Query("s") type: String = "",
    ): Drinks

    @GET("lookup.php")
    suspend fun getCocktailDetails(
        @Query("i") type: Int,
    ): DetailListDrink
}