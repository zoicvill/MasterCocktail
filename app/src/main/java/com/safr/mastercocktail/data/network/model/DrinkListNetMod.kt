package com.safr.mastercocktail.data.network.model

import com.safr.mastercocktail.Mapper1
import com.safr.mastercocktail.domain.model.api.DrinkListNet

data class DrinkListNetMod (var drinks: List<DrinkNetMod>): Mapper1<DrinkListNet>{
    override fun to(): DrinkListNet {
        return DrinkListNet(drinks.map { it.to() })
    }
}