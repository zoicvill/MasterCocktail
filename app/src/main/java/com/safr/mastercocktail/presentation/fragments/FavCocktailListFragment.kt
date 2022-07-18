package com.safr.mastercocktail.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase
import com.safr.mastercocktail.databinding.FragmentFavCocktailListBinding
import com.safr.mastercocktail.domain.model.data.DrinkData
import com.safr.mastercocktail.presentation.activity.MainActivity
import com.safr.mastercocktail.presentation.adapters.FavDrinkRecyclerViewAdapter
import com.safr.mastercocktail.presentation.viewmodels.ConnectionLiveData
import com.safr.mastercocktail.presentation.viewmodels.FavCocktailListViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class FavCocktailListFragment : Fragment(), FavDrinkRecyclerViewAdapter.FavDrinkListClickListener {

    private val viewModel: FavCocktailListViewModel by viewModels()

    private lateinit var analytics: FirebaseAnalytics

    @Inject
    lateinit var mAdapter: FavDrinkRecyclerViewAdapter
    private val connectionLiveData: ConnectionLiveData by viewModels()

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
        createAdapter()
        viewModel.run(progressBarHolder, noFavCocktailTitle)
        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewLifecycleOwner.lifecycleScope.launchWhenResumed {
            viewModel.favourites.observe(viewLifecycleOwner) { dataState ->
                setList(dataState)
            }
        }
    }

    private fun createAdapter() = mBinding.run {
        mBinding.list.apply {
            setHasFixedSize(true)
            itemAnimator = DefaultItemAnimator()
            adapter = mAdapter
        }
    }

    private fun setList(drinkDataLocalMods: List<DrinkData>?){
        mAdapter.setList(drinkDataLocalMods, this@FavCocktailListFragment)
    }

    override fun onClickDrinkList(drinkId: Int?) {
        this@FavCocktailListFragment.findNavController().navigate(
            TabFragmentDirections.actionTabFragmentToCocktailDetailFragment(
                drinkId = drinkId ?: 15346
            )
        )
    }
}