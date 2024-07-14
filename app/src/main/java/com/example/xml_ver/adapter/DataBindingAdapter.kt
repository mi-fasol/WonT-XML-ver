package com.example.xml_ver.adapter

import android.widget.TextView
import androidx.databinding.BindingAdapter
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
}
