package com.example.xml_ver.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.xml_ver.data.retrofit.comment.reply.ReplyResponseModel
import com.example.xml_ver.databinding.ItemReplyBinding

class ReplyAdapter :
    ListAdapter<ReplyResponseModel, ReplyAdapter.ReplyViewHolder>(ReplyDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReplyViewHolder {
        val binding = ItemReplyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReplyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReplyViewHolder, position: Int) {
        val reply = getItem(position)
        holder.bind(reply)
    }

    inner class ReplyViewHolder(private val binding: ItemReplyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(reply: ReplyResponseModel) {
            binding.reply = reply
            binding.executePendingBindings()
        }
    }
}
