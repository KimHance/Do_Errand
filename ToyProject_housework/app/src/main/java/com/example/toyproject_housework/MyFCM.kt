package com.example.toyproject_housework

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.Person
import androidx.core.graphics.drawable.IconCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

private const val CHANNEL_ID = "my_channel"

class MyFCM : FirebaseMessagingService() {
    private val TAG = "FirebaseTest"
    private var auth : FirebaseAuth? = null

    var db = FirebaseFirestore.getInstance()
    var rdb = Firebase.database.reference

    // 메세지가 수신되면 호출
    @RequiresApi(Build.VERSION_CODES.P)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if(remoteMessage.notification != null){
            sendNotification(remoteMessage.notification?.title,
                remoteMessage.notification?.body!!)
        }
        else if(remoteMessage.data.isNotEmpty()){
            val title = remoteMessage.data["title"]!!
            val message = remoteMessage.data["message"]!!

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
                sendMessageNotification(title,message)
            }else{
                sendMessageNotification(remoteMessage.notification?.title!!,
                remoteMessage.notification?.body!!)
            }
        }
    }

    // Firebase Cloud Messaging Server 가 대기중인 메세지를 삭제 시 호출
    override fun onDeletedMessages() {
        super.onDeletedMessages()
    }

    // 메세지가 서버로 전송 성공 했을때 호출
    override fun onMessageSent(p0: String) {
        super.onMessageSent(p0)
    }

    // 메세지가 서버로 전송 실패 했을때 호출
    override fun onSendError(p0: String, p1: Exception) {
        super.onSendError(p0, p1)
    }

    // 새로운 토큰이 생성 될 때 호출
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        sendRegistrationToServer(token)
    }

    private fun sendNotification(title: String?, body: String){
//        val intent = Intent(this,MainActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // 액티비티 중복 생성 방지
//        val pendingIntent = PendingIntent.getActivity(this, 0 , intent,
//            PendingIntent.FLAG_ONE_SHOT) // 일회성

        val channelId = "channel" // 채널 아이디
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION) // 소리
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title) // 제목
            .setContentText(body) // 내용
            .setSmallIcon(R.drawable.logo_green) // 아이콘
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
//            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 오레오 버전 예외처리
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 , notificationBuilder.build()) // 알림 생성
    }
    // 다른 기기에서 서버로 보냈을 때
    @RequiresApi(Build.VERSION_CODES.P)
    private fun sendMessageNotification(title: String, body: String){
//        val intent = Intent(this, MainActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // 액티비티 중복 생성 방지
//        val pendingIntent = PendingIntent.getActivity(this, 0 , intent,
//            PendingIntent.FLAG_ONE_SHOT) // 일회성

        val channelId = "channel" // 채널 아이디
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION) // 소리
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(title) // 제목
            .setContentText(body) // 내용
            .setSmallIcon(R.drawable.logo_green) // 아이콘
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
//            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 오레오 버전 예외처리
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                "알림 메세지",
                NotificationManager.IMPORTANCE_LOW) // 소리없앰
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 , notificationBuilder.build()) // 알림 생성
    }

    // 받은 토큰을 방별로 저장
    private fun sendRegistrationToServer(token: String){
        auth = FirebaseAuth.getInstance()
        val user = auth?.currentUser?.uid.toString()
        db.collection("User")
            .document(user)
            .get()
            .addOnSuccessListener {
                val room = it["room"].toString()
                val name = it["name"].toString()
                rdb.child(room).child("userToken").child(name).setValue(token)
            }.addOnFailureListener {  }

    }
}