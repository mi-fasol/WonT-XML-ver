package com.example.xml_ver.ui.intro

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.example.xml_ver.databinding.FragmentLoginBinding
import com.example.xml_ver.viewModel.user.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
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

        setupButton()
        setButtonEnable()
        setupEditTexts()
    }

    private fun setButtonEnable() {
        viewLifecycleOwner.lifecycleScope.launch {
            loginViewModel.isValid.collect {
                binding.loginButton.isEnabled = it
                binding.loginButton.isClickable = it
                if (it) {
                    binding.loginButton.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.mainColor
                        )
                    )
                } else {
                    binding.loginButton.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.buttonDisabledColor
                        )
                    )
                }
            }
        }
    }

    private fun setupButton() {
        binding.apply {
            loginButton.setOnClickListener { tryLogin() }
        }
    }

    private fun setupEditTexts() {
        binding.studentIdField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                loginViewModel.id.value = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.pwdField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                loginViewModel.pwd.value = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun tryLogin() {
        val username = binding.studentIdField.text.toString()
        val password = binding.pwdField.text.toString()

        viewLifecycleOwner.lifecycleScope.launch {
            loginViewModel.login(username, password)
            loginViewModel.isLoginSuccess.collect { isSuccess ->
                if (isSuccess) {
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
                                requireActivity().finish()
                            }

                            else -> {
                            }
                        }
                    }
                } else {
                    Log.d("미란", "실패해버렷당 엣큥")
                }
            }

        }
    }
}