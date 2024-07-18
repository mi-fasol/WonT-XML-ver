package com.example.xml_ver.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.xml_ver.databinding.ItemMyPageListBinding

class MyPageListAdapter(private val itemList: List<String>) :
    RecyclerView.Adapter<MyPageListAdapter.MyPageListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPageListViewHolder {
        val binding =
            ItemMyPageListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyPageListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyPageListViewHolder, position: Int) {
        val itemText = itemList[position]
        holder.bind(itemText)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class MyPageListViewHolder(private val binding: ItemMyPageListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(itemText: String) {
            binding.itemText = itemText
            binding.executePendingBindings()
        }
    }
}