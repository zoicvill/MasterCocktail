package com.safr.mastercocktail.data.network.model

import com.google.gson.annotations.SerializedName
import com.safr.mastercocktail.core.Mapper
import com.safr.mastercocktail.domain.model.api.CategoryNet
import java.io.Serializable

data class CategoryNetMod(@SerializedName("strCategory") val strCategory: String?) :
    Mapper<CategoryNet>, Serializable {
    override fun to(): CategoryNet {
        return CategoryNet(strCategory)
    }
}