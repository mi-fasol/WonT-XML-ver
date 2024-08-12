package com.example.xml_ver.ui.main.board.register

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.haemo_kotlin.network.Resource
import com.example.xml_ver.MainActivity
import com.example.xml_ver.R
import com.example.xml_ver.data.retrofit.post.PostResponseModel
import com.example.xml_ver.databinding.FragmentMeetingPostRegisterBinding
import com.example.xml_ver.util.categoryList
import com.example.xml_ver.util.dayList
import com.example.xml_ver.util.hourList
import com.example.xml_ver.util.monthList
import com.example.xml_ver.util.personList
import com.example.xml_ver.util.setupButtonState
import com.example.xml_ver.util.setupSpinner
import com.example.xml_ver.util.setupSpinnerClickEvent
import com.example.xml_ver.util.yearList
import com.example.xml_ver.viewModel.MainViewModel
import com.example.xml_ver.viewModel.board.MeetingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MeetingPostRegisterFragment : Fragment() {
    private var _binding: FragmentMeetingPostRegisterBinding? = null
    private val binding get() = _binding!!
    private val postViewModel: MeetingViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var personAdapter: ArrayAdapter<String>
    private lateinit var categoryAdapter: ArrayAdapter<String>
    private lateinit var yearAdapter: ArrayAdapter<String>
    private lateinit var monthAdapter: ArrayAdapter<String>
    private lateinit var dayAdapter: ArrayAdapter<String>
    private lateinit var hourAdapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMeetingPostRegisterBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupSpinners()
        setupAllSpinner()
        setupEditText()
        setButton()
        tryRegister()

        (activity as MainActivity).hideBottomNavigation()
        (activity as MainActivity).hideFloatingButton()
    }

    private fun setupAllSpinner() {
        setupSpinnerClickEvent(binding.personEnterField) { person ->
            postViewModel.person.value = personList.indexOf(person)
        }
        setupSpinnerClickEvent(binding.categoryEnterField) {
            postViewModel.category.value = it
        }
        setupSpinnerClickEvent(binding.yearEnterField) {
            postViewModel.deadlineYear.value = it
        }
        setupSpinnerClickEvent(binding.monthEnterField) {
            postViewModel.deadlineMonth.value = it
        }
        setupSpinnerClickEvent(binding.dayEnterField) {
            postViewModel.deadlineDay.value = it
        }
        setupSpinnerClickEvent(binding.hourEnterField) {
            postViewModel.deadlineTime.value = it
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            NavHostFragment.findNavController(this).popBackStack()
            (activity as MainActivity).showBottomNavigation()
            (activity as MainActivity).showFloatingButton()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupSpinners() {
        personAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, personList)
        personAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categoryAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categoryList)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        yearAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, yearList)
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        monthAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, monthList)
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dayAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, dayList)
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        hourAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, hourList)
        hourAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.personEnterField.adapter = personAdapter
        binding.categoryEnterField.adapter = categoryAdapter
        binding.yearEnterField.adapter = yearAdapter
        binding.monthEnterField.adapter = monthAdapter
        binding.dayEnterField.adapter = dayAdapter
        binding.hourEnterField.adapter = hourAdapter
    }

    private fun setupEditText() {
        binding.contentEnterField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                postViewModel.content.value = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.titleEnterField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                postViewModel.title.value = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setButton() {
        viewLifecycleOwner.lifecycleScope.launch {
            postViewModel.isValid.collect {
                setupButtonState(it, binding.registerButton, requireContext())
                Log.d("미란, isValid", it.toString())
            }
        }

        binding.registerButton.setOnClickListener {
            tryRegister()
        }
    }

    private fun tryRegister() {
        viewLifecycleOwner.lifecycleScope.launch {
            postViewModel.registerPost()
            postViewModel.postRegisterState.collect { state ->
                when (state) {
                    is Resource.Success<PostResponseModel> -> {

                    }

                    is Resource.Error<PostResponseModel> -> {
                    }

                    else -> {
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
