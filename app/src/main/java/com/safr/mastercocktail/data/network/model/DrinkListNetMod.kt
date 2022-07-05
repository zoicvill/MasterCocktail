package com.safr.mastercocktail.data.network.model

import android.util.Log
import com.safr.mastercocktail.core.Mapper
import com.safr.mastercocktail.domain.model.api.DrinkListNet

data class DrinkListNetMod (var drinks: List<DrinkNetMod>?): Mapper<DrinkListNet> {
    override fun to(): DrinkListNet? {
        return drinks?.map { it.to() }?.let { DrinkListNet(it) }
    }
}