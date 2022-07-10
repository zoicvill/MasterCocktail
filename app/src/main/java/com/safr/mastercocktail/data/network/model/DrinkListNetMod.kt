package com.safr.mastercocktail.data.network.model

import android.util.Log
import com.google.gson.annotations.SerializedName
import com.safr.mastercocktail.core.Mapper
import com.safr.mastercocktail.domain.model.api.DrinkListNet

data class DrinkListNetMod (@SerializedName("drinks")var drinks: List<DrinkNetMod>?): Mapper<DrinkListNet> {
    override fun to(): DrinkListNet? {
        return drinks?.map { it.to() }?.let { DrinkListNet(it) }
    }
}