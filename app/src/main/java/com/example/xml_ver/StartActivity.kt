package com.example.xml_ver

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.xml_ver.databinding.ActivityStartBinding
import com.example.xml_ver.viewModel.MainViewModel
import com.example.xml_ver.viewModel.user.LoginViewModel
import com.example.xml_ver.viewModel.user.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

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
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
            navHostFragment?.let {
                navController = it.navController

                navController.navigate(R.id.loadingFragment)
            } ?: throw IllegalStateException("NavHostFragment not found")
        }
    }
}