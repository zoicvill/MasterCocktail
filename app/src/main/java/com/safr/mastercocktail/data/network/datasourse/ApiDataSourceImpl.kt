package com.safr.mastercocktail.data.network.datasourse

import com.safr.mastercocktail.data.local.model.Drinks
import com.safr.mastercocktail.data.network.api.CocktailApi
import com.safr.mastercocktail.data.network.model.DetailListDrink
import com.safr.mastercocktail.data.network.model.DetailedDrink
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiDataSourceImpl @Inject constructor(private val api: CocktailApi) : ApiDataSource {
//    override suspend fun getCocktails(type: String): Drinks {
//        return api.getCocktails(type)
//    }
   override suspend fun getCocktails(): Drinks {
        return api.getCocktails()
    }

    override suspend fun searchCocktails(type: String): Drinks {
        return api.searchCocktails(type)
    }

    override suspend fun getCocktailDetails(type: Int): DetailListDrink {
        return api.getCocktailDetails(type)
    }
}