package com.example.xml_ver.ui.main.board

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.xml_ver.R
import com.example.xml_ver.adapter.HotPlacePostAdapter
import com.example.xml_ver.adapter.PopularHotPlacePostAdapter
import com.example.xml_ver.data.retrofit.post.HotPlaceResponsePostModel
import com.example.xml_ver.databinding.FragmentHotPlaceBinding
import com.example.xml_ver.viewModel.MainViewModel
import com.example.xml_ver.viewModel.board.HotPlacePostViewModel
import com.example.xml_ver.viewModel.boardInfo.WishViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HotPlaceFragment : Fragment() {
    private var _binding: FragmentHotPlaceBinding? = null
    private val binding get() = _binding!!
    private lateinit var hotPlacePostAdapter: HotPlacePostAdapter
    private lateinit var popularHotPlacePostAdapter: PopularHotPlacePostAdapter
    private val hotPlaceViewModel: HotPlacePostViewModel by viewModels()
    private val wishViewModel: WishViewModel by viewModels()
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
        getWishPostList()
        setupToolbar(view)
        getPostData()
    }

    private fun setupRecyclerViews(wishList: List<HotPlaceResponsePostModel>) {
        setupPopularRecyclerView(wishList)
        setupHotPlacePostRecyclerView(wishList)
    }

    private fun setupPopularRecyclerView(wishList: List<HotPlaceResponsePostModel>) {
        popularHotPlacePostAdapter = PopularHotPlacePostAdapter(mainViewModel, wishList, wishViewModel)
        binding.popularHotPlaceView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = popularHotPlacePostAdapter
        }

        popularHotPlacePostAdapter.setOnItemClickListener { post ->
            val action =
                HotPlaceFragmentDirections.actionHotPlaceFragmentToHotPlaceDetailFragment(
                    post.hpId,
                    post.nickname
                )
            NavHostFragment.findNavController(this).navigate(action)
        }
    }

    private fun setupHotPlacePostRecyclerView(wishList: List<HotPlaceResponsePostModel>) {
        hotPlacePostAdapter =
            HotPlacePostAdapter(mainViewModel, wishList, wishViewModel)
        binding.hotPlacePostView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = hotPlacePostAdapter
        }

        hotPlacePostAdapter.setOnItemClickListener { post ->
            val action =
                HotPlaceFragmentDirections.actionHotPlaceFragmentToHotPlaceDetailFragment(
                    post.hpId,
                    post.nickname
                )
            NavHostFragment.findNavController(this).navigate(action)
        }
    }

    private fun setupToolbar(view: View) {
        binding.toolbar.setOnMenuItemClickListener { menuItem: MenuItem ->
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

    private fun getWishPostList() {
        viewLifecycleOwner.lifecycleScope.launch {
            wishViewModel.getWishHotPlace()
            wishViewModel.wishHotPlaceList.collect {
                if (it.isNotEmpty()) {
                    setupRecyclerViews(it)
                } else {
                    setupRecyclerViews(emptyList())
                }
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
}