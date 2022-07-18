package com.safr.mastercocktail.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
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
import com.safr.mastercocktail.presentation.viewmodels.ConnectionLiveData
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CocktailDetailFragment : Fragment() {

    private var drinkId: Int? = null
    private var isFavourite: Boolean = false

    private var binding: FragmentCocktailDetailBinding? = null
    private val mBinding get() = binding!!

    private lateinit var analytics: FirebaseAnalytics

    private val viewModel: CocktailDetailViewModel by viewModels()
    private val connectionLiveData: ConnectionLiveData by viewModels()

    @Inject
    lateinit var mAdapter: CocktailDetailAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        drinkId = arguments?.let { CocktailDetailFragmentArgs.fromBundle(it).drinkId }
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
        mBinding.allView.isVisible = false
        viewModel.start(drinkId ?: 15346)
        subscribeObservers()
        like()

    }

    private fun like() {
        mBinding.likeStar.setOnClickListener {
            Log.d("lol", "viewmodel like()  $isFavourite")
            if (isFavourite) {
                viewModel.removeCocktailFromFavourite()
                Toast.makeText(context, "Removed from Favourites!", Toast.LENGTH_SHORT).show()
            }
            else {
                viewModel.addCocktailToFavourite()
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

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.detailListDrink.collect { dataState ->
//                Log.d("lol", "detailListDrink.observe  $isFavourite")
                dataState?.let { displayData(it) }
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.favourites.collect {
                liker(it ?: false)
                isFavourite = it ?: false
                Log.d("lol", "favourites.o/bserve  $isFavourite")
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.isDataLoading.collect {
                mBinding.allView.isVisible = !it
                mBinding.progressBarHolder.isVisible = it
//                Log.d("lol", "isDataLoading.observe  $it")
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.isError.collect { error ->


            }
        }

        connectionLiveData.connect.observe(viewLifecycleOwner) { error ->
            mBinding.errorView.root.isVisible = !error
            mBinding.allView.isVisible = error
        }


    }


    private fun createAdapter(drinkDataLocalMods: List<String?>) = mBinding.run {
        Log.d("lol", "createAdapter ${drinkDataLocalMods.size}")

        mBinding.list.apply {
            setHasFixedSize(true)
            itemAnimator = DefaultItemAnimator()
            adapter = mAdapter
        }
        mAdapter.setList(drinkDataLocalMods)
    }

    private fun displayData(cocktail: DetailedDrinkNet) = mBinding.run {
        Log.d("lol", "cocktail ${cocktail.strDrink}")
        cocktailName.text = cocktail.strDrink
        Glide.with(this@CocktailDetailFragment)
            .load(cocktail.strDrinkThumb)
            .into(cocktailImage)
        cocktailInstructions.text = cocktail.strInstructions
        createAdapter(cocktail.listIngredients)
    }

}