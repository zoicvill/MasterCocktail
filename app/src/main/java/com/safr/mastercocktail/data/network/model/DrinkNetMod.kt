package com.safr.mastercocktail.data.network.model

import com.safr.mastercocktail.Mapper1
import com.safr.mastercocktail.domain.model.api.DrinkNet

data class DrinkNetMod(
    var strDrink: String,
    var strDrinkThumb: String,
    var idDrink: Int
) : Mapper1<DrinkNet> {
    override fun to(): DrinkNet {
        return DrinkNet(strDrink, strDrinkThumb, idDrink)
    }
}