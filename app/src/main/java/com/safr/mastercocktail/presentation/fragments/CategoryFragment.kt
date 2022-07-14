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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
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
        mBinding.buttonRandom.setOnClickListener {
            viewModel.rand()
            viewModel.getRandomState.observe(viewLifecycleOwner) {
                onClickDrinkList(it[0].idDrink)
            }
        }
    }

    private fun subscribeObservers() {
        viewModel.categoryLive.observe(viewLifecycleOwner) {
            setList(it)
        }

        viewModel.isDataLoading.observe(viewLifecycleOwner) { loading ->
            mBinding.progressBarHolder.isVisible = loading
        }

        viewModel.searchDataState.observe(viewLifecycleOwner) { dataState ->
            mBinding.noCocktailTitle.isVisible = dataState.isNullOrEmpty()
            setupRecyclerView(dataState)
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
            mAdapter.stateRestorationPolicy =
                RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
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
                        mAdapterDrink.setList(listOf(), this@CategoryFragment)

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

