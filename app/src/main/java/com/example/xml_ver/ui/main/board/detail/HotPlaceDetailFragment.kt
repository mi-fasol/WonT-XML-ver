package com.example.xml_ver.ui.main.board.detail

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.xml_ver.MainActivity
import com.example.xml_ver.R
import com.example.xml_ver.adapter.CommentAdapter
import com.example.xml_ver.adapter.HotPlaceImageAdapter
import com.example.xml_ver.databinding.FragmentHotPlaceDetailBinding
import com.example.xml_ver.util.SharedPreferenceUtil
import com.example.xml_ver.viewModel.MainViewModel
import com.example.xml_ver.viewModel.board.HotPlacePostViewModel
import com.example.xml_ver.viewModel.boardInfo.CommentViewModel
import com.example.xml_ver.viewModel.boardInfo.WishViewModel
import com.example.xml_ver.viewModel.user.UserViewModel
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HotPlaceDetailFragment : Fragment() {
    private var _binding: FragmentHotPlaceDetailBinding? = null
    private val binding get() = _binding!!
    private val postViewModel: HotPlacePostViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()
    private val commentViewModel: CommentViewModel by viewModels()
    private val wishViewModel: WishViewModel by viewModels()
    private lateinit var viewPager: ViewPager2
    private lateinit var commentAdapter: CommentAdapter
    private lateinit var hotPlaceImageAdapter: HotPlaceImageAdapter
    private lateinit var dotsIndicator: SpringDotsIndicator
    private val args: HotPlaceDetailFragmentArgs by navArgs()
    private var pId = 0
    private var nickname = ""
    private var wished = false
    private var isMyPost = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHotPlaceDetailBinding.inflate(inflater, container, false).apply {
            hotPlaceViewModel = postViewModel
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
        setupViewPager()
        getPostWriterInfo()
        setupCommentRecyclerView()
        getCommentUserList()
        getCommentList()
        setupButton()

        (activity as MainActivity).hideBottomNavigation()
        (activity as MainActivity).hideFloatingButton()
    }

    private fun setupViewPager() {
        viewPager = binding.hotPlaceImageList
        dotsIndicator = binding.dotsIndicator
    }


    private fun getPostInfo() {
        viewLifecycleOwner.lifecycleScope.launch {
            postViewModel.getOneHotPlacePost(pId)
            postViewModel.hotPlaceModel.collect {
                binding.post = it
                if (it != null) {
                    viewPager = binding.hotPlaceImageList
                    dotsIndicator = binding.dotsIndicator

                    hotPlaceImageAdapter = HotPlaceImageAdapter(it.imageList!!)
                    viewPager.adapter = hotPlaceImageAdapter
                    dotsIndicator.attachTo(viewPager)
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            NavHostFragment.findNavController(this).popBackStack()
            (activity as MainActivity).showBottomNavigation()
            (activity as MainActivity).showFloatingButton()
        }

        binding.toolbar.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.wish_heart_button -> {
                    viewLifecycleOwner.lifecycleScope.launch {
                        wishViewModel.changeWish(pId, 3, wished)
                        wished = !wished
                        updateWishButtonColor(menuItem)
                    }
                    true
                }

                else -> false
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            wishViewModel.checkIsWishedPost(pId, 3)
            wishViewModel.isWished.collect { wish ->
                wished = wish
                val menuItem = binding.toolbar.menu.findItem(R.id.wish_heart_button)
                menuItem?.let {
                    updateWishButtonColor(it)
                }
            }
        }
    }

    private fun updateWishButtonColor(menuItem: MenuItem) {
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
                            3
                        )
                    }
                } else {
                    commentViewModel.registerComment(
                        binding.commentEditText.text.toString(),
                        pId,
                        3
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
            commentViewModel.getCommentListByPId(pId, 3)
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
            commentViewModel.getCommentUser(pId, 3)
            commentViewModel.userList.collect {
                commentAdapter.updateUserList(it)
            }
        }
    }

    private fun getReplyList(cId: Int) {
        viewLifecycleOwner.lifecycleScope.launch {
            commentViewModel.getReplyListByCId(cId, 3)
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
