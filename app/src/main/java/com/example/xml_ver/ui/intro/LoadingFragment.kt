package com.example.xml_ver.ui.intro

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.xml_ver.MainActivity
import com.example.xml_ver.R
import com.example.xml_ver.databinding.FragmentLoadingBinding
import com.example.xml_ver.util.SharedPreferenceUtil
import com.example.xml_ver.viewModel.MainViewModel
import com.example.xml_ver.viewModel.user.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoadingFragment : Fragment() {

    private val loginViewModel by viewModels<LoginViewModel>()
    private val mainViewModel by viewModels<MainViewModel>()

    private lateinit var binding: FragmentLoadingBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoadingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("미란", "로딩 프래그먼트 진입 성공")
        val navController = findNavController()

        binding.apply {
            logoImg.setImageResource(R.drawable.wont_icon)
            logoImg.setColorFilter(ContextCompat.getColor(requireContext(),R.color.mainColor))

            wontImg.setImageResource(R.drawable.wont)
            wontImg.setColorFilter(ContextCompat.getColor(requireContext(), R.color.mainColor))
        }

        checkLoginState()

    }

    fun checkLoginState() {
        viewLifecycleOwner.lifecycleScope.launch {
            delay(1500)
            loginViewModel.checkUserExists()
            loginViewModel.loginUser.collect { state ->
                when (state) {
                    LoginViewModel.LoginUserState.LOGIN -> {
                        findNavController().navigate(R.id.userRegisterFragment)
                    }
                    LoginViewModel.LoginUserState.NONE -> {
                        findNavController().navigate(R.id.loginFragment)
                    }
                    LoginViewModel.LoginUserState.SUCCESS -> {
                        val intent = Intent(context, MainActivity::class.java)
                        startActivity(intent)
                        Log.d("미란 유저 정보", SharedPreferenceUtil(requireContext()).getUser().toString())
                    }
                    else -> {
                    }
                }
            }
        }
    }
}
