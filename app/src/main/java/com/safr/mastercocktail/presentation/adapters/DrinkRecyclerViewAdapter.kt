package com.safr.mastercocktail.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.safr.mastercocktail.R
import com.safr.mastercocktail.databinding.ItemCocktailBinding
import com.safr.mastercocktail.domain.model.api.DrinkNet

class DrinkRecyclerViewAdapter : RecyclerView.Adapter<DrinkRecyclerViewAdapter.ViewHolder>() {

    private var mValues = ArrayList<DrinkNet>()
    private var mOldValues = ArrayList<DrinkNet>()
    private lateinit var onClick: Listener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCocktailBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    fun setList(valuesSet: List<DrinkNet>?, onClickSet: Listener) {
        val diffCallback = valuesSet?.let { DiffCallback(mValues, it) }
        val diffResult = diffCallback?.let { DiffUtil.calculateDiff(it) }
        mValues.clear()
        if (valuesSet != null) {
            mValues.addAll(valuesSet)
        }
        onClick = onClickSet
        diffResult?.dispatchUpdatesTo(this)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(private val view: ItemCocktailBinding) :
        RecyclerView.ViewHolder(view.root) {

        fun bind(item: Int) = view.apply {
            drinkName.text = mValues[item].strDrink
            Glide.with(itemView.context)
                .load(mValues[item].strDrinkThumb)
                .placeholder(R.drawable.ic_martini_glass_citrus_solid)
                .into(drinkImg)

            root.apply {
                setOnClickListener {
                    onClick.onClickDrinkList(mValues[item].idDrink)
                }
            }
        }
    }

    interface Listener {
        fun onClickDrinkList(drinkId: Int)
    }
}