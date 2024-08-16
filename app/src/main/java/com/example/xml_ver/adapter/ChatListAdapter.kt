package com.example.xml_ver.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.xml_ver.adapter.PostDiffCallback
import com.example.xml_ver.data.retrofit.chat.FireBaseChatModel
import com.example.xml_ver.data.retrofit.post.PostResponseModel
import com.example.xml_ver.databinding.ItemChatListBinding
import com.example.xml_ver.databinding.ItemPostBinding
import com.example.xml_ver.util.SharedPreferenceUtil
import com.example.xml_ver.viewModel.MainViewModel
import com.example.xml_ver.viewModel.board.AcceptationViewModel
import com.example.xml_ver.viewModel.board.MeetingViewModel
import com.example.xml_ver.viewModel.chat.ChatListViewModel
import com.example.xml_ver.viewModel.user.UserViewModel
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

class ChatListAdapter(
    private val chatListViewModel: ChatListViewModel,
    private val mainViewModel: MainViewModel
) : ListAdapter<FireBaseChatModel, ChatListAdapter.PostViewHolder>(ChatListDiffCallback()) {

    private var onItemClickListener: ((FireBaseChatModel) -> Unit)? = null

    fun setOnItemClickListener(listener: (FireBaseChatModel) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemChatListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val chat = getItem(position)
        holder.bind(chat)
    }

    inner class PostViewHolder(private val binding: ItemChatListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(chat: FireBaseChatModel) {
            binding.chatContent = chat.messages[chat.messages.size-1].content
            binding.mainViewModel = mainViewModel

            val receiver = chatListViewModel.chatList.value[chat.id]

            receiver?.let {
                binding.userImage = it.userImage
                binding.nickname = it.nickname
            }

            binding.executePendingBindings()

            binding.root.setOnClickListener {
                onItemClickListener?.invoke(chat)
            }
        }
    }
}
