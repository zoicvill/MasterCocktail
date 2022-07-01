package com.safr.mastercocktail.data.local.model

import com.google.gson.annotations.SerializedName
import com.safr.mastercocktail.Mapper1
import com.safr.mastercocktail.domain.model.data.DrinksListData

data class DrinksListDataLocalMod(
    @SerializedName("drinkDataLocalMods")
    var drinkDataLocalMods: List<DrinkDataLocalMod>
) : Mapper1<DrinksListData> {
    override fun to(): DrinksListData {
        return DrinksListData(drinkDataLocalMods.map { it.to() }.toList())
    }
}

