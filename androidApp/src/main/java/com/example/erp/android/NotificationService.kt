package com.example.erp.android

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.lms.android.Services.Methods
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessageReceiver : FirebaseMessagingService() {


    // Override onNewToken to get new token
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Methods().save_DToken(token,this.baseContext)
        Log.d("device Token", token)
    }


    fun subscribeToTopic(topic: String) {

        FirebaseMessaging.getInstance().subscribeToTopic(topic)
            .addOnCompleteListener { task ->
                var msg = "Subscribed to $topic"
                if (!task.isSuccessful) {
                    msg = "Subscription failed"
                }
                Log.d("FCM", msg)
            }
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            Log.d("FCM token",task.result)
        })
    }

    // Override onMessageReceived() method to extract the
    // title and
    // body from the message passed in FCM
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // First case when notifications are received via
        // data event
        // Here, 'title' and 'message' are the assumed names
        // of JSON
        // attributes. Since here we do not have any data
        // payload, This section is commented out. It is
        // here only for reference purposes.
        /*if(remoteMessage.getData().size()>0){
            showNotification(remoteMessage.getData().get("title"),
                          remoteMessage.getData().get("message"));
        }*/

        // Second case when notification payload is
        // received.
        if (remoteMessage.getNotification() != null) {
            // Since the notification is received directly from
            // FCM, the title and the body can be fetched
            // directly as below.
            remoteMessage.getNotification()!!.getBody()?.let {
                remoteMessage.getNotification()!!.getTitle()?.let { it1 ->
                    remoteMessage.getNotification()!!.imageUrl?.let { it2 ->
                        showNotification(
                            it1,
                            it,
                            it2
                        )
                    }
                }
            }
        }
    }
    //to show the notification
    // Unique Notification IDs Working
//    fun showNotification(title: String, message: String) {
//        val intent = Intent(this, MainActivity::class.java).apply {
//            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        }
//
//        val pendingIntent = PendingIntent.getActivity(
//            this,
//            System.currentTimeMillis().toInt(), // Unique request code
//            intent,
//            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
//        )
//
//        val builder = NotificationCompat.Builder(this, "notification_channel")
//            .setSmallIcon(R.drawable.ic_launcher_foreground)
//            .setContentTitle(title)
//            .setContentText(message)
//            .setAutoCancel(true)
//            .setContentIntent(pendingIntent)
//
//
//        val notificationManager =
//            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                "notification_channel",
//                "Channel Name",
//                NotificationManager.IMPORTANCE_HIGH
//            )
//            notificationManager.createNotificationChannel(channel)
//        }
//
//        notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
//    }

    fun showNotification(title: String, message: String, imageUri: Uri) {
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }

        // Create a unique PendingIntent for each notification
        val pendingIntent = PendingIntent.getActivity(
            this,
            System.currentTimeMillis().toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Load the image using Glide with a Uri
        Glide.with(this)
            .asBitmap()
            .load(imageUri) // Use Uri instead of String
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    // Build the notification with BigPictureStyle
                    val builder = NotificationCompat.Builder(this@MyFirebaseMessageReceiver, "notification_channel")
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setLargeIcon(resource)
                        .setContentIntent(pendingIntent)
                        .setStyle(
                            NotificationCompat.BigPictureStyle()
                            .bigPicture(resource) // Set the loaded bitmap as the big picture
//                            .bigLargeIcon(null)) // Remove large icon when expanded
                        )
                    // Get the NotificationManager
                    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                    // Create notification channel if necessary
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val channelName = "Web App Notifications"
                        val channelDescription = "Notifications for web app updates"

                        val notificationChannel = NotificationChannel(
                            "notification_channel",
                            channelName,
                            NotificationManager.IMPORTANCE_HIGH
                        ).apply {
                            description = channelDescription
                            enableVibration(true)
                            vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
                        }

                        notificationManager.createNotificationChannel(notificationChannel)
                    }

                    // Show the notification
                    notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // Handle cleanup if needed
                }
            })
    }}

class App: Application() {
    val channel_ID ="CHANNEL_1_ID"
    val channel_ID2 ="CHANNEL_2_ID"
    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            // Create the NotificationChannel.
            val name = getString(R.string.notification_channel2)
            val descriptionText = "getString(R.string.channel_description)"
            val importance = NotificationManager.IMPORTANCE_HIGH

            val mChannel = NotificationChannel(channel_ID, name, importance)
            mChannel.description = descriptionText

            val mChannel2 = NotificationChannel(channel_ID2, getString(R.string.notification_channel2),
                NotificationManager.IMPORTANCE_HIGH)
            mChannel2.description = descriptionText

            val notificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(mChannel)
            notificationManager.createNotificationChannel(mChannel2)
        }

    }
}