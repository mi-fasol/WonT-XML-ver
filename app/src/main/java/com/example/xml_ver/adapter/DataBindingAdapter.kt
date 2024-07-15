package com.example.xml_ver.adapter

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.xml_ver.R
import com.example.xml_ver.util.convertDate
import java.text.SimpleDateFormat
import java.util.*

object DateBindingAdapter {

    @JvmStatic
    @BindingAdapter("android:deadline")
    fun setDeadline(view: TextView, deadline: String?) {
        deadline?.let {
            view.text = convertDate(it)
        }
    }

    @JvmStatic
    @BindingAdapter("android:person")
    fun setPerson(view: TextView, person: Int?) {
        person?.let {
            view.text = "${person}명"
        }
    }

    @JvmStatic
    @BindingAdapter("android:attendee")
    fun setAttendee(view: TextView, person: Int?) {
        person?.let {
            view.text = "${person}/6"
        }
    }

    @JvmStatic
    @BindingAdapter("android:imageUrl")
    fun setImage(view: ImageView, imageUrl: String?) {
        Glide.with(view.context)
            .load(imageUrl)
            .placeholder(R.drawable.wont)
            .error(R.drawable.dummy_image)
            .into(view)
    }
}
