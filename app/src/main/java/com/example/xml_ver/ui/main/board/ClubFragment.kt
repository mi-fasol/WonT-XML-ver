package com.example.xml_ver.ui.main.board

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.xml_ver.R
import com.example.xml_ver.adapter.ClubPostAdapter
import com.example.xml_ver.databinding.FragmentClubBinding
import com.example.xml_ver.viewModel.MainViewModel
import com.example.xml_ver.viewModel.board.ClubPostViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ClubFragment : Fragment() {
    private var _binding: FragmentClubBinding? = null
    private val binding get() = _binding!!
    private val clubPostViewModel: ClubPostViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var clubAdapter: ClubPostAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClubBinding.inflate(inflater, container, false).apply {
            viewModel = clubPostViewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar(view)
        setupClubRecyclerView()
        setupSearchBar()
        getClubPostList()
    }

    private fun setupClubRecyclerView() {
        clubAdapter = ClubPostAdapter(mainViewModel)
        binding.clubPostView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = clubAdapter
        }

        clubAdapter.setOnItemClickListener { post ->
            Log.d("미란", "눌렸음")
            val action =
                ClubFragmentDirections.actionClubFragmentToClubDetailFragment(
                    post.pId,
                    post.nickname
                )
            NavHostFragment.findNavController(this).navigate(action)
        }
    }

    private fun setupToolbar(view: View) {
        binding.apply {
            binding.toolbar.setOnMenuItemClickListener { menuItem: MenuItem ->
                when (menuItem.itemId) {
                    R.id.chat_list_page_navigation -> {
                        findNavController().navigate(R.id.chatListFragment)
                        true
                    }

                    else -> false
                }
            }
        }
    }

    private fun setupSearchBar() {
        binding.clubSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                clubAdapter.filter(newText.orEmpty())
                return true
            }
        })
    }

    private fun getClubPostList() {
        viewLifecycleOwner.lifecycleScope.launch {
            clubPostViewModel.getClubPostList()
            clubPostViewModel.clubPostList.collect { posts ->
                clubAdapter.submitList(posts)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}