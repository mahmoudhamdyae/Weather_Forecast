package com.mahmoudhamdyae.weatherforecast

import android.content.res.Configuration
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.preference.PreferenceManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Bottom Navigation
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val bottomView = findViewById<BottomNavigationView>(R.id.bottom_nav)
        setupWithNavController(bottomView, navHostFragment.navController)

        val preferences = PreferenceManager.getDefaultSharedPreferences(this).all
        val language =  if (preferences["language"] == getString(R.string.pref_language_english)) {
            "en"
        } else {
            "ar"
        }

        val res = resources
        val dm: DisplayMetrics = res.displayMetrics
        val conf: Configuration = res.configuration
        conf.setLocale(Locale(language))
        conf.setLayoutDirection(Locale(language))
        res.updateConfiguration(conf, dm)
    }
}