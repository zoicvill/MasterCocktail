package com.safr.mastercocktail.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import com.safr.mastercocktail.R
import com.safr.mastercocktail.databinding.FragmentFavCocktailListBinding
import com.safr.mastercocktail.domain.model.data.DrinkData
import com.safr.mastercocktail.presentation.adapters.DiffCallback
import com.safr.mastercocktail.presentation.adapters.FavDrinkRecyclerViewAdapter
import com.safr.mastercocktail.presentation.viewmodels.FavCocktailListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_fav_cocktail_list.*
import javax.inject.Inject


@AndroidEntryPoint
class FavCocktailListFragment : Fragment(), FavDrinkRecyclerViewAdapter.FavDrinkListClickListener {

    private val viewModel: FavCocktailListViewModel by viewModels()

    private lateinit var analytics: FirebaseAnalytics

    private var columnCount = 2


    @Inject
    lateinit var mAdapter: FavDrinkRecyclerViewAdapter

    private var binding: FragmentFavCocktailListBinding? = null
    private val mBinding get() = binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        analytics = Firebase.analytics
        Log.d("lol", "FavCocktailListFragment onCreate")
        analytics.logEvent("fragment_open"){
          param("name", "cocktail_fav_list")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavCocktailListBinding.inflate(layoutInflater)
        return mBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = mBinding.run {
        super.onViewCreated(view, savedInstanceState)
        createAdapter()
        viewModel.run(progressBarHolder, no_fav_cocktail_title)
        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.favourites.observe(viewLifecycleOwner) { dataState ->
                setupRecyclerView(dataState)
            }
        }
    }

    private fun createAdapter() {
        mBinding.list.apply {
            setHasFixedSize(true)
            itemAnimator = DefaultItemAnimator()
            adapter = mAdapter
            layoutManager = GridLayoutManager(context, columnCount)
        }
    }

    private fun setupRecyclerView(drinkDataLocalMods: List<DrinkData>) = mBinding.run {
        Log.d("lol","${drinkDataLocalMods.size }")
        mAdapter.setList(drinkDataLocalMods, this@FavCocktailListFragment)
    }

    override fun onClickDrinkList(drinkId: Int?) {
        val bundle = bundleOf("drinkId" to drinkId)
        Navigation.findNavController(this.view!!)
            .navigate(R.id.action_tabFragment_to_cocktailDetailFragment, bundle)
    }
}