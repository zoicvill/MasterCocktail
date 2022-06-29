package com.safr.mastercocktail.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.safr.mastercocktail.data.network.model.DetailedDrink

@Entity(tableName = "drinks")
data class Drink (
    @SerializedName("strDrink")
    var strDrink: String,
    @SerializedName("strDrinkThumb")
    var strDrinkThumb: String,
    @SerializedName("strGlass")
    var strGlass: String,
    @SerializedName("idDrink")
    @PrimaryKey
    var idDrink: Int
) {
    constructor(detailedDrink: DetailedDrink) : this(
        strDrink = detailedDrink.strDrink,
        strDrinkThumb = detailedDrink.strDrinkThumb,
        idDrink = detailedDrink.idDrink,
        strGlass = detailedDrink.strGlass
    )

}
