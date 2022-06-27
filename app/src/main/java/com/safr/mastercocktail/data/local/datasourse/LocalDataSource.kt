package com.safr.mastercocktail.data.local.datasourse

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.safr.mastercocktail.data.local.model.Drink

interface LocalDataSource {
    suspend fun insert(cocktail: Drink) : Long

    suspend fun getFavourites() : List<Drink>

    suspend fun remove(cocktail: Drink)

    suspend fun isFavourite(id : Int) : Boolean
}