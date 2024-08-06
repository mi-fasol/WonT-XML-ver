package com.example.xml_ver.ui.main.setting.wish

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.xml_ver.adapter.WishClubAdapter
import com.example.xml_ver.adapter.WishHotPlaceAdapter
import com.example.xml_ver.databinding.FragmentWishHotPlaceBinding
import com.example.xml_ver.viewModel.MainViewModel
import com.example.xml_ver.viewModel.board.HotPlacePostViewModel
import com.example.xml_ver.viewModel.boardInfo.WishViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WishHotPlaceFragment : Fragment() {
    private var _binding: FragmentWishHotPlaceBinding? = null
    private val binding get() = _binding!!
    private val postViewModel: HotPlacePostViewModel by viewModels()
    private val wishViewModel: WishViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var postAdapter: WishHotPlaceAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWishHotPlaceBinding.inflate(inflater, container, false).apply {
            hotPlaceViewModel = postViewModel
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
            WishHotPlaceAdapter(mainViewModel, wishViewModel)
        binding.wishHotPlaceRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = postAdapter
        }

        postAdapter.setOnItemClickListener { post ->
            val action =
                WishHotPlaceFragmentDirections.actionWishHotPlaceFragmentToHotPlaceDetailFragment(
                    post.hpId,
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
            wishViewModel.getWishHotPlace()
            wishViewModel.wishHotPlaceList.collect {
                Log.d("미란", it.toString())
                postAdapter.submitList(it)
            }
        }
    }
}