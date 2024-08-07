package com.example.xml_ver.ui.main.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.xml_ver.adapter.MyPostAdapter
import com.example.xml_ver.databinding.FragmentWishMeetingBinding
import com.example.xml_ver.util.SharedPreferenceUtil
import com.example.xml_ver.viewModel.MainViewModel
import com.example.xml_ver.viewModel.board.AcceptationViewModel
import com.example.xml_ver.viewModel.board.MeetingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyMeetingFragment : Fragment() {
    private var _binding: FragmentWishMeetingBinding? = null
    private val binding get() = _binding!!
    private val postViewModel: MeetingViewModel by viewModels()
    private val acceptationViewModel: AcceptationViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var postAdapter: MyPostAdapter

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
        setupMyMeetingPostRecyclerView()
        getMyPostList()
    }

    private fun setupMyMeetingPostRecyclerView() {
        postAdapter =
            MyPostAdapter(postViewModel, acceptationViewModel, mainViewModel)
        binding.postListView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = postAdapter
        }

        postAdapter.setOnItemClickListener { post ->
            val action =
                MyMeetingFragmentDirections.actionMyMeetingFragmentToMeetingDetailFragment(
                    post.pId,
                    post.nickname
                )
            NavHostFragment.findNavController(this).navigate(action)
        }
    }

    private fun getMyPostList() {
        viewLifecycleOwner.lifecycleScope.launch {
            postViewModel.getPost()
            postViewModel.postModelList.collect {
                val list = it.filter { post ->
                    post.nickname == SharedPreferenceUtil(requireContext()).getString(
                        "nickname",
                        ""
                    )
                }
                postAdapter.submitList(list)
            }
        }
    }

    private fun setupToolbar(view: View) {
        binding.apply {
            binding.toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }
}