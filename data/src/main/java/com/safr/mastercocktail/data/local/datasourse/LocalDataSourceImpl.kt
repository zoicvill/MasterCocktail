package com.safr.mastercocktail.data.local.datasourse

import com.safr.mastercocktail.data.local.dao.CocktailDao
import com.safr.mastercocktail.data.local.model.DrinkDataLocalMod
import com.safr.mastercocktail.domain.model.data.DrinkData
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val dao: CocktailDao
) : LocalDataSource {

    override suspend fun insert(cocktail: DrinkDataLocalMod): Long {
        return cocktail.let { dao.insert(it) }
    }

    override suspend fun getFavourites(): List<DrinkDataLocalMod> {
        return dao.getFavourites()
    }

    override suspend fun remove(cocktail: DrinkDataLocalMod) {
        cocktail.let { dao.remove(it) }
    }

    override suspend fun isFavourite(id: Int): Boolean {
        return dao.isFavourite(id)

    }
}