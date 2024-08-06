package com.example.xml_ver.ui.main.setting.wish

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.xml_ver.R
import com.example.xml_ver.adapter.PostAdapter
import com.example.xml_ver.adapter.TodayPostAdapter
import com.example.xml_ver.adapter.WishPostAdapter
import com.example.xml_ver.databinding.FragmentMeetingBinding
import com.example.xml_ver.databinding.FragmentWishMeetingBinding
import com.example.xml_ver.viewModel.MainViewModel
import com.example.xml_ver.viewModel.board.AcceptationViewModel
import com.example.xml_ver.viewModel.board.MeetingViewModel
import com.example.xml_ver.viewModel.boardInfo.WishViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WishMeetingFragment : Fragment() {
    private var _binding: FragmentWishMeetingBinding? = null
    private val binding get() = _binding!!
    private val postViewModel: MeetingViewModel by viewModels()
    private val wishViewModel: WishViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var postAdapter: WishPostAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWishMeetingBinding.inflate(inflater, container, false).apply {
            meetingViewModel = postViewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar(view)
        setupWishPostRecyclerView()
        getWishPostData()
    }

    private fun setupWishPostRecyclerView() {
        postAdapter =
            WishPostAdapter(wishViewModel, mainViewModel)
        binding.postListView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = postAdapter
        }

        postAdapter.setOnItemClickListener { post ->
            val action =
                WishMeetingFragmentDirections.actionWishMeetingFragmentToMeetingDetailFragment(
                    post.pId,
                    post.nickname
                )
            NavHostFragment.findNavController(this).navigate(action)
        }
    }

    private fun setupToolbar(view: View) {
        binding.apply {
            binding.toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun getWishPostData() {
        viewLifecycleOwner.lifecycleScope.launch {
            wishViewModel.getWishMeeting()
            wishViewModel.wishMeetingList.collect {
                postAdapter.submitList(it)
            }
        }
    }
}