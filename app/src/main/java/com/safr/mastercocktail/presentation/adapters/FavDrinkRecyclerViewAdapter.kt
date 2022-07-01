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

class FavDrinkRecyclerViewAdapter() :
    RecyclerView.Adapter<FavDrinkRecyclerViewAdapter.ViewHolder>() {

    private var mValues = ArrayList<DrinkData>()
    private var mOldValues = ArrayList<DrinkData>()
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

    fun setList(
        valuesSet: List<DrinkData>,
        onClickSet: DrinkListClickListener,
        diffCallback: DiffCallback<DrinkData>
    ) {

        mValues.clear()
        mValues.addAll(valuesSet)
        notifyDataSetChanged()

        mOldValues.clear()
        mOldValues.addAll(valuesSet)

        onClick = onClickSet
//        val diffCallback = DiffCallback(values, valuesSet)
//        val diffResult = DiffUtil.calculateDiff(diffCallback)
//        diffResult.dispatchUpdatesTo(this)

        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun getOldListSize(): Int {
                return mOldValues.size
            }

            override fun getNewListSize(): Int {
                return mValues.size
            }

            override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
                return diffCallback.areItemsTheSame(
                    mOldValues[oldItemPosition],
                    mValues[newItemPosition]
                )
            }


            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return diffCallback.areItemsTheSame(
                    mOldValues[oldItemPosition],
                    mValues[newItemPosition]
                )
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return diffCallback.areContentsTheSame(
                    mOldValues[oldItemPosition],
                    mValues[newItemPosition]
                )
            }

        })
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mValues[position]
        holder.itemView.setOnClickListener {
            onClick.onClickDrinkList(item.idDrink)
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

    interface DrinkListClickListener {
        fun onClickDrinkList(drinkId: Int)
    }

}