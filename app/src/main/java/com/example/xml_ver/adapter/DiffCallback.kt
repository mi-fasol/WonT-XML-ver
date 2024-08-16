package com.example.xml_ver.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.xml_ver.data.retrofit.chat.ChatMessageModel
import com.example.xml_ver.data.retrofit.chat.FireBaseChatModel
import com.example.xml_ver.data.retrofit.comment.comment.CommentResponseModel
import com.example.xml_ver.data.retrofit.comment.reply.ReplyResponseModel
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

class CommentDiffCallback : DiffUtil.ItemCallback<CommentResponseModel>() {
    override fun areItemsTheSame(
        oldItem: CommentResponseModel,
        newItem: CommentResponseModel
    ): Boolean {
        return oldItem.cId == newItem.cId
    }

    override fun areContentsTheSame(
        oldItem: CommentResponseModel,
        newItem: CommentResponseModel
    ): Boolean {
        return oldItem == newItem
    }
}

class ReplyDiffCallback : DiffUtil.ItemCallback<ReplyResponseModel>() {
    override fun areItemsTheSame(
        oldItem: ReplyResponseModel,
        newItem: ReplyResponseModel
    ): Boolean {
        return oldItem.rId == newItem.rId
    }

    override fun areContentsTheSame(
        oldItem: ReplyResponseModel,
        newItem: ReplyResponseModel
    ): Boolean {
        return oldItem == newItem
    }
}

class ChatListDiffCallback : DiffUtil.ItemCallback<FireBaseChatModel>() {
    override fun areItemsTheSame(
        oldItem: FireBaseChatModel,
        newItem: FireBaseChatModel
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: FireBaseChatModel,
        newItem: FireBaseChatModel
    ): Boolean {
        return oldItem == newItem
    }
}