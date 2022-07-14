package com.safr.mastercocktail.data.network.model

import com.google.gson.annotations.SerializedName
import com.safr.mastercocktail.core.Mapper
import com.safr.mastercocktail.domain.model.api.CatModelListNet
import java.io.Serializable

data class CatModelListNetMod(@SerializedName("drinks") val cat: List<CategoryNetMod>?): Mapper<CatModelListNet?>, Serializable {
    override fun to(): CatModelListNet? {
        return cat?.map { it.to() }?.let { CatModelListNet(it) }
    }
}
