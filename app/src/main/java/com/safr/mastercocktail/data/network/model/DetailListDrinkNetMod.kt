package com.safr.mastercocktail.data.network.model

import android.util.Log
import com.safr.mastercocktail.Mapper1
import com.safr.mastercocktail.domain.model.api.DetailListDrinkNet

data class DetailListDrinkNetMod(val drinks: List<DetailedDrinkNetMod>) :
    Mapper1<DetailListDrinkNet> {
    override fun to(): DetailListDrinkNet {
        Log.d("lol DetailListDrinkNetMod ", "${drinks.map { it.to() }}")
        return DetailListDrinkNet(drinks.map { it.to() })
    }
}
