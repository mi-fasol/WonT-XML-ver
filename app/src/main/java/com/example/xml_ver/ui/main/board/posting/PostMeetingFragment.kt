package com.example.xml_ver.ui.main.board.posting

import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.xml_ver.MainActivity
import com.example.xml_ver.R
import com.example.xml_ver.adapter.CommentAdapter
import com.example.xml_ver.databinding.FragmentMeetingDetailBinding
import com.example.xml_ver.databinding.FragmentPostMeetingBinding
import com.example.xml_ver.util.SharedPreferenceUtil
import com.example.xml_ver.viewModel.MainViewModel
import com.example.xml_ver.viewModel.board.AcceptState
import com.example.xml_ver.viewModel.board.AcceptationViewModel
import com.example.xml_ver.viewModel.board.MeetingViewModel
import com.example.xml_ver.viewModel.boardInfo.CommentViewModel
import com.example.xml_ver.viewModel.boardInfo.WishViewModel
import com.example.xml_ver.viewModel.user.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostMeetingFragment : Fragment() {
    private var _binding: FragmentPostMeetingBinding? = null
    private val binding get() = _binding!!
    private val postViewModel: MeetingViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostMeetingBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
//        getPostInfo()
//        getPostWriterInfo()
//        setupEditText()
//        setupButton()
//        setupCommentRecyclerView()
//        getCommentUserList()
//        getCommentList()

        (activity as MainActivity).hideBottomNavigation()
        (activity as MainActivity).hideFloatingButton()
    }


//    private fun setupButton() {
//        binding.apply {
//            commentSendButton.setOnClickListener {
//                sendComment()
//            }
//        }
//    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            NavHostFragment.findNavController(this).popBackStack()
            (activity as MainActivity).showBottomNavigation()
            (activity as MainActivity).showFloatingButton()
        }
    }

//
//    private fun setupEditText() {
//        binding.commentEditText.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                commentViewModel.content.value = s.toString()
//            }
//
//            override fun afterTextChanged(s: Editable?) {}
//        })
//    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
