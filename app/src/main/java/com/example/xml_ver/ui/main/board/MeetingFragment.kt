package com.example.xml_ver.ui.main.board

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.xml_ver.R
import com.example.xml_ver.adapter.PostAdapter
import com.example.xml_ver.adapter.TodayPostAdapter
import com.example.xml_ver.databinding.FragmentMeetingBinding
import com.example.xml_ver.viewModel.MainViewModel
import com.example.xml_ver.viewModel.board.AcceptationViewModel
import com.example.xml_ver.viewModel.board.MeetingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MeetingFragment : Fragment() {
    private var _binding: FragmentMeetingBinding? = null
    private val binding get() = _binding!!
    private val postViewModel: MeetingViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()
    private val acceptationViewModel: AcceptationViewModel by viewModels()
    private lateinit var postAdapter: PostAdapter
    private lateinit var todayPostAdapter: TodayPostAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMeetingBinding.inflate(inflater, container, false).apply {
            meetingViewModel = postViewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerViews()
        setupToolbar(view)
        getPostData()
    }

    private fun setupRecyclerViews() {
        setupTodayRecyclerView()
        setupPostRecyclerView()
    }

    private fun setupTodayRecyclerView() {
        todayPostAdapter = TodayPostAdapter()
        binding.todayPostView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = todayPostAdapter
        }
    }

    private fun setupPostRecyclerView() {
        postAdapter =
            PostAdapter(postViewModel, acceptationViewModel, mainViewModel)
        binding.postListView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = postAdapter
        }
    }

    private fun setupToolbar(view: View) {
        binding.apply {
            binding.toolbar.setOnMenuItemClickListener { menuItem: MenuItem ->
                when (menuItem.itemId) {
                    R.id.chat_list_page_navigation -> {
                        Navigation.findNavController(view).popBackStack()
                        true
                    }

                    else -> false
                }
            }
        }
    }

    private fun getPostData() {
        lifecycleScope.launch {
            getTodayPostList()
            getPostList()
        }
    }

    private fun getPostList() {
        viewLifecycleOwner.lifecycleScope.launch {
            postViewModel.getPost()
            postViewModel.postModelList.collect {
                postAdapter.submitList(it)
            }
        }
    }

    private fun getTodayPostList() {
        viewLifecycleOwner.lifecycleScope.launch {
            postViewModel.getTodayPost()
            postViewModel.todayPostList.collect { posts ->
                todayPostAdapter.updatePosts(posts)
                updateTodayPostViewVisibility(posts.isEmpty())
            }
        }
    }

    private fun updateTodayPostViewVisibility(isEmpty: Boolean) {
        val params = binding.postListView.layoutParams as ConstraintLayout.LayoutParams
        binding.todayPostView.visibility = if (isEmpty) {
            params.height = ViewGroup.LayoutParams.MATCH_PARENT
            View.GONE
        } else {
            params.height = (ViewGroup.LayoutParams.MATCH_PARENT * 0.62).toInt()
            View.VISIBLE
        }
        binding.postListView.layoutParams = params
        binding.postListView.requestLayout()
    }
}