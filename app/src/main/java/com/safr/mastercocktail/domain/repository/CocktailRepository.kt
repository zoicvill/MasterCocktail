package com.safr.mastercocktail.domain.repository

import android.os.Bundle
import com.safr.mastercocktail.core.DataState
import com.safr.mastercocktail.data.local.model.Drink
import com.safr.mastercocktail.data.local.model.Drinks
import com.safr.mastercocktail.data.network.model.DetailListDrink
import com.safr.mastercocktail.data.network.model.DetailedDrink
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface CocktailRepository {

    suspend fun getCocktails(): Flow<DataState<Drinks>>

    suspend fun searchCocktails(name : String): Flow<DataState<Drinks>>

    suspend fun getCocktailDetails(cocktailId : Int): Flow<DataState<DetailListDrink>>

    suspend fun getFavourites() : Flow<DataState<List<Drink>>>

    suspend fun addToFavourites(cocktail : Drink)

    suspend fun removeFromFavourites(cocktail : Drink)

    suspend fun checkIfFavourite(id: Int) : Flow<DataState<Boolean>>

}