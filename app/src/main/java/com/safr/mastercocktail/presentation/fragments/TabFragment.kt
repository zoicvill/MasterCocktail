package com.safr.mastercocktail.presentation.fragments

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.safr.mastercocktail.presentation.adapters.ViewPagerAdapter
import com.safr.mastercocktail.databinding.FragmentTabBinding

import com.safr.mastercocktail.presentation.viewmodels.ConnectionLiveData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TabFragment : Fragment() {
    private val titlesArray = arrayOf(
        "Cocktails",
        "Favourites"
    )

    private var binding: FragmentTabBinding? = null
    private val mBinding get() = binding!!

    private var viewPagerAdapter: ViewPagerAdapter? = null
    private val connectionLiveData: ConnectionLiveData by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTabBinding.inflate(layoutInflater)
        return mBinding.root
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = mBinding.run {
        super.onViewCreated(view, savedInstanceState)

        viewPagerAdapter = ViewPagerAdapter(childFragmentManager, lifecycle)
        pager.adapter = viewPagerAdapter

        TabLayoutMediator(tabLayout, pager) { tab, position ->
            tab.text = titlesArray[position]
        }.attach()

        checkConnect()

    }


    private fun checkConnect() = mBinding.run {
        connectionLiveData.connect.observe(viewLifecycleOwner) { error ->
            Log.d("lol", "if  connectionLiveData")
            errrorView.root.isVisible = !error
            tabLayout.isVisible = error
            pager.isVisible = error
        }

    }
}