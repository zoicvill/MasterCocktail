package com.safr.mastercocktail.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.safr.mastercocktail.databinding.ItemListCocktailBinding
import com.safr.mastercocktail.domain.model.api.DrinkNet

class DrinkRecyclerViewAdapter : RecyclerView.Adapter<DrinkRecyclerViewAdapter.ViewHolder>() {

    private var mValues = ArrayList<DrinkNet>()
    private var mOldValues = ArrayList<DrinkNet>()
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


    fun setList(
        valuesSet: List<DrinkNet>, onClickSet: DrinkListClickListener,
        diffCallback: DiffCallback<DrinkNet>
    ) {

        mValues.clear()
        mValues.addAll(valuesSet)
        notifyDataSetChanged()

        mOldValues.clear()
        mOldValues.addAll(mValues)

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
                return diffCallback.getChangePayload(
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
        holder.bind(position)
    }

    override fun getItemCount(): Int = mValues.size

    inner class ViewHolder(private val view: ItemListCocktailBinding) :
        RecyclerView.ViewHolder(view.root) {

        fun bind(item: Int) = view.apply {
            drinkName.text = mValues[item].strDrink
            Glide.with(itemView.context)
                .load(mValues[item].strDrinkThumb)
                .into(drinkImg)

            root.apply {
                setOnClickListener {
                    onClick.onClickDrinkList(mValues[item].idDrink)
                }
            }
        }
    }

    interface DrinkListClickListener {
        fun onClickDrinkList(drinkId: Int)
    }

}