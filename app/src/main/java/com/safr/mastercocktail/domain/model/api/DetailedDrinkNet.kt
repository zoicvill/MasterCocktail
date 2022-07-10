package com.safr.mastercocktail.domain.model.api

data class DetailedDrinkNet(
    val idDrink: Int?,
    val strDrink: String?,
    val strCategory: String?,
    val strAlcoholic: String?,
    val strGlass: String?,
    val strInstructions: String?,
    val strDrinkThumb: String?,
    val listIngredients: List<String?>,
    val strImageSource: String?,
)
