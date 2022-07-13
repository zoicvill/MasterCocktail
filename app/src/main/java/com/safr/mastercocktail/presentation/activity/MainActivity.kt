package com.safr.mastercocktail.presentation.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.transaction
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation

import androidx.navigation.fragment.findNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.safr.mastercocktail.R
import com.safr.mastercocktail.presentation.fragments.ErrorFragment
import com.safr.mastercocktail.presentation.fragments.TabFragmentDirections
import com.safr.mastercocktail.presentation.viewmodels.ConnectionLiveData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var analytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        analytics = Firebase.analytics
        val bundle = Bundle()
        bundle.putString("title","App")
        analytics.logEvent("app_started", bundle)

    }


}