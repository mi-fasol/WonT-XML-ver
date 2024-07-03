package com.example.xml_ver

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.xml_ver.databinding.ActivityStartBinding
import com.example.xml_ver.screen.MainViewModel
import com.example.xml_ver.screen.intro.viewModel.LoginViewModel
import com.example.xml_ver.screen.intro.viewModel.UserViewModel
import com.example.xml_ver.service.MyFirebaseMessagingService
import com.example.xml_ver.util.SharedPreferenceUtil
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
class StartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStartBinding
    private val mainViewModel by viewModels<MainViewModel>()
    private val loginViewModel by viewModels<LoginViewModel>()
    private val userViewModel by viewModels<UserViewModel>()

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startContainer.post {
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
            navHostFragment?.let {
                navController = it.navController

                navController.navigate(R.id.loadingFragment)
            } ?: throw IllegalStateException("NavHostFragment not found")
        }
    }
}