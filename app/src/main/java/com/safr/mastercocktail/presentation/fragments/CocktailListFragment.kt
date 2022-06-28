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
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.safr.mastercocktail.R
import com.safr.mastercocktail.core.DataState
import com.safr.mastercocktail.data.local.model.Drink
import com.safr.mastercocktail.data.local.model.Drinks
import com.safr.mastercocktail.databinding.FragmentCocktailListBinding
import com.safr.mastercocktail.presentation.adapters.MyDrinkRecyclerViewAdapter
import com.safr.mastercocktail.presentation.viewmodels.CocktailListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_cocktail_list.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class CocktailListFragment : Fragment(), MyDrinkRecyclerViewAdapter.DrinkListClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var binding: FragmentCocktailListBinding? = null
    private val mBinding get() = binding!!

    private val viewModel: CocktailListViewModel by viewModels()

    private lateinit var analytics: FirebaseAnalytics

    private var columnCount = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        analytics = Firebase.analytics
        val bundle = Bundle()
        bundle.putString("name", "cocktail_list")
        analytics.logEvent("fragment_open", bundle)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCocktailListBinding.inflate(layoutInflater)
        return mBinding.root
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner) { dataState ->
            when (dataState) {
                is DataState.Loading -> {
                    progressBarHolder.visibility = View.VISIBLE
                }
                is DataState.Success<Drinks> -> {
                    setupRecyclerView(dataState.data.drinks)
                    progressBarHolder.visibility = View.GONE
                }
                is DataState.Error -> TODO()
            }
        }
        viewModel.searchdataState.observe(viewLifecycleOwner) { dataState ->
            when (dataState) {
                is DataState.Loading -> {
                    progressBarHolder.visibility = View.VISIBLE
                }
                is DataState.Success<Drinks> -> {
                    progressBarHolder.visibility = View.GONE
                    if (dataState.data.drinks != null) {
                        no_cocktail_title.visibility = View.GONE
                        setupRecyclerView(dataState.data.drinks)
                    }
                    else {
                        setupRecyclerView(emptyList())
                        no_cocktail_title.visibility = View.VISIBLE
                    }
                }
                is DataState.Error -> TODO()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.start()
        subscribeObservers()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(text: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(text: String?): Boolean {
                if (text != null) {
                    if (text.isNotEmpty()) {
                        val bundle = Bundle()
                        bundle.putString("search_text", text)
                        analytics.logEvent("search_cocktail", bundle)
                        viewModel.search(text)
                    }
                    else {
                        viewModel.start()
                    }
                }
                else {
                    viewModel.start()
                }
                return false
            }
        })
    }

    private fun setupRecyclerView(drinks: List<Drink>) {
        list.layoutManager = GridLayoutManager(context, columnCount)
        list.adapter = MyDrinkRecyclerViewAdapter(drinks, this)
    }

    override fun onClickDrinkList(drinkId: Int) {
        val anbundle = Bundle()
        anbundle.putInt("cocktail_id", drinkId)
        analytics.logEvent("view_details", anbundle)
        val bundle = bundleOf("drinkId" to drinkId)
        Navigation.findNavController(this.view!!)
            .navigate(R.id.action_tabFragment_to_cocktailDetailFragment, bundle)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CocktailListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}