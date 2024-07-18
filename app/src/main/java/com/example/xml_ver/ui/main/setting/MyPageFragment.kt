package com.example.xml_ver.ui.main.setting

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.xml_ver.adapter.MyPageListAdapter
import com.example.xml_ver.databinding.FragmentHomeBinding
import com.example.xml_ver.databinding.FragmentLoadingBinding
import com.example.xml_ver.databinding.FragmentMyPageBinding
import com.example.xml_ver.util.myPageList
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageFragment : Fragment() {
    private var _binding : FragmentMyPageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.myPageListView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = MyPageListAdapter(myPageList)
        }
    }

}