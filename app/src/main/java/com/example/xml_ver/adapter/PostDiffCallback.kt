package com.example.xml_ver.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.xml_ver.data.retrofit.post.PostResponseModel

class PostDiffCallback : DiffUtil.ItemCallback<PostResponseModel>() {
    override fun areItemsTheSame(oldItem: PostResponseModel, newItem: PostResponseModel): Boolean {
        return oldItem.pId == newItem.pId
    }

    override fun areContentsTheSame(oldItem: PostResponseModel, newItem: PostResponseModel): Boolean {
        return oldItem == newItem
    }
}