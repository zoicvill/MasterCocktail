package com.safr.mastercocktail.presentation.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.safr.mastercocktail.data.local.model.Drink
import com.safr.mastercocktail.databinding.CocktailItemBinding
import com.safr.mastercocktail.databinding.ItemListCocktailBinding
import kotlinx.android.synthetic.main.cocktail_item.view.*

class DrinkRecyclerViewAdapter() : RecyclerView.Adapter<DrinkRecyclerViewAdapter.ViewHolder>() {

    private var values = ArrayList<Drink>()
    private lateinit var onClick: DrinkListClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemListCocktailBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }


    fun setList(valuesSet: List<Drink>, onClickSet: DrinkListClickListener) {
        val diffCallback = DiffCallback(values, valuesSet)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        values.clear()
        values.addAll(valuesSet)
        onClick = onClickSet
        diffResult.dispatchUpdatesTo(this)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val item = values[position]
        holder.bind(position)
//        holder.itemView.setOnClickListener {
//            onClick.onClickDrinkList(item.idDrink)
//        }
//        holder.titleTv.text = item.strDrink
//        Glide.with(holder.itemView.context)
//            .load(item.strDrinkThumb)
//            .into(holder.drinkImage)
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(private val view: ItemListCocktailBinding) :
        RecyclerView.ViewHolder(view.root) {

        fun bind(item: Int) = view.apply {
//            itemView.setOnClickListener { onClick.onClickDrinkList(values[item].idDrink) }

//            holder.titleTv.text = item.strDrink
            drinkName.text = values[item].strDrink
            drinkGlass.text = values[item].strGlass
            Glide.with(itemView.context)
                .load(values[item].strDrinkThumb)
                .into(drinkImg)

            root.apply {
                setOnClickListener {
                    onClick.onClickDrinkList(values[item].idDrink)
                }
            }
        }
    }

    interface DrinkListClickListener {
        fun onClickDrinkList(drinkId: Int)
    }

}