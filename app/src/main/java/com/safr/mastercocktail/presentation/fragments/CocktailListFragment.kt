package com.safr.mastercocktail.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.diff.FastAdapterDiffUtil
import com.safr.mastercocktail.databinding.FragmentCocktailListBinding
import com.safr.mastercocktail.domain.model.api.DrinkNet
import com.safr.mastercocktail.presentation.adapters.FastDrinkItem
import com.safr.mastercocktail.presentation.viewmodels.CocktailListViewModel
import com.safr.mastercocktail.presentation.viewmodels.ConnectionLiveData
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CocktailListFragment : Fragment() {

    private var binding: FragmentCocktailListBinding? = null

    private val mBinding get() = binding!!

    private var nameCat: String? = null

    private val viewModel: CocktailListViewModel by viewModels()
    private val connectionLiveData: ConnectionLiveData by viewModels()

    private val itemAdapter = ItemAdapter<FastDrinkItem>()
    private val fastAdapter = FastAdapter.with(itemAdapter)

    private lateinit var analytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.catDrinkState.collect { dataState ->
                dataState?.let { setList(it) }
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.isDataLoading.collect {
                mBinding.progressBarHolder.isVisible = it
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.isError.collect { error ->
                mBinding.apply {
                    errorServer.isVisible = error
                    cocktailList.isVisible = !error
                }
            }
        }
        connectionLiveData.connect.observe(viewLifecycleOwner) { error ->
            Log.d("lol", " connectionLiveData")
            mBinding.errorView.root.isVisible = !error
            mBinding.cocktailList.isVisible = error

        }

    }


    private fun setList(drinkDataLocalMods: List<DrinkNet>) {
        fastAdapter.onClickListener = { _, _, item, _ ->
            findNavController().navigate(
                CocktailListFragmentDirections.actionCocktailListFragmentToCocktailDetailFragment(
                    drinkId = item.drink.idDrink
                )
            )
            Log.d("lol", " onClick weatherFastAdapter ${item.drink.idDrink}")
            false
        }
        mBinding.cocktailList.adapter = fastAdapter

        FastAdapterDiffUtil[itemAdapter] = FastAdapterDiffUtil.calculateDiff(
            itemAdapter,
            drinkDataLocalMods.map(::FastDrinkItem)
        )
    }
}