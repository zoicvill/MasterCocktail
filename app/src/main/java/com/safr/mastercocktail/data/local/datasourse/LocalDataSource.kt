package com.safr.mastercocktail.data.local.datasourse

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.safr.mastercocktail.core.DataState
import com.safr.mastercocktail.data.local.model.Drink
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {
    suspend fun insert(cocktail: Drink) : Long

    fun getFavourites() : Flow<DataState<List<Drink>>>

    suspend fun remove(cocktail: Drink)

    suspend fun isFavourite(id : Int) : Boolean
}