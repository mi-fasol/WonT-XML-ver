package com.example.xml_ver.ui.main.board.detail

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
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.haemo_kotlin.viewModel.boardInfo.WishViewModel
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
class MeetingDetailFragment : Fragment() {
    private var _binding: FragmentMeetingBinding? = null
    private val binding get() = _binding!!
    private val postViewModel: MeetingViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()
    private val acceptationViewModel: AcceptationViewModel by viewModels()
    private val wishViewModel: WishViewModel by viewModels()
    private val args: MeetingDetailFragmentArgs by navArgs()
//    private lateinit var commentAdapter: PostAdapter
//    private lateinit var replyAdapter: TodayPostAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMeetingBinding.inflate(inflater, container, false).apply {
            meetingViewModel = postViewModel
            lifecycleOwner = viewLifecycleOwner
        }
        val postId = args.pId
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar(view)
    }

    private fun setupToolbar(view: View) {
        binding.apply {
            binding.toolbar.setOnMenuItemClickListener { menuItem: MenuItem ->
                when (menuItem.itemId) {
                    R.id.wish_start_button -> {
                        true
                    }

                    else -> false
                }
            }
        }
    }
}