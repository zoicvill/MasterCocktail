package com.safr.mastercocktail.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DefaultItemAnimator
import com.safr.mastercocktail.R
import com.safr.mastercocktail.databinding.FragmentCategoryBinding
import com.safr.mastercocktail.domain.model.api.CategoryNet
import com.safr.mastercocktail.domain.model.api.DrinkNet
import com.safr.mastercocktail.presentation.adapters.CategoryAdapter
import com.safr.mastercocktail.presentation.adapters.DiffCallback
import com.safr.mastercocktail.presentation.adapters.DrinkRecyclerViewAdapter
import com.safr.mastercocktail.presentation.adapters.Listener
import com.safr.mastercocktail.presentation.viewmodels.CategoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CategoryFragment : Fragment(), CategoryAdapter.CategoryClickListener, Listener {

    private var binding: FragmentCategoryBinding? = null
    private val mBinding get() = binding!!

    private val viewModel: CategoryViewModel by viewModels()

    @Inject
    lateinit var mAdapter: CategoryAdapter

    @Inject
    lateinit var mAdapterDrink: DrinkRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoryBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.categoryState(mBinding.progressBarHolder)
        subscribeObservers()
        searchView()
        mBinding.buttonRandom.setOnClickListener {
            viewModel.getRandomState.observe(viewLifecycleOwner){
                onClickDrinkList(it[0].idDrink)
            }
        }
    }

    private fun subscribeObservers() {
        viewModel.start(mBinding.progressBarHolder)
        viewModel.categoryLive.observe(viewLifecycleOwner) {
            createAdapter(it)
        }
        viewModel.searchDataState.observe(viewLifecycleOwner) { dataState ->
            setupRecyclerView(dataState)
        }
    }

    private fun setupRecyclerView(drinkDataLocalMods: List<DrinkNet>) {
        mBinding.test.apply {
            setHasFixedSize(true)
            itemAnimator = DefaultItemAnimator()
            adapter = mAdapterDrink
        }
        val diffCallback = object : DiffCallback<DrinkNet>() {
            override fun areItemsTheSame(oldItem: DrinkNet, newItem: DrinkNet): Boolean {
                return oldItem.idDrink == newItem.idDrink
            }

            override fun areContentsTheSame(oldItem: DrinkNet, newItem: DrinkNet): Boolean {
                return oldItem == newItem
            }

        }
        mAdapterDrink.setList(drinkDataLocalMods, this@CategoryFragment, diffCallback)
    }

    private fun createAdapter(setL: List<CategoryNet>) {
        mBinding.test.apply {
            setHasFixedSize(true)
            itemAnimator = DefaultItemAnimator()
            adapter = mAdapter
        }
        mAdapter.setList(setL, this@CategoryFragment)
    }


    private fun searchView() = mBinding.run {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(text: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(text: String?): Boolean {
                if (text != null) {
                    if (text.isNotEmpty()) {
                        viewModel.search(text, progressBarHolder, mBinding.noCocktailTitle)
                    }
                    else {
                        viewModel.categoryState(mBinding.progressBarHolder)
                    }
                }
                else {
                    viewModel.categoryState(mBinding.progressBarHolder)
                }
                return false
            }
        })
    }

    override fun onClick(nameCat: String?) {
        val bundle = bundleOf("nameCat" to nameCat)
        Navigation.findNavController(this.requireView())
            .navigate(R.id.action_tabFragment_to_cocktailListFragment, bundle)
        Log.d("lol", " onClick $nameCat")
    }

    override fun onClickDrinkList(drinkId: Int) {
        val bundle = bundleOf("drinkId" to drinkId)
        Navigation.findNavController(this.requireView())
            .navigate(R.id.action_tabFragment_to_cocktailDetailFragment, bundle)
    }
}

