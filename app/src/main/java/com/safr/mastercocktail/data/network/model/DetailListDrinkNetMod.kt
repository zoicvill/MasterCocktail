package com.safr.mastercocktail.data.network.model

import android.util.Log
import com.google.gson.annotations.SerializedName
import com.safr.mastercocktail.core.Mapper
import com.safr.mastercocktail.domain.model.api.DetailListDrinkNet

data class DetailListDrinkNetMod(@SerializedName("drinks")var drinks: List<DetailedDrinkNetMod>?) :
    Mapper<DetailListDrinkNet> {
    override fun to(): DetailListDrinkNet? {
        return drinks?.map { it.to() }?.let { DetailListDrinkNet(it) }

    }
}
