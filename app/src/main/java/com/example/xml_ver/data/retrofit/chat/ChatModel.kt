package com.example.xml_ver.data.retrofit.chat

data class FireBaseChatModel(
    var id: String?,
    var sender: ChatUserModel,
    var receiver: ChatUserModel,
    var messages: List<ChatMessageModel>
)

data class ChatUserModel(
    var id: Int?,
    var nickname: String
)

data class ChatMessageModel(
    var content: String = "",
    var createdAt: Long = 0,
    var from: Int = 0,
    var senderNickname : String = "",
    var isRead: Boolean = false
)