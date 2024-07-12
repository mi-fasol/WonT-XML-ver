package com.example.xml_ver

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.xml_ver.databinding.ActivityMainBinding
import com.example.xml_ver.ui.main.HomeFragment
import com.example.xml_ver.ui.main.board.ClubFragment
import com.example.xml_ver.ui.main.board.HotPlaceFragment
import com.example.xml_ver.ui.main.board.MeetingFragment
import com.example.xml_ver.ui.main.setting.MyPageFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavigationView: BottomNavigationView = binding.bottomNavigation
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_meeting -> {
                    loadFragment(MeetingFragment())
                    true
                }
                R.id.navigation_club -> {
                    loadFragment(ClubFragment())
                    true
                }
                R.id.navigation_hot -> {
                    loadFragment(HotPlaceFragment())
                    true
                }
                R.id.navigation_my_page -> {
                    loadFragment(MyPageFragment())
                    true
                }
                else -> false
            }
        }

        if (savedInstanceState == null) {
            bottomNavigationView.selectedItemId = R.id.navigation_meeting
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_nav_host_fragment, fragment)
            .commit()
    }
}