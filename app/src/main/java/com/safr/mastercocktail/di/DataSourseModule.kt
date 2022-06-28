package com.safr.mastercocktail.di

import com.safr.mastercocktail.data.local.dao.CocktailDao
import com.safr.mastercocktail.data.local.datasourse.LocalDataSource
import com.safr.mastercocktail.data.local.datasourse.LocalDataSourceImpl
import com.safr.mastercocktail.data.network.api.CocktailApi
import com.safr.mastercocktail.data.network.datasourse.ApiDataSource
import com.safr.mastercocktail.data.network.datasourse.ApiDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourseModule {


    @Provides
    @Singleton
    fun provideLocalDataSource(dao: CocktailDao): LocalDataSource {
        return LocalDataSourceImpl(dao)
    }

    @Provides
    @Singleton
    fun provideApiDataSource(api: CocktailApi): ApiDataSource {
        return ApiDataSourceImpl(api)
    }
}