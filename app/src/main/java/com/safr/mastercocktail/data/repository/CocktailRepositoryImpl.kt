package com.safr.mastercocktail.data.repository

import android.os.Bundle
import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
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

    private var analytics: FirebaseAnalytics = Firebase.analytics

    override suspend fun getRandom(): Flow<DataState<DrinkListNet?>> = flow {
        val bundle = Bundle()
        bundle.putString("function", "getRandom")
        analytics.logEvent("repository_called", bundle)

        emit(DataState.Loading)
        emit(DataState.Success(api.getRandom()))
    }.catch { emit(DataState.Error(it)) }
        .flowOn(Dispatchers.IO)

    override suspend fun searchCocktails(name: String): Flow<DataState<DrinkListNet?>> = flow {
        val bundle = Bundle()
        bundle.putString("function", "searchCocktails")
        analytics.logEvent("repository_called", bundle)

        emit(DataState.Loading)
        val search = api.searchCocktails(name)
        emit(DataState.Success(search))
        Log.d("lol", "CocktailRepositoryImpl test ${api.searchCocktails("lol")}")
    }.catch { emit(DataState.Error(it)) }
        .flowOn(Dispatchers.IO)

    override suspend fun getCocktailDetails(cocktailId: Int): Flow<DataState<DetailListDrinkNet?>> =
        flow {
            analytics.logEvent("repository_called") {
                param("function", "getCocktailDetails")
            }

            emit(DataState.Loading)
            val cocktail = api.getCocktailDetails(cocktailId)
            emit(DataState.Success(cocktail))

        }.catch { emit(DataState.Error(it)) }
            .flowOn(Dispatchers.IO)

    override suspend fun getCategory(): Flow<DataState<CatModelListNet?>> = flow {
        emit(DataState.Loading)
        emit(DataState.Success(api.getCategory()))
    }.catch { emit(DataState.Error(it)) }
        .flowOn(Dispatchers.IO)

    override suspend fun getCocktailsCategories(type: String): Flow<DataState<DrinkListNet?>> =
        flow {
            emit(DataState.Loading)
            emit(DataState.Success(api.getCocktailsCategories(type)))
        }.catch { emit(DataState.Error(it)) }
            .flowOn(Dispatchers.IO)


    //dao

    override fun getFavourites(): Flow<DataState<List<DrinkData>>> {
        val bundle = Bundle()
        bundle.putString("function", "getFavourites")
        analytics.logEvent("repository_called", bundle)
        return dao.getFavourites()
    }

    override suspend fun addToFavourites(cocktail: DrinkData) {
        val bundle = Bundle()
        bundle.putString("function", "addToFavourites")
        analytics.logEvent("repository_called", bundle)
        dao.insert(cocktail)
    }

    override suspend fun removeFromFavourites(cocktail: DrinkData) {
        val bundle = Bundle()
        bundle.putString("function", "removeFromFavourites")
        analytics.logEvent("repository_called", bundle)
        dao.remove(cocktail)
    }

    override suspend fun checkIfFavourite(id: Int): Flow<DataState<Boolean>> {
        val bundle = Bundle()
        bundle.putString("function", "checkIfFavourite")
        analytics.logEvent("repository_called", bundle)
        return dao.isFavourite(id)
    }


}