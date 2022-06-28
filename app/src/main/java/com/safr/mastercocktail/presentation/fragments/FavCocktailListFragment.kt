package com.safr.mastercocktail.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.safr.mastercocktail.R
import com.safr.mastercocktail.core.DataState
import com.safr.mastercocktail.data.local.model.Drink
import com.safr.mastercocktail.databinding.FragmentFavCocktailListBinding
import com.safr.mastercocktail.presentation.adapters.MyFavDrinkRecyclerViewAdapter
import com.safr.mastercocktail.presentation.viewmodels.FavCocktailListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_cocktail_list.*
import kotlinx.android.synthetic.main.fragment_cocktail_list.list
import kotlinx.android.synthetic.main.fragment_fav_cocktail_list.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class FavCocktailListFragment : Fragment(), MyFavDrinkRecyclerViewAdapter.DrinkListClickListener {

    private var param1: String? = null
    private var param2: String? = null

    private val favCocktailListViewModel : FavCocktailListViewModel by viewModels()

    private lateinit var analytics: FirebaseAnalytics

    private var columnCount = 2


    private var binding: FragmentFavCocktailListBinding? = null
    private val mBinding get() = binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


        analytics = Firebase.analytics

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObservers()
    }

    private fun subscribeObservers() = mBinding.run {
        favCocktailListViewModel.dataState.observe(viewLifecycleOwner) { dataState ->
            when (dataState) {
                is DataState.Loading -> {
                    progressBarHolder.visibility = View.VISIBLE
                }
                is DataState.Success<List<Drink>> -> {
                    progressBarHolder.visibility = View.GONE
                    if (dataState.data.isEmpty()) {
                        no_fav_cocktail_title.visibility = View.VISIBLE
                    }
                    else {
                        no_fav_cocktail_title.visibility = View.INVISIBLE
                        setupRecyclerView(dataState.data)
                    }
                }
                is DataState.Error -> TODO()
            }
        }
    }

    private fun setupRecyclerView(drinks : List<Drink>) {
        list.layoutManager = GridLayoutManager(context, columnCount)
        list.adapter = MyFavDrinkRecyclerViewAdapter(drinks, this)
    }

    override fun onClickDrinkList(drinkId: Int) {
        val bundle = bundleOf("drinkId" to drinkId)
        Navigation.findNavController(this.view!!)
            .navigate(R.id.action_tabFragment_to_cocktailDetailFragment, bundle)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FavCocktailListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}