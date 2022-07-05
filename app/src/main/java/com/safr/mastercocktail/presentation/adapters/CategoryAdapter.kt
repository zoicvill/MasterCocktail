package com.safr.mastercocktail.presentation.adapters

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

    init {
        Log.d("lol", "init")

    }

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
        Log.d("lol", "holder.bin  ${position}")
        holder.bind(position)
    }

    override fun getItemCount() : Int{
        Log.d("lol", "getItemCount()  ${mList.size }")
        return mList.size
    }

    fun setList(dList: List<CategoryNet>, onClickSet: CategoryClickListener) {
        mList.clear()
        mList.addAll(dList)
        onClick = onClickSet
    }

    inner class VH(private val viewCat: ItemViewCategoryBinding) : RecyclerView.ViewHolder(viewCat.root) {
        fun bind(position: Int) {
            Log.d("lol", "CategoryAdapter VH ${ mList[position]}")
            viewCat.catName.text = mList[position].strCategory
            viewCat.root.setOnClickListener {
                onClick.onClick(mList[position].strCategory)
            }
        }
    }

    interface CategoryClickListener {
        fun onClick(nameCat: String?)
    }
}