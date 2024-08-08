package com.example.xml_ver.ui.main.board.detail

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
class MeetingDetailFragment : Fragment() {
    private var _binding: FragmentMeetingDetailBinding? = null
    private val binding get() = _binding!!
    private val postViewModel: MeetingViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()
    private val commentViewModel: CommentViewModel by viewModels()
    private val acceptationViewModel: AcceptationViewModel by viewModels()
    private val wishViewModel: WishViewModel by viewModels()
    private lateinit var commentAdapter: CommentAdapter
    private val args: MeetingDetailFragmentArgs by navArgs()
    private var pId = 0
    private var nickname = ""
    private var wished = false
    private var isMyPost = false

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
        isMyPost = nickname == SharedPreferenceUtil(requireContext()).getString("nickname", "")

        setupToolbar()
        getPostInfo()
        getPostWriterInfo()
        setupEditText()
        setupButton()
        setupCommentRecyclerView()
        getCommentUserList()
        getCommentList()

        (activity as MainActivity).hideBottomNavigation()
        (activity as MainActivity).hideFloatingButton()
    }

    private fun getPostInfo() {
        viewLifecycleOwner.lifecycleScope.launch {
            postViewModel.getOnePost(pId)
            postViewModel.postModel.collect {
                binding.post = it
                Log.d("미란 사람수: ", binding.post?.person.toString())
                if (it != null) {
                    getAttendeeInfo()
                }
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

    private fun setupButton() {
        binding.apply {
            commentSendButton.setOnClickListener {
                sendComment()
            }
        }
    }

    private fun sendComment() {
        viewLifecycleOwner.lifecycleScope.launch {
            commentViewModel.isReply.collect {
                if (it) {
                    commentViewModel.commentId.collect { cId ->
                        commentViewModel.registerReply(
                            binding.commentEditText.text.toString(),
                            cId,
                            1
                        )
                    }
                } else {
                    commentViewModel.registerComment(
                        binding.commentEditText.text.toString(),
                        pId,
                        1
                    )
                }
                binding.commentEditText.text.clear()
                commentViewModel.isReply.value = false
                commentViewModel.commentId.value = 0
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            NavHostFragment.findNavController(this).popBackStack()
            (activity as MainActivity).showBottomNavigation()
            (activity as MainActivity).showFloatingButton()
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
            ContextCompat.getColor(requireContext(), R.color.inquiryScreenContentTextColor)
        }
        val icon = menuItem.icon
        icon?.let {
            DrawableCompat.setTint(it, color)
        }
    }

    private fun getAttendeeInfo() {
        viewLifecycleOwner.lifecycleScope.launch {
            acceptationViewModel.getAcceptationByPId(pId)
            acceptationViewModel.getAttendeeByPId(pId)
            acceptationViewModel.attendeeList.collect {
                acceptationViewModel.nowAttendees.collect {
                    updateAttendeeInfo(it)
                    updateAttendButton(isMyPost, it)
                }
            }
        }
    }

    private fun setupEditText() {
        binding.commentEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                commentViewModel.content.value = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun updateAttendeeInfo(size: Int) {
        binding.attendInfo.text = "$size/${binding.post!!.person}"
    }

    private fun setupCommentRecyclerView() {
        commentAdapter = CommentAdapter(mainViewModel, commentViewModel)

        binding.commentRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = commentAdapter
        }
    }

    private fun getCommentList() {
        viewLifecycleOwner.lifecycleScope.launch {
            commentViewModel.getCommentListByPId(pId, 1)
            commentViewModel.commentList.collect { comments ->
                updateCommentSize(comments.size)
                commentAdapter.submitList(comments)
                comments.forEach { comment ->
                    getReplyList(comment.cId)
                }
            }
        }
    }

    private fun updateCommentSize(size: Int) {
        binding.commentSize.text = size.toString()
    }

    private fun getCommentUserList() {
        viewLifecycleOwner.lifecycleScope.launch {
            commentViewModel.getCommentUser(pId, 1)
            commentViewModel.userList.collect {
                commentAdapter.updateUserList(it)
            }
        }
    }

    private fun getReplyList(cId: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            commentViewModel.getReplyListByCId(cId, 1)
            commentViewModel.replyList.collect { replies ->
                commentAdapter.updateReplies(cId, replies[cId] ?: emptyList())
            }
        }
    }

    private fun updateAttendButton(state: Boolean, person: Int) {
        val newWidth = if (state) {
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                95f,
                Resources.getSystem().displayMetrics
            ).toInt()
        } else {
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                85f,
                Resources.getSystem().displayMetrics
            ).toInt()
        }

        val params = binding.attendButton.layoutParams as ConstraintLayout.LayoutParams
        params.width = newWidth
        binding.attendButton.layoutParams = params

        if (state) {
            binding.attendButton.text = "참여인원 $person"
            binding.attendButton.requestLayout()
            binding.attendButton.setOnClickListener {
                Log.d("미란", "헤헤 눌렀당")
            }
        } else {
            viewLifecycleOwner.lifecycleScope.launch {
                binding.attendButton.setOnClickListener {
                    acceptationViewModel.changeAcceptationRequest(pId)
                }

                acceptationViewModel.myAcceptState.collect {
                    when (it) {
                        AcceptState.JOIN -> {
                            binding.attendButton.text = "참여완료"
                            binding.attendButton.setBackgroundColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.white
                                )
                            )
                            binding.attendButton.setTextColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.mainColor
                                )
                            )
                        }

                        AcceptState.REQUEST -> {
                            binding.attendButton.text = "참여요청"
                            binding.attendButton.setBackgroundColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.mainColor
                                )
                            )
                            binding.attendButton.setTextColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.white
                                )
                            )
                        }

                        else -> {
                            binding.attendButton.text = "참여하기"
                            binding.attendButton.setBackgroundColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.mainColor
                                )
                            )
                            binding.attendButton.setTextColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.white
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
