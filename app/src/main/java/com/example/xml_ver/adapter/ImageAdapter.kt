package com.example.xml_ver.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.xml_ver.R
import com.example.xml_ver.databinding.ItemImageListBinding

class ImageAdapter(
    private val images: List<String>,
    private val onImageClicked: (Int) -> Unit
) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemImageListBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val imageUrl = images[position]
        Glide.with(holder.binding.imageView)
            .load(imageUrl)
            .placeholder(R.drawable.dummy_image)
            .error(R.drawable.error_image)
            .into(holder.binding.imageView)
        holder.binding.imageLabel.visibility = if (position == 0) View.VISIBLE else View.GONE
        holder.itemView.setOnClickListener { onImageClicked(position) }
    }

    override fun getItemCount() = images.size

    inner class ImageViewHolder(val binding: ItemImageListBinding) :
        RecyclerView.ViewHolder(binding.root)
}