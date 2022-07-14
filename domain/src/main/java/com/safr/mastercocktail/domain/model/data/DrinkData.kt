package com.safr.mastercocktail.domain.model.data

import com.safr.mastercocktail.domain.model.api.DetailedDrinkNet

data class DrinkData(
    var strDrink: String?,
    var strDrinkThumb: String?,
    var idDrink: Int?
) {
    constructor(detailedDrink: DetailedDrinkNet) : this(
        strDrink = detailedDrink.strDrink,
        strDrinkThumb = detailedDrink.strDrinkThumb,
        idDrink = detailedDrink.idDrink,
    )

}