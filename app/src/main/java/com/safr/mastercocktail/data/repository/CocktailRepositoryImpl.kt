package com.safr.mastercocktail.data.repository

import android.os.Bundle
import android.util.Log
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.safr.mastercocktail.core.DataState
import com.safr.mastercocktail.data.local.datasourse.LocalDataSource
import com.safr.mastercocktail.data.network.datasourse.ApiDataSource
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

    override suspend fun getCocktails(): Flow<DataState<DrinkListNet>> = flow {
        val bundle = Bundle()
        bundle.putString("function", "getCocktails")
        analytics.logEvent("repository_called", bundle)

        emit(DataState.Loading)
        emit(DataState.Success(api.getCocktails()))
    }.catch { emit(DataState.Error(it)) }
        .flowOn(Dispatchers.IO)

    override suspend fun searchCocktails(name: String): Flow<DataState<DrinkListNet>> = flow {
        val bundle = Bundle()
        bundle.putString("function", "searchCocktails")
        analytics.logEvent("repository_called", bundle)
        emit(DataState.Loading)
        val search = api.searchCocktails(name)
        emit(DataState.Success(search))

    }.catch { emit(DataState.Error(it)) }
        .flowOn(Dispatchers.IO)

    override suspend fun getCocktailDetails(cocktailId: Int): Flow<DataState<DetailListDrinkNet>> =
        flow {
            val bundle = Bundle()
            bundle.putString("function", "getCocktailDetails")
            analytics.logEvent("repository_called", bundle)
            emit(DataState.Loading)
            val cocktail = api.getCocktailDetails(cocktailId)
            emit(DataState.Success(cocktail))

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