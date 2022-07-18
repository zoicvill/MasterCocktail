package com.safr.mastercocktail.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.diff.FastAdapterDiffUtil
import com.safr.mastercocktail.databinding.FragmentFavCocktailListBinding
import com.safr.mastercocktail.domain.model.data.DrinkData
import com.safr.mastercocktail.presentation.adapters.FastFavDrinkItem
import com.safr.mastercocktail.presentation.viewmodels.FavCocktailListViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FavCocktailListFragment : Fragment() {

    private val viewModel: FavCocktailListViewModel by viewModels()

    private lateinit var analytics: FirebaseAnalytics


    private var binding: FragmentFavCocktailListBinding? = null
    private val mBinding get() = binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        analytics = Firebase.analytics
        Log.d("lol", "FavCocktailListFragment onCreate")
        analytics.logEvent("fragment_open") {
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
        viewModel.run(progressBarHolder, noFavCocktailTitle)
        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.favourites.observe(viewLifecycleOwner) { dataState ->
                dataState?.let { setList(it) }
            }
        }
    }

    private fun setList(drinkDataLocalMods: List<DrinkData>) {
        val itemAdapter = ItemAdapter<FastFavDrinkItem>()
        val fastAdapter = FastAdapter.with(itemAdapter)
        fastAdapter.onClickListener = { _, _, item, _ ->
            findNavController().navigate(
                TabFragmentDirections.actionTabFragmentToCocktailDetailFragment(
                    drinkId = item.drink.idDrink ?: 15346
                )
            )
            Log.d("lol", " onClick weatherFastAdapter ${item.drink.idDrink}")
            false
        }
        mBinding.favCocktailList.adapter = fastAdapter
        FastAdapterDiffUtil[itemAdapter] =
            drinkDataLocalMods.map(::FastFavDrinkItem)
    }

}