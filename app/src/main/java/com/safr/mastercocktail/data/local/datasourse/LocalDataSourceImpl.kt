package com.safr.mastercocktail.data.local.datasourse

import com.safr.mastercocktail.data.local.dao.CocktailDao
import com.safr.mastercocktail.data.local.model.Drink
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(private val dao: CocktailDao): LocalDataSource {
    override suspend fun insert(cocktail: Drink): Long {
        return dao.insert(cocktail)
    }

    override suspend fun getFavourites(): List<Drink> {
        return dao.getFavourites()
    }

    override suspend fun remove(cocktail: Drink) {
        dao.remove(cocktail)
    }

    override suspend fun isFavourite(id: Int): Boolean {
      return dao.isFavourite(id)
    }
}