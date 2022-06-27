package com.safr.mastercocktail.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.safr.mastercocktail.R
import com.safr.mastercocktail.databinding.FragmentCocktailDetailBinding
import com.safr.mastercocktail.presentation.viewmodels.CocktailDetailViewModel


private const val ARG_PARAM1 = "drinkId"


class CocktailDetailFragment : Fragment() {

    private var drinkId: Int? = null

    private var binding: FragmentCocktailDetailBinding? = null
    private val mBinding get() = binding!!

    private lateinit var analytics: FirebaseAnalytics

    private val viewModel : CocktailDetailViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            drinkId = it.getInt(ARG_PARAM1)
        }
        analytics = Firebase.analytics
        val bundle = Bundle()
        bundle.putString("name", "cocktail_detail")
        analytics.logEvent("fragment_open", bundle)

        viewModel.start(drinkId ?: 1)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCocktailDetailBinding.inflate(layoutInflater)
        return mBinding.root
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