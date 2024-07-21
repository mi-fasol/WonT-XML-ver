package com.example.xml_ver.ui.main.board

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.xml_ver.R
import com.example.xml_ver.adapter.HotPlacePostAdapter
import com.example.xml_ver.adapter.PopularHotPlacePostAdapter
import com.example.xml_ver.databinding.FragmentHotPlaceBinding
import com.example.xml_ver.viewModel.MainViewModel
import com.example.xml_ver.viewModel.board.HotPlacePostViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HotPlaceFragment : Fragment() {
    private var _binding: FragmentHotPlaceBinding? = null
    private val binding get() = _binding!!
    private lateinit var hotPlacePostAdapter: HotPlacePostAdapter
    private lateinit var popularHotPlacePostAdapter: PopularHotPlacePostAdapter
    private val hotPlaceViewModel: HotPlacePostViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHotPlaceBinding.inflate(inflater, container, false).apply {
            viewModel = hotPlaceViewModel
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
        setupPopularRecyclerView()
        setupHotPlacePostRecyclerView()
    }

    private fun setupPopularRecyclerView() {
        popularHotPlacePostAdapter = PopularHotPlacePostAdapter(mainViewModel)
        binding.popularHotPlaceView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularHotPlacePostAdapter
        }
    }

    private fun setupHotPlacePostRecyclerView() {
        hotPlacePostAdapter =
            HotPlacePostAdapter(mainViewModel)
        binding.hotPlacePostView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = hotPlacePostAdapter
        }
    }

    private fun setupToolbar(view: View) {
        binding.toolbar.setOnMenuItemClickListener {menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.chat_list_page_navigation -> {
                    Navigation.findNavController(view).navigateUp()
                    true
                }
                else -> false
            }
        }
    }

    private fun getPostData() {
        lifecycleScope.launch {
            getPopularHotPlacePostList()
            getPostList()
        }
    }

    private fun getPostList() {
        viewLifecycleOwner.lifecycleScope.launch {
            hotPlaceViewModel.getHotPlacePost()
            hotPlaceViewModel.hotPlacePostList.collect {
                hotPlacePostAdapter.submitList(it)
            }
        }
    }

    private fun getPopularHotPlacePostList() {
        viewLifecycleOwner.lifecycleScope.launch {
            hotPlaceViewModel.getPopularHotPlace()
            hotPlaceViewModel.popularHotPlace.collect { posts ->
                popularHotPlacePostAdapter.submitList(posts)
            }
        }
    }

//    private fun updateTodayPostViewVisibility(isEmpty: Boolean) {
//        val params = binding.postListView.layoutParams as ConstraintLayout.LayoutParams
//        binding.todayPostView.visibility = if (isEmpty) {
//            params.height = ViewGroup.LayoutParams.MATCH_PARENT
//            View.GONE
//        } else {
//            params.height = (ViewGroup.LayoutParams.MATCH_PARENT * 0.62).toInt()
//            View.VISIBLE
//        }
//        binding.postListView.layoutParams = params
//        binding.postListView.requestLayout()
//    }
}