package com.safr.mastercocktail.di

import com.safr.mastercocktail.data.local.dao.CocktailDao
import com.safr.mastercocktail.data.local.datasourse.LocalDataSource
import com.safr.mastercocktail.data.network.api.CocktailApi
import com.safr.mastercocktail.data.network.datasourse.ApiDataSource
import com.safr.mastercocktail.data.repository.CocktailRepositoryImpl
import com.safr.mastercocktail.domain.repository.CocktailRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideCocktailRepository(cocktailApi: ApiDataSource, cocktailDao : LocalDataSource) : CocktailRepository {
        return CocktailRepositoryImpl(api = cocktailApi, dao = cocktailDao)
    }
}