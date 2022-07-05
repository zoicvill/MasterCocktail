package com.safr.mastercocktail.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.safr.mastercocktail.core.Mapper
import com.safr.mastercocktail.domain.model.data.DrinkData

@Entity(tableName = "drinkDataLocalMods")
data class DrinkDataLocalMod (
    @SerializedName("strDrink")
     var strDrink: String?,
    @SerializedName("strDrinkThumb")
     var strDrinkThumb: String?,
    @SerializedName("idDrink")
    @PrimaryKey
     var idDrink: Int?
): Mapper<DrinkData> {
    override fun to(): DrinkData {
        return DrinkData( strDrink = strDrink,
        strDrinkThumb = strDrinkThumb,
        idDrink = idDrink,)
    }
}

//{
//    constructor(detailedDrink: DetailedDrinkNetMod) : this(
//        strDrink = detailedDrink.strDrink,
//        strDrinkThumb = detailedDrink.strDrinkThumb,
//        idDrink = detailedDrink.idDrink,
//    )
//
//}
