package com.safr.mastercocktail.data.local.dao

import androidx.room.*
import com.safr.mastercocktail.data.local.model.DrinkDataLocalMod

@Dao
interface CocktailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(cocktail: DrinkDataLocalMod) : Long

    @Query("SELECT * FROM drinkDataLocalMods")
    suspend fun getFavourites() : List<DrinkDataLocalMod>

    @Delete
    suspend fun remove(cocktail: DrinkDataLocalMod)

    @Query("SELECT EXISTS(SELECT * FROM drinkDataLocalMods WHERE idDrink = :id)")
    suspend fun isFavourite(id : Int) : Boolean
}