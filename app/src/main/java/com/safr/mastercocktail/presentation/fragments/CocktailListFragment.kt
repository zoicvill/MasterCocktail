package com.safr.mastercocktail.presentation.fragments

import android.os.Bundle
import android.util.Log
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
import com.safr.mastercocktail.domain.model.api.DetailedDrinkNet
import com.safr.mastercocktail.domain.model.api.DrinkNet
import com.safr.mastercocktail.presentation.adapters.DiffCallback
import com.safr.mastercocktail.presentation.adapters.DrinkRecyclerViewAdapter
import com.safr.mastercocktail.presentation.viewmodels.CocktailListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_cocktail_list.*
import javax.inject.Inject

@AndroidEntryPoint
class CocktailListFragment : Fragment(), DrinkRecyclerViewAdapter.DrinkListClickListener {

    private var binding: FragmentCocktailListBinding? = null
//    private var binding: FrCocktailListBinding? = null
    private val mBinding get() = binding!!

    private val viewModel: CocktailListViewModel by viewModels()

    private lateinit var analytics: FirebaseAnalytics

    @Inject
    lateinit var mAdapter: DrinkRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        analytics = Firebase.analytics
        val bundle = Bundle()
        bundle.putString("name", "cocktail_list")
        analytics.logEvent("fragment_open", bundle)
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
        createAdapter()
        viewModel.start(mBinding.progressBarHolder)
        subscribeObservers()
        searchView()
    }

    private fun subscribeObservers(){
        viewModel.getCocktailState.observe(viewLifecycleOwner) { dataState ->
            setupRecyclerView(dataState)
        }
        viewModel.searchdataState.observe(viewLifecycleOwner) { dataState ->
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


    private fun createAdapter() {
        mBinding.list.apply {
            setHasFixedSize(true)
            itemAnimator = DefaultItemAnimator()
            adapter = mAdapter
        }
    }

    private fun setupRecyclerView(drinkDataLocalMods: List<DrinkNet>) {
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
        Navigation.findNavController(this.view!!)
            .navigate(R.id.action_tabFragment_to_cocktailDetailFragment, bundle)
    }

}