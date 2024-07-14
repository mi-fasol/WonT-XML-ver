package com.example.xml_ver.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.xml_ver.data.retrofit.post.PostResponseModel
import com.example.xml_ver.databinding.ItemPostBinding
import com.example.xml_ver.databinding.ItemTodayPostBinding

class TodayPostAdapter(
    private val navController: NavController
) : RecyclerView.Adapter<TodayPostAdapter.PostViewHolder>() {

    private var posts: List<PostResponseModel> = emptyList()

    inner class PostViewHolder(private val binding: ItemTodayPostBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(post: PostResponseModel) {
            binding.post = post
            binding.root.setOnClickListener {

            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = ItemTodayPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(posts[position])
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    fun updatePosts(newPosts: List<PostResponseModel>) {
        posts = newPosts
        notifyDataSetChanged()
    }
}