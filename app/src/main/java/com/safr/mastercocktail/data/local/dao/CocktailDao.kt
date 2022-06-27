package com.safr.mastercocktail.data.local.dao

import androidx.room.*
import com.safr.mastercocktail.data.local.model.Drink

@Dao
interface CocktailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cocktail: Drink) : Long

    @Query("SELECT * FROM drinks")
    suspend fun getFavourites() : List<Drink>

    @Delete
    suspend fun remove(cocktail: Drink)

    @Query("SELECT EXISTS(SELECT * FROM drinks WHERE idDrink = :id)")
    suspend fun isFavourite(id : Int) : Boolean
}