package com.safr.mastercocktail.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.safr.mastercocktail.core.DataState
import com.safr.mastercocktail.data.local.datasourse.LocalDataSource
import com.safr.mastercocktail.data.network.datasourse.ApiDataSource
import com.safr.mastercocktail.domain.model.api.CatModelListNet
import com.safr.mastercocktail.domain.model.api.DetailListDrinkNet
import com.safr.mastercocktail.domain.model.api.DrinkListNet
import com.safr.mastercocktail.domain.model.data.DrinkData
import com.safr.mastercocktail.domain.repository.CocktailRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CocktailRepositoryImpl @Inject constructor(
    private val dao: LocalDataSource,
    private val api: ApiDataSource
) : CocktailRepository {

    override suspend fun getRandom(): Flow<DataState<DrinkListNet?>> = flow {
        emit(DataState.Loading)
        emit(DataState.Success(api.getRandom()?.to()))
    }.catch { emit(DataState.Error(it)) }
        .flowOn(Dispatchers.IO)

    override suspend fun searchCocktails(name: String): Flow<DataState<DrinkListNet?>> = flow {
        emit(DataState.Loading)
        emit(DataState.Success(api.searchCocktails(name)?.to()))
    }.catch { emit(DataState.Error(it)) }
        .flowOn(Dispatchers.IO)

    override suspend fun getCocktailDetails(cocktailId: Int): Flow<DataState<DetailListDrinkNet?>> =
        flow {
            emit(DataState.Loading)
            emit(DataState.Success(api.getCocktailDetails(cocktailId)?.to()))

        }.catch { emit(DataState.Error(it)) }
            .flowOn(Dispatchers.IO)

    override suspend fun getCategory(): Flow<DataState<CatModelListNet?>> = flow {
        emit(DataState.Loading)
        emit(DataState.Success(api.getCategory()?.to()))
    }.catch { emit(DataState.Error(it)) }
        .flowOn(Dispatchers.IO)

    override suspend fun getCocktailsCategories(type: String): Flow<DataState<DrinkListNet?>> =
        flow {
            emit(DataState.Loading)
            emit(DataState.Success(api.getCocktailsCategories(type)?.to()))
        }.catch { emit(DataState.Error(it)) }
            .flowOn(Dispatchers.IO)


    //dao

    override fun getFavourites(): Flow<DataState<List<DrinkData>>> = flow {
        try {
            emit(DataState.Loading)
            emit(DataState.Success(dao.getFavourites().map { it.to() }))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }

    }.flowOn(Dispatchers.IO)

    override suspend fun addToFavourites(cocktail: DrinkData) {
        dao.insert(cocktail.to())
    }

    override suspend fun removeFromFavourites(cocktail: DrinkData) {
        dao.remove(cocktail.to())
    }

    override suspend fun checkIfFavourite(id: Int): Flow<DataState<Boolean>> = flow {
        try {
            emit(DataState.Loading)
            val isFavourite = dao.isFavourite(id)
            emit(DataState.Success(isFavourite))
        } catch (e: Exception) {
            emit(DataState.Error(e))
        }

    }.flowOn(Dispatchers.IO)

}