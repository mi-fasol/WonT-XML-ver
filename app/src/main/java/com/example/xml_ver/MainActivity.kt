package com.example.xml_ver

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.xml_ver.databinding.ActivityMainBinding
import com.example.xml_ver.service.MyFirebaseMessagingService
import com.example.xml_ver.util.SharedPreferenceUtil
import com.example.xml_ver.util.userMyPageImageList
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication : Application() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()

        FirebaseApp.initializeApp(this)

        val notificationChannel = NotificationChannel(
            "chat_notification",
            "Chat",
            NotificationManager.IMPORTANCE_HIGH
        )
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)

        startFirebaseMessagingService()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }

        startFirebaseMessagingService()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            "chat_notification",
            "Chat Notifications",
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Channel for chat notifications"
        }

        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    private fun startFirebaseMessagingService() {
        val intent = Intent(this, MyFirebaseMessagingService::class.java)
        startService(intent)
    }
}

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private var isExpanded = false

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
                    showFloatingButton()
                    navController.navigate(R.id.meetingFragment)
                    true
                }

                R.id.navigation_club -> {
                    showFloatingButton()
                    navController.navigate(R.id.clubFragment)
                    true
                }

                R.id.navigation_hot -> {
                    showFloatingButton()
                    navController.navigate(R.id.hotPlaceFragment)
                    true
                }

                R.id.navigation_my_page -> {
                    hideFloatingButton()
                    navController.navigate(R.id.myPageFragment)
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

        hideMenu()

        binding.mainFloatingButton.setOnClickListener {
            Log.d("미란", "눌렸음")
            isExpanded = !isExpanded
            toggleMenu()
        }
    }

    private fun toggleMenu() {
        if (isExpanded) {
            showMenu()
        } else {
            hideMenu()
        }
    }

    private fun showMenu() {
        binding.meetingPostWriteButton.apply {
            visibility = View.VISIBLE
            alpha = 0f
            scaleX = 0f
            scaleY = 0f
            animate().alpha(1f).scaleX(1f).scaleY(1f).setDuration(200).start()
        }

        binding.meetingPostWriteButton.setOnClickListener {
            navController.navigate(R.id.meetingPostRegisterFragment)
            binding.meetingPostWriteButton.visibility = View.GONE
            binding.clubPostWriteButton.visibility = View.GONE
            binding.hotPlaceWriteButton.visibility = View.GONE
            binding.mainFloatingButton.visibility = View.GONE
        }

        binding.clubPostWriteButton.setOnClickListener {
            navController.navigate(R.id.clubPostRegisterFragment)
            binding.meetingPostWriteButton.visibility = View.GONE
            binding.clubPostWriteButton.visibility = View.GONE
            binding.hotPlaceWriteButton.visibility = View.GONE
            binding.mainFloatingButton.visibility = View.GONE
        }

        binding.hotPlaceWriteButton.setOnClickListener {
            navController.navigate(R.id.hotPlacePostRegisterFragment)
            binding.meetingPostWriteButton.visibility = View.GONE
            binding.clubPostWriteButton.visibility = View.GONE
            binding.hotPlaceWriteButton.visibility = View.GONE
            binding.mainFloatingButton.visibility = View.GONE
        }

        binding.clubPostWriteButton.apply {
            visibility = View.VISIBLE
            alpha = 0f
            scaleX = 0f
            scaleY = 0f
            animate().alpha(1f).scaleX(1f).scaleY(1f).setDuration(200).start()
        }
        binding.hotPlaceWriteButton.apply {
            visibility = View.VISIBLE
            alpha = 0f
            scaleX = 0f
            scaleY = 0f
            animate().alpha(1f).scaleX(1f).scaleY(1f).setDuration(200).start()
        }

        binding.mainFloatingButton.animate().rotation(45f).setDuration(200).start()
    }

    private fun hideMenu() {
        binding.meetingPostWriteButton.animate().alpha(0f).scaleX(0f).scaleY(0f).setDuration(200)
            .withEndAction {
                binding.meetingPostWriteButton.visibility = View.GONE
            }.start()
        binding.clubPostWriteButton.animate().alpha(0f).scaleX(0f).scaleY(0f).setDuration(200)
            .withEndAction {
                binding.clubPostWriteButton.visibility = View.GONE
            }.start()
        binding.hotPlaceWriteButton.animate().alpha(0f).scaleX(0f).scaleY(0f).setDuration(200)
            .withEndAction {
                binding.hotPlaceWriteButton.visibility = View.GONE
            }.start()

        binding.mainFloatingButton.animate().rotation(0f).setDuration(200).start()
    }

    fun hideBottomNavigation() {
        binding.bottomNavigation.visibility = View.GONE
    }

    fun showBottomNavigation() {
        binding.bottomNavigation.visibility = View.VISIBLE
    }

    fun hideFloatingButton() {
        isExpanded = false
        binding.mainFloatingButton.visibility = View.GONE
    }

    fun showFloatingButton() {
        binding.mainFloatingButton.visibility = View.VISIBLE
    }
}