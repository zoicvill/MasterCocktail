package com.safr.mastercocktail.domain.repository

import com.safr.mastercocktail.core.DataState
import com.safr.mastercocktail.domain.model.api.CatModelListNet
import com.safr.mastercocktail.domain.model.api.DetailListDrinkNet
import com.safr.mastercocktail.domain.model.api.DrinkListNet
import com.safr.mastercocktail.domain.model.data.DrinkData
import kotlinx.coroutines.flow.Flow

interface CocktailRepository {

    suspend fun getRandom(): Flow<DataState<DrinkListNet?>>

    suspend fun searchCocktails(name: String): Flow<DataState<DrinkListNet?>>

    suspend fun getCocktailDetails(cocktailId: Int): Flow<DataState<DetailListDrinkNet?>>

    suspend fun getCategory(): Flow<DataState<CatModelListNet?>>

    suspend fun getCocktailsCategories(type: String): Flow<DataState<DrinkListNet?>>


    fun getFavourites(): Flow<DataState<List<DrinkData>>>

    suspend fun addToFavourites(cocktail: DrinkData)

    suspend fun removeFromFavourites(cocktail: DrinkData)

    suspend fun checkIfFavourite(id: Int): Flow<DataState<Boolean>>


}