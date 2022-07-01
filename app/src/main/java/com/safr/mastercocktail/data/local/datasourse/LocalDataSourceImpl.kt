package com.safr.mastercocktail.data.local.datasourse

import com.safr.mastercocktail.core.DataState
import com.safr.mastercocktail.data.local.dao.CocktailDao
import com.safr.mastercocktail.domain.model.data.DrinkData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val dao: CocktailDao) : LocalDataSource {

    override suspend fun insert(cocktail: DrinkData): Long {
        return dao.insert(cocktail.to())
    }

    override fun getFavourites(): Flow<DataState<List<DrinkData>>> = flow {
        try {
            emit(DataState.Loading)
            val favourites = dao.getFavourites()
            emit(DataState.Success(favourites.map { it.to() }))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }

    }.flowOn(Dispatchers.IO)

    override suspend fun remove(cocktail: DrinkData) {
        dao.remove(cocktail.to())
    }

    override suspend fun isFavourite(id: Int): Flow<DataState<Boolean>> = flow {
        try {
            emit(DataState.Loading)
            val isFavourite = dao.isFavourite(id)
            emit(DataState.Success(isFavourite))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }

    }.flowOn(Dispatchers.IO)
}