package com.safr.mastercocktail.data.network.api

import com.safr.mastercocktail.data.local.model.Drinks
import com.safr.mastercocktail.data.network.model.DetailListDrink
import com.safr.mastercocktail.data.network.model.DetailedDrink
import retrofit2.http.*

interface CocktailApi {
    @GET("filter.php")
    suspend fun getCocktails(
        @Query("c") type: String = "Cocktail",
    ) : Drinks

    @GET("search.php")
    suspend fun searchCocktails(
        @Query("s") type: String = "",
    ) : Drinks

    @GET("lookup.php")
    suspend fun getCocktailDetails(
        @Query("i") type: Int,
    ) : DetailListDrink

    // DOES NOT EXIST ON API
    @POST("addnew.php")
    suspend fun addCocktail(
        @Body cocktail: DetailedDrink
    ) : Int

    // DOES NOT EXIST ON API
    @PUT("update.php")
    suspend fun updateCocktail(
        @Body cocktail: DetailedDrink
    ) : Int
}