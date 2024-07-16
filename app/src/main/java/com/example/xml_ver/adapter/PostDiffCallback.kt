package com.example.xml_ver.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.xml_ver.data.retrofit.post.ClubPostResponseModel
import com.example.xml_ver.data.retrofit.post.HotPlaceResponsePostModel
import com.example.xml_ver.data.retrofit.post.PostResponseModel

class PostDiffCallback : DiffUtil.ItemCallback<PostResponseModel>() {
    override fun areItemsTheSame(oldItem: PostResponseModel, newItem: PostResponseModel): Boolean {
        return oldItem.pId == newItem.pId
    }

    override fun areContentsTheSame(
        oldItem: PostResponseModel,
        newItem: PostResponseModel
    ): Boolean {
        return oldItem == newItem
    }
}

class ClubPostDiffCallback : DiffUtil.ItemCallback<ClubPostResponseModel>() {
    override fun areItemsTheSame(
        oldItem: ClubPostResponseModel,
        newItem: ClubPostResponseModel
    ): Boolean {
        return oldItem.pId == newItem.pId
    }

    override fun areContentsTheSame(
        oldItem: ClubPostResponseModel,
        newItem: ClubPostResponseModel
    ): Boolean {
        return oldItem == newItem
    }
}

class HotPlacePostDiffCallback : DiffUtil.ItemCallback<HotPlaceResponsePostModel>() {
    override fun areItemsTheSame(
        oldItem: HotPlaceResponsePostModel,
        newItem: HotPlaceResponsePostModel
    ): Boolean {
        return oldItem.hpId == newItem.hpId
    }

    override fun areContentsTheSame(
        oldItem: HotPlaceResponsePostModel,
        newItem: HotPlaceResponsePostModel
    ): Boolean {
        return oldItem == newItem
    }
}