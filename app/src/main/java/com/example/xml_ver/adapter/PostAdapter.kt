package com.example.xml_ver.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.xml_ver.adapter.PostDiffCallback
import com.example.xml_ver.data.retrofit.post.PostResponseModel
import com.example.xml_ver.databinding.ItemPostBinding
import com.example.xml_ver.viewModel.MainViewModel
import com.example.xml_ver.viewModel.board.AcceptationViewModel
import com.example.xml_ver.viewModel.board.MeetingViewModel

class PostAdapter(
    private val postViewModel: MeetingViewModel,
    private val acceptationViewModel: AcceptationViewModel,
    private val mainViewModel: MainViewModel
) : ListAdapter<PostResponseModel, PostAdapter.PostViewHolder>(PostDiffCallback()) {

    private var onItemClickListener: ((PostResponseModel) -> Unit)? = null

    fun setOnItemClickListener(listener: (PostResponseModel) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }

    inner class PostViewHolder(private val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(post: PostResponseModel) {
            binding.post = post
            binding.mainViewModel = mainViewModel
            binding.executePendingBindings()

            binding.root.setOnClickListener {
                onItemClickListener?.invoke(post)
            }
        }
    }
}
