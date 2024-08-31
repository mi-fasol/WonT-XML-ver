package com.example.xml_ver.adapter

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.xml_ver.R
import com.example.xml_ver.data.retrofit.post.HotPlaceResponsePostModel
import com.example.xml_ver.databinding.ItemPopularHotPlacePostBinding
import com.example.xml_ver.viewModel.MainViewModel
import com.example.xml_ver.viewModel.boardInfo.WishViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PopularHotPlacePostAdapter(
    private val mainViewModel: MainViewModel,
    private val wishViewModel: WishViewModel
) : ListAdapter<HotPlaceResponsePostModel, PopularHotPlacePostAdapter.PostViewHolder>(
    HotPlacePostDiffCallback()
) {
    private var onItemClickListener: ((HotPlaceResponsePostModel) -> Unit)? = null

    fun setOnItemClickListener(listener: (HotPlaceResponsePostModel) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding =
            ItemPopularHotPlacePostBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }

    inner class PostViewHolder(private val binding: ItemPopularHotPlacePostBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(post: HotPlaceResponsePostModel) {
            binding.hotPlacePost = post
            binding.mainViewModel = mainViewModel

            CoroutineScope(Dispatchers.Main).launch {
                wishViewModel.getWishHotPlace()
                wishViewModel.wishHotPlaceList.collect { wishList ->
                    binding.wish = wishList.contains(post)
                    binding.executePendingBindings()
                }
            }

            binding.heartIcon.setOnClickListener {
                wishViewModel.changeWish(post.hpId, 3, binding.wish)
                notifyItemChanged(bindingAdapterPosition)
            }

            binding.root.setOnClickListener {
                onItemClickListener?.invoke(post)
            }
        }
    }
}