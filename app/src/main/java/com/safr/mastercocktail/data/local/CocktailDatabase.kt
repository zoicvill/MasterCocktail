package com.safr.mastercocktail.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.safr.mastercocktail.data.local.dao.CocktailDao
import com.safr.mastercocktail.data.local.model.Drink

@Database(entities = [Drink::class], version = 1)
abstract class CocktailDatabase : RoomDatabase() {

    abstract fun CocktailDao(): CocktailDao

    companion object {
        const val DATABASE_NAME: String = "drinks_db"
    }
}