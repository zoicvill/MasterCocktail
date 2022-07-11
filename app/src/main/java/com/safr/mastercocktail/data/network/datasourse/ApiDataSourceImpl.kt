package com.safr.mastercocktail.data.network.datasourse

import com.safr.mastercocktail.core.DataState
import com.safr.mastercocktail.data.network.api.CocktailApi
import com.safr.mastercocktail.data.network.model.CatModelListNetMod
import com.safr.mastercocktail.data.network.model.DetailListDrinkNetMod
import com.safr.mastercocktail.data.network.model.DrinkListNetMod
import com.safr.mastercocktail.domain.model.api.CatModelListNet
import com.safr.mastercocktail.domain.model.api.DetailListDrinkNet
import com.safr.mastercocktail.domain.model.api.DrinkListNet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiDataSourceImpl @Inject constructor(
    private val api: CocktailApi
) : ApiDataSource {

    override suspend fun getRandom(): DrinkListNetMod? {
        return api.getRandom()
    }

    override suspend fun searchCocktails(type: String): DrinkListNetMod? {
        return api.searchCocktails(type)
    }

    override suspend fun getCocktailDetails(type: Int): DetailListDrinkNetMod? {
        return api.getCocktailDetails(type)
    }

    override suspend fun getCategory(): CatModelListNetMod? {
        return api.getCategories()
    }

    override suspend fun getCocktailsCategories(type: String): DrinkListNetMod? {
        return api.getCocktailsCategories(type)
    }


}