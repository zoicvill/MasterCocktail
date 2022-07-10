package com.safr.mastercocktail.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.safr.mastercocktail.databinding.ItemListCocktailBinding
import com.safr.mastercocktail.domain.model.data.DrinkData
import javax.inject.Inject

class FavDrinkRecyclerViewAdapter @Inject constructor() :
    RecyclerView.Adapter<FavDrinkRecyclerViewAdapter.ViewHolder>() {

    private var mValues = ArrayList<DrinkData>()
    private lateinit var onClick: FavDrinkListClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemListCocktailBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    fun setList(
        valuesSet: List<DrinkData>?,
        onClickSet: FavDrinkListClickListener,
    ) {
        val diffCallback = valuesSet?.let { FavDiffCallback(mValues, it) }
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

    inner class ViewHolder(private val binding: ItemListCocktailBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) = binding.run {
            itemView.setOnClickListener {
                mValues[position].idDrink?.let { it1 -> onClick.onClickDrinkList(it1) }
            }
            drinkName.text = mValues[position].strDrink
            Glide.with(itemView.context)
                .load(mValues[position].strDrinkThumb)
                .into(drinkImg)
        }
    }

    interface FavDrinkListClickListener {
        fun onClickDrinkList(drinkId: Int?)
    }

}