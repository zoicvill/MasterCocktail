package com.safr.mastercocktail.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DefaultItemAnimator
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.safr.mastercocktail.R
import com.safr.mastercocktail.databinding.FragmentCocktailListBinding
import com.safr.mastercocktail.domain.model.api.DrinkNet
import com.safr.mastercocktail.presentation.adapters.DiffCallback
import com.safr.mastercocktail.presentation.adapters.DrinkRecyclerViewAdapter
import com.safr.mastercocktail.presentation.viewmodels.CocktailListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_cocktail_list.*
import javax.inject.Inject

private const val ARG_PARAM1 = "nameCat"

@AndroidEntryPoint
class CocktailListFragment : Fragment(), DrinkRecyclerViewAdapter.DrinkListClickListener {

    private var binding: FragmentCocktailListBinding? = null

    //    private var binding: FrCocktailListBinding? = null
    private val mBinding get() = binding!!

    private var nameCat: String? = null

    private val viewModel: CocktailListViewModel by viewModels()

    private lateinit var analytics: FirebaseAnalytics

    @Inject
    lateinit var mAdapter: DrinkRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            nameCat = it.getString(ARG_PARAM1)
        }
        analytics = Firebase.analytics
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCocktailListBinding.inflate(layoutInflater)
//        binding = FrCocktailListBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.catDrinkFun(nameCat, mBinding.progressBarHolder)
        subscribeObservers()
        searchView()
    }

    private fun subscribeObservers() {
        viewModel.searchdataState.observe(viewLifecycleOwner) { dataState ->
            setupRecyclerView(dataState)

        }
        viewModel.catDrinkState.observe(viewLifecycleOwner) { dataState ->
            setupRecyclerView(dataState)
        }
    }


    private fun searchView() = mBinding.run {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(text: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(text: String?): Boolean {
                if (text != null) {
                    if (text.isNotEmpty()) {
                        viewModel.search(text, progressBarHolder, no_cocktail_title)
                    }
                    else {
                        viewModel.start(progressBarHolder)
                    }
                }
                else {
                    viewModel.start(progressBarHolder)
                }
                return false
            }
        })
    }

    private fun setupRecyclerView(drinkDataLocalMods: List<DrinkNet>) {
        mBinding.list.apply {
            setHasFixedSize(true)
            itemAnimator = DefaultItemAnimator()
            adapter = mAdapter
        }
        val diffCallback = object : DiffCallback<DrinkNet>() {
            override fun areItemsTheSame(oldItem: DrinkNet, newItem: DrinkNet): Boolean {
                return oldItem.idDrink == newItem.idDrink
            }

            override fun areContentsTheSame(oldItem: DrinkNet, newItem: DrinkNet): Boolean {
                return oldItem == newItem
            }

        }
        mAdapter.setList(drinkDataLocalMods, this@CocktailListFragment, diffCallback)
    }


    override fun onClickDrinkList(drinkId: Int) {
        val bundle = bundleOf("drinkId" to drinkId)
        Navigation.findNavController(this.requireView())
            .navigate(R.id.action_cocktailListFragment_to_cocktailDetailFragment, bundle)
    }

}