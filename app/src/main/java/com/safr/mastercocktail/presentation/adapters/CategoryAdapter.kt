package com.safr.mastercocktail.presentation.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.safr.mastercocktail.databinding.ItemViewCategoryBinding
import com.safr.mastercocktail.domain.model.api.CategoryNet
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryAdapter @Inject constructor() : RecyclerView.Adapter<CategoryAdapter.VH>() {

    private var mList = ArrayList<CategoryNet>()
    private lateinit var onClick: CategoryClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(
            ItemViewCategoryBinding.inflate(
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


    @SuppressLint("NotifyDataSetChanged")
    fun setList(dList: List<CategoryNet>, onClickSet: CategoryClickListener) {
        mList.clear()
        mList.addAll(dList)
        notifyDataSetChanged()
        onClick = onClickSet
    }

    inner class VH(private val viewCat: ItemViewCategoryBinding) : RecyclerView.ViewHolder(viewCat.root) {
        fun bind(position: Int) = viewCat.run {
            catName.text = mList[position].strCategory
            root.setOnClickListener {
                onClick.onClick(mList[position].strCategory)
            }
        }
    }

    interface CategoryClickListener {
        fun onClick(nameCat: String?)
    }
}