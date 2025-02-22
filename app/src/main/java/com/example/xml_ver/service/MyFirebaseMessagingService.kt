package com.example.xml_ver.service

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.xml_ver.MainActivity
import com.example.xml_ver.R
import com.example.xml_ver.data.retrofit.chat.ChatMessageModel
import com.example.xml_ver.util.SharedPreferenceUtil
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow


@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {

    private lateinit var context: Context
    private val firebaseDB = FirebaseDatabase.getInstance()
    private val chatRef = firebaseDB.getReference("chat")
    private val userRef = firebaseDB.getReference("user")
    private var userChatList = MutableStateFlow<List<String>>(emptyList())
    private val chatListeners = mutableMapOf<String, ChildEventListener>()
    private var notificationPermission = false

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        notificationPermission = SharedPreferenceUtil(context).getBoolean("notification", false)
        val uId = SharedPreferenceUtil(context).getInt("uId", 0)
        newChatRoomCreated(uId)
        Log.d("미란 알림 허용 상태: ", SharedPreferenceUtil(this).getBoolean("notification", false).toString())
        Log.d("미란 파이어베이스 서비스", "onCreate")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.data.isNotEmpty().let {
//            val message = remoteMessage.data["message"]
//            val sender = remoteMessage.data["sender"]
//            if (message != null && sender != null && notificationPermission) {
                val chatMessage = ChatMessageModel(
                    content = "이잉",
                    createdAt = System.currentTimeMillis(),
                    from = 0,
                    senderNickname = "나다",
                    isRead = false
                )
                sendNotification(chatMessage)
        }
    }

    @SuppressLint("MissingPermission")
    fun sendNotification(lastChat: ChatMessageModel) {
        val intent = Intent(context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.chat_icon)
            .setContentTitle(lastChat.senderNickname)
            .setContentText(lastChat.content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        Log.d("미란 알림은요", notification.toString())

        with(NotificationManagerCompat.from(context)) {
            Log.d("미란 알림은요", "우와아")
            notify(System.currentTimeMillis().toInt(), notification)
        }
    }

    private fun newChatRoomCreated(uId: Int) {
        userRef.child(uId.toString()).addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatId = snapshot.value as String
                if (chatId !in userChatList.value) {
                    userChatList.value += chatId
                    addChatListener(chatId, uId)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {
                val chatId = snapshot.value as String
                userChatList.value -= chatId
                removeChatListener(chatId)
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error: ${error.message}")
            }
        })
    }

    private fun addChatListener(chatId: String, uId: Int) {
        val listener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessageModel::class.java)
                if (chatMessage?.from != uId && chatMessage?.createdAt == System.currentTimeMillis() && !chatMessage.isRead && notificationPermission) {
                    sendNotification(chatMessage)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error: ${error.message}")
            }
        }
        chatRef.child(chatId).child("messages").addChildEventListener(listener)
        chatListeners[chatId] = listener
    }

    private fun removeChatListener(chatId: String) {
        chatListeners[chatId]?.let {
            chatRef.child(chatId).child("messages").removeEventListener(it)
        }
        chatListeners.remove(chatId)
    }

    companion object {
        private const val CHANNEL_ID = "chat_notification"
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("미란 파이어베이스 토큰", token)
        sendRegistrationToServer(token)
    }

    private fun sendRegistrationToServer(token: String?) {
        // TODO: Implement this method to send token to your app server.
        Log.d("미란 파이어베이스 토큰", "sendRegistrationTokenToServer($token)")
    }
}