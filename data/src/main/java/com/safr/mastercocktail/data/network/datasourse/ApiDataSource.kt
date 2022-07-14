package com.safr.mastercocktail.data.network.datasourse

import com.safr.mastercocktail.core.DataState
import com.safr.mastercocktail.data.network.model.CatModelListNetMod
import com.safr.mastercocktail.data.network.model.DetailListDrinkNetMod
import com.safr.mastercocktail.data.network.model.DrinkListNetMod
import com.safr.mastercocktail.domain.model.api.CatModelListNet
import com.safr.mastercocktail.domain.model.api.DetailListDrinkNet
import com.safr.mastercocktail.domain.model.api.DrinkListNet
import kotlinx.coroutines.flow.Flow

interface ApiDataSource {
    suspend fun getRandom() : DrinkListNetMod?

    suspend fun searchCocktails(type: String = "") : DrinkListNetMod?

    suspend fun getCocktailDetails(type: Int) : DetailListDrinkNetMod?

    suspend fun getCategory(): CatModelListNetMod?

    suspend fun getCocktailsCategories(type: String): DrinkListNetMod?

}