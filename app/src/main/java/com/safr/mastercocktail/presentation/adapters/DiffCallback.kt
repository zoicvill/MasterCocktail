package com.safr.mastercocktail.presentation.adapters

import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import com.safr.mastercocktail.domain.model.api.DrinkNet


class DiffCallback(private val oldList: List<DrinkNet>, private val newList: List<DrinkNet>) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].idDrink == newList[newItemPosition].idDrink
    }

    override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
        return oldList[oldPosition].idDrink == newList[newPosition].idDrink
    }

    @Nullable
    override fun getChangePayload(oldPosition: Int, newPosition: Int): Any? {
        return super.getChangePayload(oldPosition, newPosition)
    }
}