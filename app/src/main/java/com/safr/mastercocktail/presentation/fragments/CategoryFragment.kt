package com.safr.mastercocktail.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import com.safr.mastercocktail.databinding.FragmentCategoryBinding
import com.safr.mastercocktail.domain.model.api.CategoryNet
import com.safr.mastercocktail.presentation.adapters.CategoryAdapter
import com.safr.mastercocktail.presentation.viewmodels.CategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CategoryFragment : Fragment(), CategoryAdapter.CategoryClickListener {

    private var binding: FragmentCategoryBinding? = null
    private val mBinding get() = binding!!

    private val viewModel: CategoryViewModel by viewModels()

    @Inject
    lateinit var mAdapter: CategoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoryBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        createAdapter()
        viewModel.categoryState(mBinding.progressBarHolder)
        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.categoryLive.observe(viewLifecycleOwner) {
//            setList(it)
createAdapter(it)
        }
    }

    private fun createAdapter(setL: List<CategoryNet>) {
        mBinding.test.apply {
            setHasFixedSize(true)
            itemAnimator = DefaultItemAnimator()
            adapter = mAdapter
        }
        mAdapter.setList(setL, this@CategoryFragment)
    }

    private fun setList(setL: List<CategoryNet>) {
        Log.d("lol", "setList(setL: List<CategoryNet>)  ${setL.size}")
        mAdapter.setList(setL, this@CategoryFragment)
    }

    override fun onClick(nameCat: String?) {
        Log.d("lol", " onClick ${nameCat}")
    }
}

