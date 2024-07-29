package com.example.xml_ver.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.xml_ver.data.retrofit.comment.comment.CommentResponseModel
import com.example.xml_ver.data.retrofit.comment.reply.ReplyResponseModel
import com.example.xml_ver.data.retrofit.user.UserResponseModel
import com.example.xml_ver.databinding.ItemCommentBinding
import com.example.xml_ver.viewModel.MainViewModel
import com.example.xml_ver.viewModel.board.AcceptationViewModel
import com.example.xml_ver.viewModel.board.MeetingViewModel

class CommentAdapter(
    private val mainViewModel: MainViewModel,
) : ListAdapter<CommentResponseModel, CommentAdapter.CommentViewHolder>(CommentDiffCallback()) {

    private var onReplyClickListener: ((CommentResponseModel) -> Unit)? = null
    private var userMap: Map<String, UserResponseModel> = emptyMap()
    private var replyMap: MutableMap<Int, List<ReplyResponseModel>> =
        mutableMapOf()

    fun setOnReplyClickListener(listener: (CommentResponseModel) -> Unit) {
        onReplyClickListener = listener
    }

    fun updateUserList(users: List<UserResponseModel>) {
        userMap = users.associateBy { it.nickname }
        notifyDataSetChanged()
    }

    fun updateReplies(commentId: Int, replies: List<ReplyResponseModel>) {
        if (replies.isNotEmpty()) {
            replyMap[commentId] = replies
            notifyItemChanged(currentList.indexOfFirst { it.cId == commentId })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val binding = ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CommentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val comment = getItem(position)
        val user = userMap[comment.nickname]
        holder.bind(comment, user, replyMap[comment.cId] ?: emptyList())
    }

    inner class CommentViewHolder(private val binding: ItemCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val replyAdapter = ReplyAdapter()

        init {
            binding.replyRecyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = replyAdapter
            }
            binding.replyButton.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onReplyClickListener?.invoke(getItem(position))
                }
            }
        }

        fun bind(
            comment: CommentResponseModel,
            user: UserResponseModel?,
            replies: List<ReplyResponseModel>
        ) {
            binding.comment = comment
            binding.user = user
            binding.mainViewModel = mainViewModel
            replyAdapter.submitList(replies)
            binding.executePendingBindings()
        }
    }
}