package com.example.xml_ver.ui.main.board.register

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.example.xml_ver.MainActivity
import com.example.xml_ver.databinding.FragmentMeetingPostRegisterBinding
import com.example.xml_ver.util.categoryList
import com.example.xml_ver.util.dayList
import com.example.xml_ver.util.hourList
import com.example.xml_ver.util.monthList
import com.example.xml_ver.util.personList
import com.example.xml_ver.util.yearList
import com.example.xml_ver.viewModel.MainViewModel
import com.example.xml_ver.viewModel.board.MeetingViewModel
import dagger.hilt.android.AndroidEntryPoint

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

        (activity as MainActivity).hideBottomNavigation()
        (activity as MainActivity).hideFloatingButton()
    }


//    private fun setupButton() {
//        binding.apply {
//            commentSendButton.setOnClickListener {
//                sendComment()
//            }
//        }
//    }

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
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
