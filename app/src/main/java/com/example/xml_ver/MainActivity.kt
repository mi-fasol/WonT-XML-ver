package com.example.xml_ver

import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.xml_ver.databinding.ActivityMainBinding
import com.example.xml_ver.ui.main.board.ClubFragment
import com.example.xml_ver.ui.main.board.HotPlaceFragment
import com.example.xml_ver.ui.main.board.MeetingFragment
import com.example.xml_ver.ui.main.board.detail.MeetingDetailFragment
import com.example.xml_ver.ui.main.setting.MyPageFragment
import com.example.xml_ver.util.SharedPreferenceUtil
import com.example.xml_ver.util.userMyPageImageList
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

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

        val myPageIcon = binding.bottomNavigation.menu.findItem(R.id.navigation_my_page)
        val userImage = userMyPageImageList[SharedPreferenceUtil(this).getInt("image", 0)]
        myPageIcon.setIcon(ContextCompat.getDrawable(this, userImage))

        if (savedInstanceState == null) {
            bottomNavigationView.selectedItemId = R.id.navigation_meeting
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_nav_host_fragment, fragment)
            .commit()
    }

    fun hideBottomNavigation() {
        binding.bottomNavigation.visibility = View.GONE
    }

    fun showBottomNavigation() {
        binding.bottomNavigation.visibility = View.VISIBLE
    }
}