package com.safr.mastercocktail.presentation.adapters

import android.media.Rating
import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil
import com.safr.mastercocktail.data.local.model.Drink

class DiffCallback(private val oldList: List<Drink>, private val newList: List<Drink>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].idDrink == newList[newItemPosition].idDrink
    }

    override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean {
        val (_, value, name) = oldList[oldPosition]
        val (_, value1, name1) = newList[newPosition]

        return name == name1 && value == value1
    }

    @Nullable
    override fun getChangePayload(oldPosition: Int, newPosition: Int): Any? {
        return super.getChangePayload(oldPosition, newPosition)
    }
}