package com.safr.mastercocktail.data.network.api

import com.safr.mastercocktail.data.network.model.CatModelListNetMod
import com.safr.mastercocktail.data.network.model.DetailListDrinkNetMod
import com.safr.mastercocktail.data.network.model.DrinkListNetMod
import retrofit2.http.GET
import retrofit2.http.Query

interface CocktailApi {
    @GET("random.php")
    suspend fun getRandom(): DrinkListNetMod

    @GET("search.php")
    suspend fun searchCocktails(
        @Query("s") type: String = "",
    ): DrinkListNetMod

    @GET("lookup.php")
    suspend fun getCocktailDetails(
        @Query("i") type: Int,
    ): DetailListDrinkNetMod

    @GET("list.php?c=list")
    suspend fun getCategories(): CatModelListNetMod

    @GET("filter.php")
    suspend fun getCocktailsCategories(
        @Query("c") type: String = "",
    ): DrinkListNetMod
}