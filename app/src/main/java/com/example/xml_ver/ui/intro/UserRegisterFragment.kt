package com.example.xml_ver.ui.intro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.xml_ver.databinding.FragmentLoadingBinding
import com.example.xml_ver.databinding.FragmentUserRegisterBinding
import com.example.xml_ver.util.userProfileList

class UserRegisterFragment : Fragment() {
    private var _binding : FragmentUserRegisterBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewPager: ViewPager2

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

        viewPager = binding.viewPager
        val adapter = SliderAdapter()
        viewPager.adapter = adapter

        binding.prevButton.setOnClickListener {
            val currentItem = viewPager.currentItem
            if (currentItem > 0) {
                viewPager.currentItem = currentItem - 1
            } else if(currentItem == 0) {
                viewPager.currentItem = userProfileList.size - 1
            }
        }

        binding.nextButton.setOnClickListener {
            val currentItem = viewPager.currentItem
            if (currentItem < userProfileList.size - 1) {
                viewPager.currentItem = currentItem + 1
            } else if(currentItem == userProfileList.size - 1) {
                viewPager.currentItem = 0
            }
        }
    }
}