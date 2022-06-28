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
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.safr.mastercocktail.R
import com.safr.mastercocktail.data.local.model.Drink
import com.safr.mastercocktail.databinding.FragmentFavCocktailListBinding
import com.safr.mastercocktail.presentation.adapters.MyFavDrinkRecyclerViewAdapter
import com.safr.mastercocktail.presentation.viewmodels.FavCocktailListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_fav_cocktail_list.*


@AndroidEntryPoint
class FavCocktailListFragment : Fragment(), MyFavDrinkRecyclerViewAdapter.DrinkListClickListener {

    private val viewModel: FavCocktailListViewModel by viewModels()

    private lateinit var analytics: FirebaseAnalytics

    private var columnCount = 2


    private var binding: FragmentFavCocktailListBinding? = null
    private val mBinding get() = binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        analytics = Firebase.analytics
        Log.d("lol", "FavCocktailListFragment onCreate")
        val bundle = Bundle()
        bundle.putString("name", "cocktail_fav_list")
        analytics.logEvent("fragment_open", bundle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavCocktailListBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun onResume() {
        super.onResume()
        Log.d("lol", "FavCocktailListFragment onResume()")
//        viewModel.run()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = mBinding.run {
        super.onViewCreated(view, savedInstanceState)
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

    private fun setupRecyclerView(drinks: List<Drink>) = mBinding.run {
        list.layoutManager = GridLayoutManager(context, columnCount)
        list.adapter = MyFavDrinkRecyclerViewAdapter(drinks, this@FavCocktailListFragment)
    }

    override fun onClickDrinkList(drinkId: Int) {
        val bundle = bundleOf("drinkId" to drinkId)
        Navigation.findNavController(this.view!!)
            .navigate(R.id.action_tabFragment_to_cocktailDetailFragment, bundle)
    }
}