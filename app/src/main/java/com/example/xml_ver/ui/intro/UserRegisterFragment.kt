package com.example.xml_ver.ui.intro

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.ViewPager2
import com.example.xml_ver.MainActivity
import com.example.xml_ver.R
import com.example.xml_ver.adapter.UserSliderAdapter
import com.example.xml_ver.databinding.FragmentUserRegisterBinding
import com.example.xml_ver.util.setupButtonState
import com.example.xml_ver.util.setupSpinner
import com.example.xml_ver.util.userProfileList
import com.example.xml_ver.viewModel.user.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserRegisterFragment : Fragment() {
    private var _binding: FragmentUserRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewPager: ViewPager2
    private val userViewModel by viewModels<UserViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewPager()
        setupSpinner(binding.genderField, R.array.gender_items, requireContext()) { gender ->
            userViewModel.gender.value = gender
        }
        setupSpinner(binding.majorField, R.array.major_items, requireContext()) { major ->
            userViewModel.major.value = major
        }
        setButtonEnable()
        setupButtons()
        setupEditText()
    }

    private fun setupEditText() {
        binding.nicknameField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                userViewModel.nickname.value = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupViewPager() {
        viewPager = binding.viewPager
        val adapter = UserSliderAdapter()
        viewPager.adapter = adapter
    }

    private fun setupButtons() {
        binding.prevButton.setOnClickListener {
            val currentItem = viewPager.currentItem
            viewPager.currentItem =
                if (currentItem > 0) currentItem - 1 else userProfileList.size - 1
        }

        binding.nextButton.setOnClickListener {
            val currentItem = viewPager.currentItem
            viewPager.currentItem =
                if (currentItem < userProfileList.size - 1) currentItem + 1 else 0
        }

        binding.registrationButton.setOnClickListener {
            tryRegister()
        }
    }

    private fun tryRegister() {
        val nickname = binding.nicknameField.text.toString()

        viewLifecycleOwner.lifecycleScope.launch {
            userViewModel.major.collect { major ->
                userViewModel.gender.collect { gender ->
                    if (major != "전공 선택" && gender != "선택") {
                        userViewModel.registerUser(nickname, gender)
                        userViewModel.isRegisterSuccess.collect { isSuccess ->
                            if (isSuccess) {
                                val intent = Intent(context, MainActivity::class.java)
                                startActivity(intent)
                                requireActivity().finish()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setButtonEnable() {
        viewLifecycleOwner.lifecycleScope.launch {
            userViewModel.isValid.collect {
                setupButtonState(it, binding.registrationButton, requireContext())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}