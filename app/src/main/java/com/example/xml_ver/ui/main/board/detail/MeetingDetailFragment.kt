package com.example.xml_ver.ui.main.board.detail

import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.example.haemo_kotlin.viewModel.boardInfo.WishViewModel
import com.example.xml_ver.R
import com.example.xml_ver.databinding.FragmentMeetingDetailBinding
import com.example.xml_ver.viewModel.MainViewModel
import com.example.xml_ver.viewModel.board.AcceptationViewModel
import com.example.xml_ver.viewModel.board.MeetingViewModel
import com.example.xml_ver.viewModel.user.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MeetingDetailFragment : Fragment() {
    private var _binding: FragmentMeetingDetailBinding? = null
    private val binding get() = _binding!!
    private val postViewModel: MeetingViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()
    private val acceptationViewModel: AcceptationViewModel by viewModels()
    private val wishViewModel: WishViewModel by viewModels()
    private val args: MeetingDetailFragmentArgs by navArgs()
    private var pId = 0
    private var nickname = ""
    private var wished = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMeetingDetailBinding.inflate(inflater, container, false).apply {
            meetingViewModel = postViewModel
            lifecycleOwner = viewLifecycleOwner
            pId = args.pId
            nickname = args.nickname
        }
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        getPostInfo()
        getPostWriterInfo()
    }

    private fun getPostInfo() {
        viewLifecycleOwner.lifecycleScope.launch {
            postViewModel.getOnePost(pId)
            postViewModel.postModel.collect {
                binding.post = it
            }
        }
    }

    private fun getPostWriterInfo() {
        viewLifecycleOwner.lifecycleScope.launch {
            userViewModel.getUserByNickname(nickname)
            userViewModel.userByNickname.collect {
                binding.user = it
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            NavHostFragment.findNavController(this).popBackStack()
        }

        binding.toolbar.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.wish_star_button -> {
                    viewLifecycleOwner.lifecycleScope.launch {
                        wishViewModel.changeWish(pId, 1, wished)
                        wished = !wished

                        updateStarButtonColor(menuItem)
                    }
                    true
                }

                else -> false
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            wishViewModel.checkIsWishedPost(pId, 1)
            wishViewModel.isWished.collect { wish ->
                wished = wish
                val menuItem = binding.toolbar.menu.findItem(R.id.wish_star_button)
                menuItem?.let {
                    updateStarButtonColor(it)
                }
            }
        }
    }

    private fun updateStarButtonColor(menuItem: MenuItem) {
        val color = if (wished) {
            ContextCompat.getColor(requireContext(), R.color.mainColor)
        } else {
            ContextCompat.getColor(requireContext(), R.color.mainGreyColor)
        }
        val icon = menuItem.icon
        icon?.let {
            DrawableCompat.setTint(it, color)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}