package com.safr.mastercocktail.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.safr.mastercocktail.databinding.ItemIngredientsBinding
import javax.inject.Inject

class CocktailDetailAdapter @Inject constructor() :
    RecyclerView.Adapter<CocktailDetailAdapter.VH>() {

    private var mList = ArrayList<String?>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(
            ItemIngredientsBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount() = mList.size

    fun setList(dList: List<String?>) {
        mList.clear()
        mList.addAll(dList)
    }

    inner class VH(private val viewCat: ItemIngredientsBinding) :
        RecyclerView.ViewHolder(viewCat.root) {
        fun bind(position: Int) {
            mList[position].let {  viewCat.textView.text = it}

        }

    }
}