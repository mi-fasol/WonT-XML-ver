package com.example.xml_ver.adapter

import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.xml_ver.R
import com.example.xml_ver.util.convertDate
import com.example.xml_ver.util.userMyPageImageList
import com.tbuonomo.viewpagerdotsindicator.setBackgroundCompat
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
    @BindingAdapter("android:imageList")
    fun setImageListText(view: TextView, cnt: Int?) {
        cnt?.let {
            view.text = "${cnt}/4"
        }
    }

    @JvmStatic
    @BindingAdapter("android:wishColor")
    fun setWishButtonColor(view: ImageButton, wish: Boolean?) {
        wish?.let {
            val color = if (it) {
                ContextCompat.getColor(
                    view.context,
                    R.color.mainColor
                )
            } else {
                ContextCompat.getColor(view.context, android.R.color.white)
            }
            view.setColorFilter(color)
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

    @JvmStatic
    @BindingAdapter("android:hotBackground")
    fun setHotPlaceCardBackground(view: ImageView, imageUrl: String?) {
        Glide.with(view.context)
            .load(imageUrl)
            .placeholder(R.drawable.wont_icon)
            .error(R.drawable.dummy_image)
            .into(view)
    }

    @JvmStatic
    @BindingAdapter("android:profile_major")
    fun setProfileMajor(view: TextView, major: String?) {
        major?.let {
            view.text = "${major} / "
        }
    }

    @JvmStatic
    @BindingAdapter("android:profile_deadline")
    fun setProfileDeadline(view: TextView, deadline: String?) {
        deadline?.let {
            view.text = " / $deadline"
        }
    }

    @JvmStatic
    @BindingAdapter("android:userImage")
    fun setUserImage(imageView: ImageView, userImageId: Int?) {
        if (userImageId != null) {
            imageView.setImageResource(userMyPageImageList[userImageId])
        } else {
            imageView.setImageResource(R.drawable.round_dog)
        }
    }
}
