package com.safr.mastercocktail.data.local.datasourse

import com.safr.mastercocktail.core.DataState
import com.safr.mastercocktail.domain.model.data.DrinkData
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun insert(cocktail: DrinkData): Long

    fun getFavourites(): Flow<DataState<List<DrinkData>>>

    suspend fun remove(cocktail: DrinkData)

    suspend fun isFavourite(id: Int): Flow<DataState<Boolean>>
}