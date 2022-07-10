package com.safr.mastercocktail.domain.model.data

import com.safr.mastercocktail.core.Mapper
import com.safr.mastercocktail.data.local.model.DrinkDataLocalMod
import com.safr.mastercocktail.domain.model.api.DetailedDrinkNet

data class DrinkData(
    var strDrink: String?,
    var strDrinkThumb: String?,
    var idDrink: Int?
): Mapper<DrinkDataLocalMod?>
{
    constructor(detailedDrink: DetailedDrinkNet) : this(
        strDrink = detailedDrink.strDrink,
        strDrinkThumb = detailedDrink.strDrinkThumb,
        idDrink = detailedDrink.idDrink,
    )

    override fun to(): DrinkDataLocalMod {
        return DrinkDataLocalMod(strDrink,strDrinkThumb ,idDrink)
    }
}