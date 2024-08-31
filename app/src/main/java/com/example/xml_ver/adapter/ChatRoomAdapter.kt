package com.example.xml_ver.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.xml_ver.data.retrofit.chat.ChatMessageModel
import com.example.xml_ver.databinding.ItemMessageFromMeBinding
import com.example.xml_ver.databinding.ItemMessageFromOtherBinding
import com.example.xml_ver.util.SharedPreferenceUtil
import com.example.xml_ver.viewModel.chat.ChatViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatRoomAdapter(
    private val context: Context,
    private val userImage: Int,
    private val chatViewModel: ChatViewModel
) : ListAdapter<ChatMessageModel, RecyclerView.ViewHolder>(ChatRoomDiffCallback()) {

    companion object {
        const val VIEW_TYPE_MESSAGE_SENT = 1
        const val VIEW_TYPE_MESSAGE_RECEIVED = 2
    }

    override fun getItemViewType(position: Int): Int {
        val message = getItem(position)
        return if (message.from == SharedPreferenceUtil(context).getUser().uId) {
            VIEW_TYPE_MESSAGE_SENT
        } else {
            VIEW_TYPE_MESSAGE_RECEIVED
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            val binding =
                ItemMessageFromMeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            SentMessageViewHolder(binding)
        } else {
            val binding = ItemMessageFromOtherBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            ReceivedMessageViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = getItem(position)
        when (holder.itemViewType) {
            VIEW_TYPE_MESSAGE_SENT -> (holder as SentMessageViewHolder).bind(message)
            VIEW_TYPE_MESSAGE_RECEIVED -> (holder as ReceivedMessageViewHolder).bind(message)
        }
    }

    inner class SentMessageViewHolder(private val binding: ItemMessageFromMeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(message: ChatMessageModel) {
            binding.chatContent = message.content
            binding.chatTime.text = formatDateTime(message.createdAt)
            binding.executePendingBindings()
        }
    }

    override fun submitList(list: List<ChatMessageModel>?) {
        super.submitList(list)
    }

    inner class ReceivedMessageViewHolder(private val binding: ItemMessageFromOtherBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(message: ChatMessageModel) {
            binding.chatContent = message.content
            chatViewModel.formatDateTime(message.createdAt)
            binding.chatTime.text = formatDateTime(message.createdAt)
            binding.userImage = userImage

            binding.executePendingBindings()
        }
    }

    private fun formatDateTime(timestamp: Long): String {
        val sdf = SimpleDateFormat("HH시 mm분", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}