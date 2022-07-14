package com.safr.mastercocktail.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.asFlow
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.safr.mastercocktail.R
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

    override fun onStart() {
        super.onStart()
        Log.d("lol", "CategoryFragment onStart()")

    }

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
        mBinding.buttonRandom.setOnClickListener {
            viewModel.rand()
            viewModel.getRandomState.observe(viewLifecycleOwner) {
                onClickDrinkList(it[0].idDrink)
            }
        }
    }

    private fun subscribeObservers() {
        viewModel.categoryLive.observe(viewLifecycleOwner) {
            createAdapter(it)
        }

        viewModel.isDataLoading.observe(viewLifecycleOwner) { loading ->
            mBinding.progressBarHolder.isVisible = loading
        }

        viewModel.isError.observe(viewLifecycleOwner) { error ->
            if (error) {
                Log.d("lol", " error viewModel.isError.observe")
                findNavController().navigate(
                    TabFragmentDirections.actionTabFragmentToErrorFragment(
                        "category"
                    )
                )
            }
        }
        connectionLiveData.connect.observe(viewLifecycleOwner) { error ->
            if (!error) {
                Log.d("lol", "if  connectionLiveData")
                findNavController().navigate(
                    TabFragmentDirections.actionTabFragmentToErrorFragment(
                        "category"
                    )
                )
            }
        }
    }

    private fun setupRecyclerView(drinkDataLocalMods: List<DrinkNet>) {
        mBinding.test.apply {
            setHasFixedSize(true)
            itemAnimator = DefaultItemAnimator()
            adapter = mAdapterDrink
        }
        mAdapterDrink.setList(drinkDataLocalMods, this@CategoryFragment)
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
                Log.d("lol", "onQueryTextSubmit $text")
                return false
            }

            override fun onQueryTextChange(text: String?): Boolean {
                if (text != null) {
                    if (text.isNotBlank()) {
                        Log.d("lol", "onQueryTextChange text.isNotBlank() $text")
                        viewModel.search(text)
                        viewModel.searchDataState.observe(viewLifecycleOwner) { dataState ->
                            mBinding.noCocktailTitle.isVisible = dataState.isNullOrEmpty()
                            setupRecyclerView(dataState)
                        }

                    }
                    else {
                        Log.d("lol", "onQueryTextChange else  $text")
                        viewModel.load()
                    }
                }
                else {
                    Log.d("lol", "onQueryTextChange text == null $text")
                    viewModel.categoryLive
                }
                Log.d("lol", "onQueryTextChange $text")
                return false
            }
        })
    }

    override fun onClick(nameCat: String?) {
        findNavController().navigate(TabFragmentDirections.actionTabFragmentToCocktailListFragment(nameCat = nameCat))

        Log.d("lol", " onClick $nameCat")
    }

    override fun onClickDrinkList(drinkId: Int) {
        val bundle = bundleOf("drinkId" to drinkId)
        Navigation.findNavController(this.requireView())
            .navigate(R.id.action_tabFragment_to_cocktailDetailFragment, bundle)
    }
}

