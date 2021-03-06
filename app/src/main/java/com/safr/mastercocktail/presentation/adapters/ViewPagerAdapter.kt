package com.safr.mastercocktail.presentation.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.safr.mastercocktail.presentation.fragments.CategoryFragment
import com.safr.mastercocktail.presentation.fragments.CocktailListFragment
import com.safr.mastercocktail.presentation.fragments.FavCocktailListFragment

class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return CategoryFragment()
            1 -> return FavCocktailListFragment()
        }
        return CocktailListFragment()
    }
}