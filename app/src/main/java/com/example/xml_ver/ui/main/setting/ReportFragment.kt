package com.example.xml_ver.ui.main.setting

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.haemo_kotlin.network.Resource
import com.example.xml_ver.MainActivity
import com.example.xml_ver.data.retrofit.post.PostResponseModel
import com.example.xml_ver.data.retrofit.user.MailState
import com.example.xml_ver.data.retrofit.user.UserResponseModel
import com.example.xml_ver.databinding.FragmentReportBinding
import com.example.xml_ver.ui.main.chat.ChatRoomFragmentArgs
import com.example.xml_ver.util.categoryList
import com.example.xml_ver.util.reportList
import com.example.xml_ver.util.setupButtonState
import com.example.xml_ver.util.setupSpinnerClickEvent
import com.example.xml_ver.viewModel.MainViewModel
import com.example.xml_ver.viewModel.user.ReportViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReportFragment : Fragment() {
    private var _binding: FragmentReportBinding? = null
    private val binding get() = _binding!!
    private val reportViewModel: ReportViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var categoryAdapter: ArrayAdapter<String>
    private val args: ReportFragmentArgs by navArgs()
    private var uId = 0
    private var reportedUser = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            uId = args.uId
            nickname = args.nickname
            reportedUser = args.nickname
        }
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupSpinner()
        setupSpinner()
        setupEditText()
        setButton()

        (activity as MainActivity).hideBottomNavigation()
        (activity as MainActivity).hideFloatingButton()
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
    private fun setupSpinner() {
        setupSpinnerClickEvent(binding.reportCategoryField) { category ->
            reportViewModel.reportReason.value = category
        }

        categoryAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, reportList)
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.reportCategoryField.adapter = categoryAdapter
    }

    private fun setupEditText() {
        binding.contentEnterField.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                reportViewModel.content.value = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setButton() {
        viewLifecycleOwner.lifecycleScope.launch {
            reportViewModel.content.collect {
                setupButtonState(it.isNotBlank(), binding.reportButton, requireContext())
                Log.d("미란, isValid", it.toString())
            }
        }

        binding.reportButton.setOnClickListener {
            sendReport()
        }
    }

    private fun sendReport() {
        viewLifecycleOwner.lifecycleScope.launch {
            if (reportedUser.isNotBlank()) {
                reportViewModel.sendReport(reportedUser, uId)
                reportViewModel.reportState.collect { state ->
                    if (state == MailState.SUCCESS) {
                        findNavController().popBackStack()
                        (activity as MainActivity).showFloatingButton()
                        (activity as MainActivity).showBottomNavigation()
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
