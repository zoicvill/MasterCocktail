package com.safr.mastercocktail.presentation.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.safr.mastercocktail.databinding.FragmentTabBinding
import com.safr.mastercocktail.presentation.adapters.ViewPagerAdapter
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

//        if(!isOnline(requireContext())){
////            Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
//
//            findNavController().navigate(TabFragmentDirections.actionTabFragmentToErrorFragment(""))
//        }

    }
//    @RequiresApi(Build.VERSION_CODES.M)
//    fun isOnline(context: Context): Boolean {
//        val connectivityManager =
//            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        if (connectivityManager != null) {
//            val capabilities =
//                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
//            if (capabilities != null) {
//                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
//                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
//                    return true
//                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
//                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
//                    return true
//                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
//                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
//                    return true
//                }
//            }
//        }
//        return false
//    }
}