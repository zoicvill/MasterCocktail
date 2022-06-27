package com.safr.mastercocktail.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.safr.mastercocktail.R
import com.safr.mastercocktail.databinding.FragmentTabBinding
import com.safr.mastercocktail.presentation.adapters.ViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_tab.*


class TabFragment : Fragment() {
    private val titlesArray = arrayOf(
        "Cocktails",
        "Favourites"
    )

    private var binding: FragmentTabBinding?  = null
    private val mBinding get() = binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTabBinding.inflate(layoutInflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = mBinding.run {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ViewPagerAdapter(parentFragmentManager, lifecycle)
        pager.adapter = adapter

        TabLayoutMediator(tabLayout, pager) { tab, position ->
            tab.text = titlesArray[position]
        }.attach()
    }

}