package com.example.xml_ver

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.example.xml_ver.databinding.ActivityMainBinding
import com.example.xml_ver.service.MyFirebaseMessagingService
import com.google.firebase.FirebaseApp
import dagger.hilt.android.HiltAndroidApp

class MainActivity : ComponentActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)
    }
}