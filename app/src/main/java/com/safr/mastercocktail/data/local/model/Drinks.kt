package com.safr.mastercocktail.data.local.model

import com.google.gson.annotations.SerializedName

data class Drinks (
    @SerializedName("drinks")
    var drinks: List<Drink>
)

