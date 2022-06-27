package com.safr.mastercocktail.di

import android.content.Context
import androidx.room.Room
import com.safr.mastercocktail.data.local.CocktailDatabase
import com.safr.mastercocktail.data.local.dao.CocktailDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDrinkDb(@ApplicationContext context: Context) : CocktailDatabase {
        return Room.databaseBuilder(
            context,
            CocktailDatabase::class.java,
            CocktailDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideDrinkDAO(cocktailDatabase: CocktailDatabase) : CocktailDao {
        return cocktailDatabase.CocktailDao()
    }
}