package com.safr.mastercocktail.data.local.datasourse

import com.safr.mastercocktail.core.DataState
import com.safr.mastercocktail.data.local.model.DrinkDataLocalMod
import com.safr.mastercocktail.domain.model.data.DrinkData
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun insert(cocktail: DrinkDataLocalMod): Long?

    suspend fun getFavourites(): List<DrinkDataLocalMod>

    suspend fun remove(cocktail: DrinkDataLocalMod)

    suspend fun isFavourite(id: Int): Boolean
}