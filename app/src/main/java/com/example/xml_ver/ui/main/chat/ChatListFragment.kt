package com.example.xml_ver.ui.main.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.xml_ver.R
import com.example.xml_ver.adapter.ChatListAdapter
import com.example.xml_ver.adapter.PostAdapter
import com.example.xml_ver.adapter.TodayPostAdapter
import com.example.xml_ver.data.retrofit.user.UserResponseModel
import com.example.xml_ver.databinding.FragmentChatListBinding
import com.example.xml_ver.util.SharedPreferenceUtil
import com.example.xml_ver.viewModel.MainViewModel
import com.example.xml_ver.viewModel.board.AcceptationViewModel
import com.example.xml_ver.viewModel.chat.ChatListViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChatListFragment : Fragment() {
    private var _binding: FragmentChatListBinding? = null
    private val binding get() = _binding!!
    private val chatListViewModel: ChatListViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var chatListAdapter: ChatListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatListBinding.inflate(inflater, container, false).apply {
            chatListViewModel = chatListViewModel
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupChatListRecyclerView()
        setupToolbar(view)
        getChatList()
    }

    private fun setupChatListRecyclerView() {
        chatListAdapter = ChatListAdapter(chatListViewModel, mainViewModel)
        binding.chatListRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = chatListAdapter
        }

        chatListAdapter.setOnItemClickListener { chat ->
            viewLifecycleOwner.lifecycleScope.launch {
                val receiver = chat.id?.let {
                    chatListViewModel.chatList.value[chat.id]
                }

                receiver?.let { r ->
                    val action =
                        ChatListFragmentDirections.actionChatListFragmentToChatRoomFragment(
                            chatId = chat.id!!,
                            nickname = r.nickname,
                            receiverId = r.uId,
                            userImage = r.userImage
                        )
                    NavHostFragment.findNavController(this@ChatListFragment).navigate(action)
                }
            }
        }
    }

    private fun setupToolbar(view: View) {
        binding.apply {
            binding.toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }


    private fun getChatList() {
        viewLifecycleOwner.lifecycleScope.launch {
            chatListViewModel.getChatList()
            chatListViewModel.fireBaseChatModel.collect {
                chatListAdapter.submitList(it)
            }
        }
    }
}