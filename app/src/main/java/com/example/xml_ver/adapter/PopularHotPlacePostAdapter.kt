package com.example.xml_ver.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.xml_ver.data.retrofit.post.HotPlaceResponsePostModel
import com.example.xml_ver.databinding.ItemHotPlacePostBinding
import com.example.xml_ver.databinding.ItemPopularHotPlacePostBinding
import com.example.xml_ver.viewModel.MainViewModel

class PopularHotPlacePostAdapter(
    private val mainViewModel: MainViewModel
) : ListAdapter<HotPlaceResponsePostModel, PopularHotPlacePostAdapter.PostViewHolder>(
    HotPlacePostDiffCallback()
) {

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
            binding.executePendingBindings()
        }
    }
}