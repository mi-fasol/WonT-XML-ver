package com.example.xml_ver.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.xml_ver.R

class HotPlaceImageAdapter(private var imageUrls: List<String>) :
    RecyclerView.Adapter<HotPlaceImageAdapter.HotPlaceImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HotPlaceImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.hot_place_image_slide, parent, false)
        return HotPlaceImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: HotPlaceImageViewHolder, position: Int) {
        val imageUrl = imageUrls[position]
        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return imageUrls.size
    }

    inner class HotPlaceImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }
}