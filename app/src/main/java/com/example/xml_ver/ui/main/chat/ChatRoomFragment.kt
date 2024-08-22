package com.example.xml_ver.ui.main.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.xml_ver.MainActivity
import com.example.xml_ver.adapter.ChatRoomAdapter
import com.example.xml_ver.data.retrofit.chat.ChatMessageModel
import com.example.xml_ver.databinding.FragmentChatRoomBinding
import com.example.xml_ver.util.SharedPreferenceUtil
import com.example.xml_ver.viewModel.MainViewModel
import com.example.xml_ver.viewModel.chat.ChatViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChatRoomFragment : Fragment() {
    private var _binding: FragmentChatRoomBinding? = null
    private val binding get() = _binding!!
    private val chatViewModel: ChatViewModel by viewModels()
    private val mainViewModel: MainViewModel by viewModels()
    private val args: ChatRoomFragmentArgs by navArgs()
    private lateinit var chatRoomAdapter: ChatRoomAdapter
    private var userImage = 0
    private var receiverId = 0
    private var receiverNickname = ""
    private var chatId = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatRoomBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            userImage = args.userImage
            receiverId = args.receiverId
            receiverNickname = args.nickname
            chatId = args.chatId
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupChatListRecyclerView()
        setupToolbar(view)
        getChatInfo()
        sendMessage()

        (activity as MainActivity).hideBottomNavigation()
        (activity as MainActivity).hideFloatingButton()
    }

    private fun setupChatListRecyclerView() {
        chatRoomAdapter = ChatRoomAdapter(requireContext(), userImage, chatViewModel)
        binding.chatListRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = chatRoomAdapter
        }
    }

    private fun sendMessage() {
        binding.messageSendButton.setOnClickListener {
            if (binding.messageEditTextView.text.isNotBlank()) {
                chatViewModel.sendMessage(
                    receiverId,
                    ChatMessageModel(
                        binding.messageEditTextView.text.toString(),
                        System.currentTimeMillis(),
                        SharedPreferenceUtil(requireContext()).getUser().uId,
                        SharedPreferenceUtil(requireContext()).getUser().nickname,
                        false
                    )
                )
                binding.messageEditTextView.text.clear()
            }
        }
    }

    private fun setupToolbar(view: View) {
        binding.apply {
            binding.nickname = receiverNickname

            binding.navigationButton.setOnClickListener {
                findNavController().popBackStack()
                (activity as MainActivity).showBottomNavigation()
                (activity as MainActivity).showFloatingButton()
            }
        }
    }


    private fun getChatInfo() {
        viewLifecycleOwner.lifecycleScope.launch {
            chatViewModel.findChatRoom(receiverId)
            chatViewModel.chatId.collect {
                chatViewModel.getChatRoomInfo(chatId, receiverId)
                chatViewModel.chatMessages.collect {
                    chatRoomAdapter.submitList(it)
                    binding.chatListRecyclerView.post {
                        binding.chatListRecyclerView.scrollToPosition(it.size - 1)
                    }
                }
            }
        }
    }
}