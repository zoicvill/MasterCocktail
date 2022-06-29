package com.safr.mastercocktail.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.safr.mastercocktail.data.local.model.Drink
import com.safr.mastercocktail.databinding.CocktailListItemBinding

class FavDrinkRecyclerViewAdapter() : RecyclerView.Adapter<FavDrinkRecyclerViewAdapter.ViewHolder>() {

    private var values = ArrayList<Drink>()
    private lateinit var onClick: DrinkListClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            CocktailListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    fun setList(valuesSet: List<Drink>, onClickSet: DrinkListClickListener){
        val diffCallback = DiffCallback(values, valuesSet)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        values.clear()
        values.addAll(valuesSet)
        onClick = onClickSet
        diffResult.dispatchUpdatesTo(this)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.itemView.setOnClickListener {
            onClick.onClickDrinkList(item.idDrink)
        }
        holder.titleTv.text = item.strDrink
        Glide.with(holder.itemView.context)
            .load(item.strDrinkThumb)
            .into(holder.drinkImage)
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: CocktailListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val titleTv: TextView = binding.drinkTitle
        val drinkImage: ImageView = binding.drinkImage
    }

    interface DrinkListClickListener {
        fun onClickDrinkList(drinkId: Int)
    }

}