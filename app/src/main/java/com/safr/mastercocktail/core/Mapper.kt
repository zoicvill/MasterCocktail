package com.safr.mastercocktail.core

import com.safr.mastercocktail.R
import com.safr.mastercocktail.data.local.model.DrinkDataLocalMod
import com.safr.mastercocktail.data.network.model.DetailListDrinkNetMod

interface Mapper<T, R > {
    fun toModel(value: T): R
    fun fromModel(value: R): T

    fun toModelList(value: List<T>): List<R>
    fun fromModelList(value: List<R>): List<T>
}
