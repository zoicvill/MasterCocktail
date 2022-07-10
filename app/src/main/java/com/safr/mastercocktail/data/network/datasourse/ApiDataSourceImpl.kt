package com.safr.mastercocktail.data.network.datasourse

import com.safr.mastercocktail.data.network.api.CocktailApi
import com.safr.mastercocktail.data.network.model.DrinkListNetMod
import com.safr.mastercocktail.domain.model.api.CatModelListNet
import com.safr.mastercocktail.domain.model.api.DetailListDrinkNet
import com.safr.mastercocktail.domain.model.api.DrinkListNet
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiDataSourceImpl @Inject constructor(
    private val api: CocktailApi
) : ApiDataSource {

    override suspend fun getRandom(): DrinkListNet? {
        return api.getRandom().to()
    }

    override suspend fun searchCocktails(type: String): DrinkListNet? {
        return api.searchCocktails(type).to()
    }

    override suspend fun getCocktailDetails(type: Int): DetailListDrinkNet? {
        return api.getCocktailDetails(type).to()
    }

    override suspend fun getCategory(): CatModelListNet? {
        return api.getCategories().to()
    }

    override suspend fun getCocktailsCategories(type: String): DrinkListNet? {
        return api.getCocktailsCategories(type).to()
    }

}