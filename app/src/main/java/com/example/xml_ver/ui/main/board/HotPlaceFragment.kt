package com.example.xml_ver.ui.main.board

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.xml_ver.databinding.FragmentHomeBinding
import com.example.xml_ver.databinding.FragmentHotPlacegBinding
import com.example.xml_ver.databinding.FragmentLoadingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HotPlaceFragment : Fragment() {
    private var _binding : FragmentHotPlacegBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHotPlacegBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("미란", "홈 프레그먼트 진입 성공")
    }
}