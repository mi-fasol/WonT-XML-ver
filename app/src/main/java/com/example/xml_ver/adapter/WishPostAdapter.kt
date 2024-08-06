package com.example.xml_ver.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.xml_ver.R
import com.example.xml_ver.adapter.PostDiffCallback
import com.example.xml_ver.data.retrofit.post.PostResponseModel
import com.example.xml_ver.databinding.ItemPostBinding
import com.example.xml_ver.databinding.ItemWishPostBinding
import com.example.xml_ver.ui.main.setting.wish.WishMeetingFragment
import com.example.xml_ver.viewModel.MainViewModel
import com.example.xml_ver.viewModel.board.AcceptationViewModel
import com.example.xml_ver.viewModel.board.MeetingViewModel
import com.example.xml_ver.viewModel.boardInfo.WishViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext

class WishPostAdapter(
    private val wishViewModel: WishViewModel,
    private val mainViewModel: MainViewModel
) : ListAdapter<PostResponseModel, WishPostAdapter.PostViewHolder>(PostDiffCallback()) {

    private var onItemClickListener: ((PostResponseModel) -> Unit)? = null
    private var isWished = true

    fun setOnItemClickListener(listener: (PostResponseModel) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding =
            ItemWishPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }

    inner class PostViewHolder(private val binding: ItemWishPostBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(post: PostResponseModel) {
            binding.post = post
            binding.mainViewModel = mainViewModel
            binding.executePendingBindings()

            binding.wishButton.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    Log.d("미란", "눌려떠용")
                    wishViewModel.changeWish(post.pId, 1, isWished)
                    isWished = !isWished
                    val color = if (isWished) {
                        ContextCompat.getColor(it.context, R.color.mainColor)
                    } else {
                        ContextCompat.getColor(it.context, R.color.inquiryScreenContentTextColor)
                    }
                    binding.wishButton.setColorFilter(color)
                }
            }

            binding.root.setOnClickListener {
                onItemClickListener?.invoke(post)
            }
        }
    }
}
