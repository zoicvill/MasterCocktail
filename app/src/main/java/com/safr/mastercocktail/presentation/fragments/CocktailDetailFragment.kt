package com.safr.mastercocktail.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import com.bumptech.glide.Glide
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.safr.mastercocktail.R
import com.safr.mastercocktail.databinding.FragmentCocktailDetailBinding
import com.safr.mastercocktail.domain.model.api.DetailedDrinkNet
import com.safr.mastercocktail.presentation.adapters.CocktailDetailAdapter
import com.safr.mastercocktail.presentation.viewmodels.CocktailDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


private const val ARG_PARAM1 = "drinkId"

@AndroidEntryPoint
class CocktailDetailFragment : Fragment() {

    private var drinkId: Int? = null
    private var isFavourite: Boolean = false

    private var binding: FragmentCocktailDetailBinding? = null
    private val mBinding get() = binding!!

    private lateinit var analytics: FirebaseAnalytics

    private val viewModel: CocktailDetailViewModel by viewModels()

    @Inject
    lateinit var mAdapter: CocktailDetailAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            drinkId = it.getInt(ARG_PARAM1)
        }
        Log.d("lol", "CocktailDetailFragment $drinkId")
        analytics = Firebase.analytics

        val bundle = Bundle()
        bundle.putString("name", "cocktail_detail")
        analytics.logEvent("fragment_open", bundle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCocktailDetailBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.start(
            drinkId ?: 15346,
            mBinding.progressBarHolder,
        )
        subscribeObservers()
        like()

    }

    private fun like() = mBinding.run {
        likeStar.setOnClickListener {
            Log.d("lol", "viewmodel like()  $isFavourite")
            if (isFavourite) {
                viewModel.removeCocktailFromFavourit()
                Toast.makeText(context, "Removed from Favourites!", Toast.LENGTH_SHORT).show()
            }
            else {
                viewModel.addCocktailToFavourit()
                Toast.makeText(context, "Added to Favourites!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun liker(star: Boolean) = mBinding.likeStar.run {
        if (star) {
            setImageDrawable(
                AppCompatResources.getDrawable(
                    requireContext(),
                    R.drawable.ic_star_24
                )
            )
        }
        else {
            setImageDrawable(
                AppCompatResources.getDrawable(
                    requireContext(),
                    R.drawable.ic_star_outline_24
                )
            )
        }
    }

    private fun subscribeObservers() = mBinding.run {
        viewModel.detailListDrink.observe(viewLifecycleOwner) { dataState ->
            displayData(dataState)
        }
        viewModel.favourites.observe(viewLifecycleOwner) {
            liker(it ?: false)
            isFavourite = it ?: false
            Log.d("lol", "favourites.observe  $isFavourite")
        }
    }



    private fun createAdapter(drinkDataLocalMods: List<String?>) = mBinding.run {
        mBinding.list.apply {
            setHasFixedSize(true)
            itemAnimator = DefaultItemAnimator()
            adapter = mAdapter
        }
        mAdapter.setList(drinkDataLocalMods)
    }

    private fun displayData(cocktail: DetailedDrinkNet) = mBinding.run {
        cocktailName.text = cocktail.strDrink
        Glide.with(this@CocktailDetailFragment)
            .load(cocktail.strDrinkThumb)
            .into(cocktailImage)
        cocktailInstructions.text = cocktail.strInstructions
        createAdapter(cocktail.listIngredients)
        progressBarHolder.visibility = View.GONE
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            CocktailDetailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                }
            }
    }
}