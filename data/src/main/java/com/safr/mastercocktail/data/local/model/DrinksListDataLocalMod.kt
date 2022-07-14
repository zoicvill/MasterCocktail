package com.safr.mastercocktail.data.local.model

import com.google.gson.annotations.SerializedName
import com.safr.mastercocktail.core.Mapper
import com.safr.mastercocktail.domain.model.data.DrinksListData

data class DrinksListDataLocalMod(
    @SerializedName("drinkDataLocalMods")
    var drinkDataLocalMods: List<DrinkDataLocalMod>
) : Mapper<DrinksListData> {
    override fun to(): DrinksListData {
        return DrinksListData(drinkDataLocalMods.map { it.to() }.toList())
    }
}

