package com.example.xml_ver.data.retrofit.chat

data class MessageModel(
    val content : String,
    val isRead : Boolean,
    val sendTime : Long,
    val sender : Int
)