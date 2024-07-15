package com.example.xml_ver.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.xml_ver.data.retrofit.post.ClubPostResponseModel
import com.example.xml_ver.databinding.ItemClubPostBinding
import com.example.xml_ver.viewModel.MainViewModel

class ClubPostAdapter(
    private val mainViewModel: MainViewModel
) : ListAdapter<ClubPostResponseModel, ClubPostAdapter.PostViewHolder>(ClubPostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding =
            ItemClubPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }

    inner class PostViewHolder(private val binding: ItemClubPostBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(post: ClubPostResponseModel) {
            binding.clubPost = post
            binding.mainViewModel = mainViewModel
            binding.executePendingBindings()
        }
    }
}