package com.example.xml_ver.ui.main.board.detail

import android.content.res.Resources
import android.os.Build
import android.os.Bundle
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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.xml_ver.MainActivity
import com.example.xml_ver.R
import com.example.xml_ver.adapter.CommentAdapter
import com.example.xml_ver.adapter.ReplyAdapter
import com.example.xml_ver.data.retrofit.user.UserResponseModel
import com.example.xml_ver.databinding.FragmentClubDetailBinding
import com.example.xml_ver.databinding.FragmentMeetingDetailBinding
import com.example.xml_ver.ui.component.RoundedBottomSheetDialogFragment
import com.example.xml_ver.util.SharedPreferenceUtil
import com.example.xml_ver.viewModel.MainViewModel
import com.example.xml_ver.viewModel.board.AcceptState
import com.example.xml_ver.viewModel.board.AcceptationViewModel
import com.example.xml_ver.viewModel.board.ClubPostViewModel
import com.example.xml_ver.viewModel.board.MeetingViewModel
import com.example.xml_ver.viewModel.boardInfo.CommentViewModel
import com.example.xml_ver.viewModel.boardInfo.WishViewModel
import com.example.xml_ver.viewModel.chat.ChatViewModel
import com.example.xml_ver.viewModel.user.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ClubDetailFragment : Fragment() {
    private var _binding: FragmentClubDetailBinding? = null
    private val binding get() = _binding!!
    private val postViewModel: ClubPostViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()
    private val commentViewModel: CommentViewModel by viewModels()
    private val chatViewModel: ChatViewModel by viewModels()
    private val wishViewModel: WishViewModel by viewModels()
    private lateinit var commentAdapter: CommentAdapter
    private val args: ClubDetailFragmentArgs by navArgs()
    private var pId = 0
    private var nickname = ""
    private var wished = false
    private var isMyPost = false
    private var postUser: UserResponseModel? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClubDetailBinding.inflate(inflater, container, false).apply {
            clubViewModel = postViewModel
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
        setupButton()
        setupCommentRecyclerView()
        getCommentUserList()
        getCommentList()

        (activity as MainActivity).hideBottomNavigation()
        (activity as MainActivity).hideFloatingButton()
    }

    private fun getPostInfo() {
        viewLifecycleOwner.lifecycleScope.launch {
            postViewModel.getOneClubPost(pId)
            postViewModel.clubPost.collect {
                binding.post = it
            }
        }
    }

    private fun getPostWriterInfo() {
        viewLifecycleOwner.lifecycleScope.launch {
            userViewModel.getUserByNickname(nickname)
            userViewModel.userByNickname.collect {
                binding.user = it
                postUser = it
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
                        wishViewModel.changeWish(pId, 2, wished)
                        wished = !wished
                        updateStarButtonColor(menuItem)
                    }
                    true
                }

                else -> false
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            wishViewModel.checkIsWishedPost(pId, 2)
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

    private fun setupButton() {
        binding.apply {
            commentSendButton.setOnClickListener {
                sendComment()
            }

            binding.writerImage.setOnClickListener {
                postUser?.let {
                    val bottomSheet = RoundedBottomSheetDialogFragment(
                        it,
                        chatViewModel
                    ){
                        val action = ClubDetailFragmentDirections
                            .actionClubDetailFragmentToChatRoomFragment(
                                chatId = chatViewModel.chatId.value,
                                nickname = it.nickname,
                                receiverId = it.uId,
                                userImage = it.userImage
                            )
                        findNavController().navigate(action)
                    }
                    bottomSheet.show(parentFragmentManager, "RoundedBottomSheetDialog")
                }
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
                            2
                        )
                    }
                } else {
                    commentViewModel.registerComment(
                        binding.commentEditText.text.toString(),
                        pId,
                        2
                    )
                }
                binding.commentEditText.text.clear()
                commentViewModel.isReply.value = false
                commentViewModel.commentId.value = 0
            }
        }
    }


    private fun setupCommentRecyclerView() {
        commentAdapter = CommentAdapter(mainViewModel, commentViewModel)
        binding.commentRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = commentAdapter
        }

        commentAdapter.setOnReplyClickListener {
        }
    }

    private fun getCommentList() {
        viewLifecycleOwner.lifecycleScope.launch {
            commentViewModel.getCommentListByPId(pId, 2)
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
            commentViewModel.getCommentUser(pId, 2)
            commentViewModel.userList.collect {
                commentAdapter.updateUserList(it)
            }
        }
    }

    private fun getReplyList(cId: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            commentViewModel.getReplyListByCId(cId, 2)
            commentViewModel.replyList.collect { replies ->
                commentAdapter.updateReplies(cId, replies[cId] ?: emptyList())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
