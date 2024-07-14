package com.example.xml_ver.ui.main.board

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.xml_ver.R
import com.example.xml_ver.data.retrofit.post.PostResponseModel
import com.example.xml_ver.databinding.FragmentMeetingBinding
import com.example.xml_ver.ui.adapter.PostAdapter
import com.example.xml_ver.ui.adapter.TodayPostAdapter
import com.example.xml_ver.viewModel.MainViewModel
import com.example.xml_ver.viewModel.board.AcceptationViewModel
import com.example.xml_ver.viewModel.board.MeetingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.StateFlow
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
    private lateinit var navController: NavController

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
        navController = findNavController()

        todayPostAdapter = TodayPostAdapter(navController)

        binding.todayPostView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = todayPostAdapter
        }

        postAdapter =
            PostAdapter(postViewModel, acceptationViewModel, mainViewModel, navController)
        binding.postListView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = postAdapter
        }

        setupToolbar(view)

        viewLifecycleOwner.lifecycleScope.launch {
            postViewModel.getPost()
            postViewModel.postModelList.collect {
                postAdapter.submitList(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            postViewModel.getTodayPost()
            postViewModel.getPost()
            postViewModel.postModelList.collect { posts ->
                todayPostAdapter.updatePosts(posts)
                val params = binding.postListView.layoutParams as ConstraintLayout.LayoutParams
                if (posts.isEmpty()) {
                    binding.todayPostView.visibility = View.GONE
                    params.height = ViewGroup.LayoutParams.MATCH_PARENT
                } else {
                    binding.todayPostView.visibility = View.VISIBLE
                    params.height = (ViewGroup.LayoutParams.MATCH_PARENT * 0.62).toInt()
                }
                binding.postListView.layoutParams = params
                binding.postListView.requestLayout()
            }
        }
    }

    private fun setupToolbar(view: View) {
        val toolbar = view.findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }
}