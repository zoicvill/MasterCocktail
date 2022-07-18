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
import androidx.recyclerview.widget.DefaultItemAnimator
import com.safr.mastercocktail.databinding.FragmentCategoryBinding
import com.safr.mastercocktail.domain.model.api.CategoryNet
import com.safr.mastercocktail.domain.model.api.DrinkNet
import com.safr.mastercocktail.presentation.adapters.CategoryAdapter
import com.safr.mastercocktail.presentation.adapters.DrinkRecyclerViewAdapter
import com.safr.mastercocktail.presentation.viewmodels.CategoryViewModel
import com.safr.mastercocktail.presentation.viewmodels.ConnectionLiveData
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CategoryFragment : Fragment(), CategoryAdapter.CategoryClickListener,
    DrinkRecyclerViewAdapter.Listener {

    private var binding: FragmentCategoryBinding? = null
    private val mBinding get() = binding!!

    private val viewModel: CategoryViewModel by viewModels()

    private val connectionLiveData: ConnectionLiveData by viewModels()

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
        createAdapter()
        subscribeObservers()
        searchView()
        randomCocktail()
    }

    private fun randomCocktail() {
        viewModel.rand()
        mBinding.buttonRandom.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launchWhenStarted {
                viewModel.getRandomState.collect {
                    it?.last()?.idDrink?.let { it1 -> onClickDrinkList(it1) }
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
        mBinding.drinkSearch.apply {
            setHasFixedSize(true)
            adapter = mAdapterDrink
            itemAnimator = DefaultItemAnimator()
            setItemViewCacheSize(20)
        }
        mAdapterDrink.setList(drinkDataLocalMods, this@CategoryFragment)
    }

    private fun createAdapter() {
        mBinding.category.apply {
            setHasFixedSize(true)
            adapter = mAdapter
            itemAnimator = DefaultItemAnimator()
            setItemViewCacheSize(20)
        }
    }

    private fun setList(setL: List<CategoryNet>) {
        Log.d("lol", "setList ${setL.size}")
        mAdapter.setList(setL, this@CategoryFragment)
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
                }
                else {
                    Log.d("lol", "onQueryTextChange else  $text")
                    category.isVisible = true
                    drinkSearch.isVisible = false

                }
                Log.d("lol", "onQueryTextChange $text")
                return false
            }
        })
    }

    override fun onClick(nameCat: String?) {
        findNavController().navigate(
            TabFragmentDirections.actionTabFragmentToCocktailListFragment(
                nameCat = nameCat
            )
        )

        Log.d("lol", " onClick $nameCat")
    }

    override fun onClickDrinkList(drinkId: Int) {
        findNavController().navigate(
            TabFragmentDirections.actionTabFragmentToCocktailDetailFragment(
                drinkId = drinkId
            )
        )
    }
}

