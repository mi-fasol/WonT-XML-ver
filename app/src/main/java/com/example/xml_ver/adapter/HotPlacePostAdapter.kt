package com.example.xml_ver.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.xml_ver.data.retrofit.post.ClubPostResponseModel
import com.example.xml_ver.data.retrofit.post.HotPlaceResponsePostModel
import com.example.xml_ver.data.retrofit.post.PostResponseModel
import com.example.xml_ver.databinding.ItemClubPostBinding
import com.example.xml_ver.databinding.ItemHotPlacePostBinding
import com.example.xml_ver.viewModel.MainViewModel
import com.example.xml_ver.viewModel.boardInfo.WishViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HotPlacePostAdapter(
    private val mainViewModel: MainViewModel,
    private val wishViewModel: WishViewModel
) : ListAdapter<HotPlaceResponsePostModel, HotPlacePostAdapter.PostViewHolder>(
    HotPlacePostDiffCallback()
) {
    private var onItemClickListener: ((HotPlaceResponsePostModel) -> Unit)? = null

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