package com.safr.mastercocktail.presentation.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.safr.mastercocktail.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var analytics: FirebaseAnalytics


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_MasterCocktail)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        analytics = Firebase.analytics
        val bundle = Bundle()
        bundle.putString("title","App")
        analytics.logEvent("app_started", bundle)

    }



}