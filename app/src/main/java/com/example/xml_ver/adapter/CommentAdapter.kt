package com.example.xml_ver.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.xml_ver.data.retrofit.comment.comment.CommentResponseModel
import com.example.xml_ver.data.retrofit.comment.reply.ReplyResponseModel
import com.example.xml_ver.data.retrofit.user.UserResponseModel
import com.example.xml_ver.databinding.ItemCommentBinding
import com.example.xml_ver.ui.component.RoundedBottomSheetDialogFragment
import com.example.xml_ver.ui.main.board.detail.MeetingDetailFragmentDirections
import com.example.xml_ver.viewModel.MainViewModel
import com.example.xml_ver.viewModel.board.AcceptationViewModel
import com.example.xml_ver.viewModel.board.MeetingViewModel
import com.example.xml_ver.viewModel.boardInfo.CommentViewModel
import com.example.xml_ver.viewModel.chat.ChatViewModel

class CommentAdapter(
    private val mainViewModel: MainViewModel,
    private val commentViewModel: CommentViewModel,
    private val chatViewModel: ChatViewModel,
    private val navController: NavController,
    private val fragmentManager: FragmentManager
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

            binding.replyButton.setOnClickListener {
                commentViewModel.commentId.value = comment.cId
                commentViewModel.isReply.value = true
                Log.d("미란 isReply: ", commentViewModel.isReply.value.toString())
            }

            binding.userIcon.setOnClickListener {
                user?.let {
                    val bottomSheet = RoundedBottomSheetDialogFragment(
                        user,
                        chatViewModel,
                        {
                            val action = MeetingDetailFragmentDirections
                                .actionMeetingDetailFragmentToChatRoomFragment(
                                    chatId = chatViewModel.chatId.value,
                                    nickname = it.nickname,
                                    receiverId = it.uId,
                                    userImage = it.userImage
                                )
                            navController.navigate(action)
                        }, {
                            val action = MeetingDetailFragmentDirections
                                .actionMeetingDetailFragmentToReportFragment(
                                    nickname = it.nickname,
                                    uId = it.uId
                                )
                            navController.navigate(action)
                        })
                    bottomSheet.show(fragmentManager, "RoundedBottomSheetDialog")
                }
            }
            replyAdapter.submitList(replies)
            binding.executePendingBindings()
        }
    }
}