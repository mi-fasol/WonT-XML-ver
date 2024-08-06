package com.example.xml_ver.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.example.xml_ver.R
import com.example.xml_ver.data.retrofit.post.PostResponseModel
import com.example.xml_ver.databinding.ItemMyPageListBinding

class MyPageListAdapter(
    private val itemList: List<String>,
    private val navController: NavController
) :
    RecyclerView.Adapter<MyPageListAdapter.MyPageListViewHolder>() {

    private var onItemClickListener: ((PostResponseModel) -> Unit)? = null

    fun setOnItemClickListener(listener: (PostResponseModel) -> Unit) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPageListViewHolder {
        val binding =
            ItemMyPageListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyPageListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyPageListViewHolder, position: Int) {
        val itemText = itemList[position]
        holder.bind(itemText, position)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class MyPageListViewHolder(private val binding: ItemMyPageListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(itemText: String, position: Int) {
            binding.itemText = itemText
            binding.executePendingBindings()

            binding.root.setOnClickListener {
                when (position) {
                    1 -> navController.navigate(R.id.wishMeetingFragment)
                }
            }
        }
    }
}