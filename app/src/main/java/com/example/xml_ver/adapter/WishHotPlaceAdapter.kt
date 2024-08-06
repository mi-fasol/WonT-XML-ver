package com.example.xml_ver.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.xml_ver.R
import com.example.xml_ver.data.retrofit.post.HotPlaceResponsePostModel
import com.example.xml_ver.databinding.ItemHotPlacePostBinding
import com.example.xml_ver.viewModel.MainViewModel
import com.example.xml_ver.viewModel.boardInfo.WishViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

class WishHotPlaceAdapter(
    private val mainViewModel: MainViewModel,
    private val wishViewModel: WishViewModel
) : ListAdapter<HotPlaceResponsePostModel, WishHotPlaceAdapter.PostViewHolder>(
    HotPlacePostDiffCallback()
) {
    private var onItemClickListener: ((HotPlaceResponsePostModel) -> Unit)? = null
    private var isWished = true

    fun setOnItemClickListener(listener: (HotPlaceResponsePostModel) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding =
            ItemHotPlacePostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }

    inner class PostViewHolder(private val binding: ItemHotPlacePostBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(post: HotPlaceResponsePostModel) {
            binding.hotPlacePost = post
            binding.mainViewModel = mainViewModel
            binding.executePendingBindings()

            binding.heartIcon.setColorFilter(ContextCompat.getColor(binding.root.context, R.color.mainColor))

            binding.heartIcon.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    wishViewModel.changeWish(post.hpId, 3, isWished)
                    isWished = !isWished
                    val color = if (isWished) {
                        ContextCompat.getColor(it.context, R.color.mainColor)
                    } else {
                        ContextCompat.getColor(it.context, R.color.inquiryScreenContentTextColor)
                    }
                    binding.heartIcon.setColorFilter(color)
                }
            }

            binding.root.setOnClickListener {
                onItemClickListener?.invoke(post)
            }
        }
    }
}