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
import com.example.xml_ver.MainActivity
import com.example.xml_ver.R
import com.example.xml_ver.databinding.FragmentLoginBinding
import com.example.xml_ver.viewModel.user.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding : FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val loginViewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            btnLogin.setOnClickListener { tryLogin() }
            viewLifecycleOwner.lifecycleScope.launch {
                loginViewModel.isValid.collect {
                    if(it) {
                        btnLogin.isEnabled = true
                    } else{
                        btnLogin.isClickable = false
                        btnLogin.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.mainGreyColor))
                    }
                }
            }
        }
    }

    private fun tryLogin() {
        val username = binding.studentIdField.text.toString()
        val password = binding.pwdField.text.toString()

        viewLifecycleOwner.lifecycleScope.launch {
            loginViewModel.login(username, password)
            loginViewModel.isLoginSuccess.collect { isSuccess ->
                if (isSuccess) {
                    val intent = Intent(context, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Log.d("미란", "실패해버렷당 엣큥")
                }
            }
        }
    }
}