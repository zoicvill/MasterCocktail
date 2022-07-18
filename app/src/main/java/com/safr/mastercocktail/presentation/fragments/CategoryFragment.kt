package com.safr.mastercocktail.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.diff.FastAdapterDiffUtil
import com.safr.mastercocktail.databinding.FragmentCategoryBinding
import com.safr.mastercocktail.domain.model.api.CategoryNet
import com.safr.mastercocktail.domain.model.api.DrinkNet
import com.safr.mastercocktail.presentation.adapters.FastCategoryItem
import com.safr.mastercocktail.presentation.adapters.FastDrinkItem
import com.safr.mastercocktail.presentation.viewmodels.CategoryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryFragment : Fragment() {

    private var binding: FragmentCategoryBinding? = null
    private val mBinding get() = binding!!

    private val viewModel: CategoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoryBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObservers()
        searchView()
        randomCocktail()
    }

    private fun randomCocktail() {
        viewModel.rand()
        mBinding.buttonRandom.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.getRandomState.collect {
                    findNavController().navigate(
                        TabFragmentDirections.actionTabFragmentToCocktailDetailFragment(
                            drinkId = it!![0].idDrink
                        )
                    )
                }
            }

        }
    }

    private fun subscribeObservers() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.categoryLive.collect {
                it?.let { it1 -> setList(it1) }
            }

        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.isDataLoading.collect { loading ->
                mBinding.progressBarHolder.isVisible = loading
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.isDataLoading.collect { loading ->
                mBinding.progressBarHolder.isVisible = loading
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.searchDataState.collect { dataState ->
                Log.d(
                    "lol", " viewModel." +
                            "searchDataState dataState $dataState if ${dataState.isNullOrEmpty()}"
                )

                mBinding.noCocktailTitle.isVisible = dataState.isNullOrEmpty()
                dataState?.let { setupRecyclerView(it) }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.isError.collect { error ->
                mBinding.errorServer.isVisible = error
                mBinding.category.isVisible = !error
            }
        }

    }

    private fun setupRecyclerView(drinkDataLocalMods: List<DrinkNet>) {
        val itemAdapter = ItemAdapter<FastDrinkItem>()
        val fastAdapter =
            FastAdapter.with(itemAdapter)
        fastAdapter.onClickListener = { _, _, item, _ ->

            findNavController().navigate(
                TabFragmentDirections.actionTabFragmentToCocktailDetailFragment(
                    drinkId = item.drink.idDrink
                )
            )
            Log.d("lol", " onClick weatherFastAdapter ${item.drink.idDrink}")
            false
        }
        mBinding.drinkSearch.adapter = fastAdapter
        FastAdapterDiffUtil[itemAdapter] =
            drinkDataLocalMods.map(::FastDrinkItem)
    }


    private fun setList(setL: List<CategoryNet>) {
        Log.d("lol", "setList ${setL.size}")
        val itemAdapter = ItemAdapter<FastCategoryItem>()
        val fastAdapter =
            FastAdapter.with(itemAdapter)

        fastAdapter.onClickListener = { _, _, item, _ ->
            findNavController().navigate(
                TabFragmentDirections.actionTabFragmentToCocktailListFragment(
                    nameCat = item.category.strCategory
                )
            )
            Log.d("lol", " onClick weatherFastAdapter ${item.category.strCategory}")
            false
        }

        mBinding.category.adapter = fastAdapter
        FastAdapterDiffUtil[itemAdapter] =
            setL.map(::FastCategoryItem)
    }


    private fun searchView() = mBinding.run {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(text: String?): Boolean {
                Log.d("lol", "onQueryTextSubmit $text")
                return false
            }

            override fun onQueryTextChange(text: String?): Boolean {
                if (text != null) if (text.isNotBlank()) {
                    Log.d("lol", "onQueryTextChange text.isNotBlank() $text")
                    viewModel.search(text)
                    category.isVisible = false
                    drinkSearch.isVisible = true
                } else {
                    Log.d("lol", "onQueryTextChange else  $text")
                    category.isVisible = true
                    drinkSearch.isVisible = false

                }
                Log.d("lol", "onQueryTextChange $text")
                return false
            }
        })
    }
}

