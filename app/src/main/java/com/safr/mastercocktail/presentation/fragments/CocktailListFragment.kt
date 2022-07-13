package com.safr.mastercocktail.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.safr.mastercocktail.R
import com.safr.mastercocktail.databinding.FragmentCocktailListBinding
import com.safr.mastercocktail.domain.model.api.DrinkNet
import com.safr.mastercocktail.presentation.adapters.DrinkRecyclerViewAdapter
import com.safr.mastercocktail.presentation.viewmodels.CocktailListViewModel
import com.safr.mastercocktail.presentation.viewmodels.ConnectionLiveData
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val ARG_PARAM1 = "nameCat"

@AndroidEntryPoint
class CocktailListFragment : Fragment(), DrinkRecyclerViewAdapter.Listener {

    private var binding: FragmentCocktailListBinding? = null

    private val mBinding get() = binding!!

    private var nameCat: String? = null

    private val viewModel: CocktailListViewModel by viewModels()
    private val connectionLiveData: ConnectionLiveData by viewModels()

    private lateinit var analytics: FirebaseAnalytics

    @Inject
    lateinit var mAdapter: DrinkRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        arguments?.let {
//            nameCat = it.getString(ARG_PARAM1)
//        }
        nameCat = arguments?.let { CocktailListFragmentArgs.fromBundle(it).nameCat }
        analytics = Firebase.analytics
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCocktailListBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.catDrinkFun(nameCat)
        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.catDrinkState.observe(viewLifecycleOwner) { dataState ->
            setupRecyclerView(dataState)
        }
        viewModel.isDataLoading.observe(viewLifecycleOwner) {
            mBinding.progressBarHolder.isVisible = it
        }
        viewModel.isError.observe(viewLifecycleOwner) { error ->
            if (error) {
                Log.d("lol", "CocktailListFragment viewModel.isError.observe $error")
                findNavController().navigate(
                    CocktailListFragmentDirections.actionCocktailListFragmentToErrorFragment(
                        "list", nameCat ?: "Cocktail"
                    )
                )
            }
        }
        connectionLiveData.connect.observe(viewLifecycleOwner) { error ->

            if (!error) {
                Log.d("lol", " connectionLiveData")
                findNavController().navigate(
                    CocktailListFragmentDirections.actionCocktailListFragmentToErrorFragment(
                        "list", nameCat ?: "Cocktail"
                    )
                )
            }
        }
    }


    private fun setupRecyclerView(drinkDataLocalMods: List<DrinkNet>) {
        mBinding.list.apply {
            setHasFixedSize(true)
            itemAnimator = DefaultItemAnimator()
            adapter = mAdapter
        }
        mAdapter.setList(drinkDataLocalMods, this@CocktailListFragment)
    }


    override fun onClickDrinkList(drinkId: Int) {
        val bundle = bundleOf("drinkId" to drinkId)
        Navigation.findNavController(this.requireView())
            .navigate(R.id.action_cocktailListFragment_to_cocktailDetailFragment, bundle)
    }

}