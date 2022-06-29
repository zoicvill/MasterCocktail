package com.safr.mastercocktail.di

import com.safr.mastercocktail.presentation.adapters.DrinkRecyclerViewAdapter
import com.safr.mastercocktail.presentation.adapters.FavDrinkRecyclerViewAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AdapterModule {

    @Provides
    fun provideFavDrinkRecyclerViewAdapter(): FavDrinkRecyclerViewAdapter{
        return FavDrinkRecyclerViewAdapter()
    }

    @Provides
    fun provideDrinkRecyclerViewAdapter(): DrinkRecyclerViewAdapter {
        return DrinkRecyclerViewAdapter()
    }
}