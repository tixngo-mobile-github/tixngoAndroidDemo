package io.tixngo.exampleAndroid.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import io.tixngo.exampleAndroid.R
import io.tixngo.exampleAndroid.databinding.ActivityMainBinding
import io.tixngo.exampleAndroid.ui.fragment.MatchesFragment
import io.tixngo.exampleAndroid.ui.fragment.NewsFragment
import io.tixngo.exampleAndroid.ui.fragment.TicketsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val navController = findNavController(io.tixngo.exampleAndroid.R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        loadFragment(NewsFragment())
        bottomNav = findViewById(io.tixngo.exampleAndroid.R.id.bottomNav) as BottomNavigationView
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                io.tixngo.exampleAndroid.R.id.news -> {
                    showTopNav()
                    loadFragment(NewsFragment())
                    true
                }
                io.tixngo.exampleAndroid.R.id.matches -> {
                    showTopNav()
                    loadFragment(MatchesFragment())
                    true
                }
                io.tixngo.exampleAndroid.R.id.tickets -> {
                    hideTopNav()
                    loadFragment(TicketsFragment())
                    true
                }
                else -> {
                    false
                }
            }
        }

    }

    private fun hideTopNav() {
        this.supportActionBar?.hide()
    }

    private fun showTopNav() {
        this.supportActionBar?.show()
    }

    private fun loadFragment(fragment: Fragment) {
        val translation = supportFragmentManager.beginTransaction()
        translation.replace(io.tixngo.exampleAndroid.R.id.content_frame, fragment)
        translation.commit()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(io.tixngo.exampleAndroid.R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}