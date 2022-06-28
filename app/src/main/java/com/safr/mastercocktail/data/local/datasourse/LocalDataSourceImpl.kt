package com.safr.mastercocktail.data.local.datasourse

import com.safr.mastercocktail.core.DataState
import com.safr.mastercocktail.data.local.dao.CocktailDao
import com.safr.mastercocktail.data.local.model.Drink
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(private val dao: CocktailDao): LocalDataSource {
    override suspend fun insert(cocktail: Drink): Long {
        return dao.insert(cocktail)
    }

    override fun getFavourites(): Flow<DataState<List<Drink>>> = flow {
        val favourites = dao.getFavourites()
        emit(DataState.Success(favourites))
//        delay(refreshIntervalMs)
    }.flowOn(Dispatchers.IO)

    override suspend fun remove(cocktail: Drink) {
        dao.remove(cocktail)
    }

    override suspend fun isFavourite(id: Int): Boolean {
      return dao.isFavourite(id)
    }
}