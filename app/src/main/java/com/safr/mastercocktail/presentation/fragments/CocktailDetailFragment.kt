package com.safr.mastercocktail.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.safr.mastercocktail.R
import com.safr.mastercocktail.core.DataState
import com.safr.mastercocktail.databinding.FragmentCocktailDetailBinding
import com.safr.mastercocktail.domain.model.api.DetailedDrinkNet
import com.safr.mastercocktail.presentation.viewmodels.CocktailDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_cocktail_detail.*


private const val ARG_PARAM1 = "drinkId"

@AndroidEntryPoint
class CocktailDetailFragment : Fragment() {

    private var drinkId: Int? = null
    private var isFavourite: Boolean = false

    private var binding: FragmentCocktailDetailBinding? = null
    private val mBinding get() = binding!!

    private lateinit var analytics: FirebaseAnalytics

    private val viewModel: CocktailDetailViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            drinkId = it.getInt(ARG_PARAM1)
        }
        analytics = Firebase.analytics
        val bundle = Bundle()
        bundle.putString("name", "cocktail_detail")
        analytics.logEvent("fragment_open", bundle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCocktailDetailBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.start(drinkId ?: 15346, mBinding.progressBarHolder)
        like()
        subscribeObservers()
    }

    private fun like() = mBinding.run {
        mBinding.likeStar.setOnClickListener {
            if (!isFavourite) {
                like_star.setImageDrawable(
                    AppCompatResources.getDrawable(
                        context!!,
                        R.drawable.ic_star_24
                    )
                )
                isFavourite = true
                viewModel.addCocktailToFavourit(progressBarHolder)
                Toast.makeText(context, "Added to Favourites!", Toast.LENGTH_SHORT).show()

            }
            else {
                like_star.setImageDrawable(
                    AppCompatResources.getDrawable(
                        context!!,
                        R.drawable.ic_star_outline_24
                    )
                )
                isFavourite = false
                viewModel.removeCocktailFromFavourit(progressBarHolder)
                Toast.makeText(context, "Removed from Favourites!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun subscribeObservers() = mBinding.run {
        viewModel.detailListDrink.observe(viewLifecycleOwner) { dataState ->
            displayData(dataState)
        }

        viewModel.favourites.observe(viewLifecycleOwner) { dataState ->
            when (dataState) {
                is DataState.Success<Boolean> -> {
                    isFavourite = dataState.data
                    if (isFavourite) {
                        like_star.setImageDrawable(
                            AppCompatResources.getDrawable(
                                context!!,
                                R.drawable.ic_star_24
                            )
                        )
                    }
                    else {
                        like_star.setImageDrawable(
                            AppCompatResources.getDrawable(
                                context!!,
                                R.drawable.ic_star_outline_24
                            )
                        )
                    }
                }
                else -> {
                    DataState.Error(object : Error() {})
                }
            }
        }
    }

    private fun displayData(cocktail: DetailedDrinkNet) {
        cocktail_name.text = cocktail.strDrink
        Glide.with(this)
            .load(cocktail.strDrinkThumb)
            .into(cocktailImage)
        cocktail_instructions.text = cocktail.strInstructions
//        cocktail_ingridients.text = getIngridientsAndMeasures(cocktail)
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