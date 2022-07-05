package com.safr.mastercocktail.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.safr.mastercocktail.databinding.CocktailListItemBinding
import com.safr.mastercocktail.domain.model.data.DrinkData
import javax.inject.Inject

class FavDrinkRecyclerViewAdapter @Inject constructor() :
    RecyclerView.Adapter<FavDrinkRecyclerViewAdapter.ViewHolder>() {

    private var mValues = ArrayList<DrinkData>()
    private lateinit var onClick: FavDrinkListClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CocktailListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    fun setList(
        valuesSet: List<DrinkData>,
        onClickSet: FavDrinkListClickListener,
    ) {
        val diffCallback = FavDiffCallback(mValues, valuesSet)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        mValues.clear()
        mValues.addAll(valuesSet)
        onClick = onClickSet
        diffResult.dispatchUpdatesTo(this)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.itemView.setOnClickListener {
            item.idDrink?.let { it1 -> onClick.onClickDrinkList(it1) }
        }
        holder.titleTv.text = item.strDrink
        Glide.with(holder.itemView.context)
            .load(item.strDrinkThumb)
            .into(holder.drinkImage)
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(binding: CocktailListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val titleTv: TextView = binding.drinkTitle
        val drinkImage: ImageView = binding.drinkImage
    }

    interface FavDrinkListClickListener {
        fun onClickDrinkList(drinkId: Int?)
    }

}