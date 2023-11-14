package kr.carepet.app.navi

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kr.carepet.singleton.MySharedPreference
import kr.carepet.util.Log

class MyFirebaseMessagingService : FirebaseMessagingService() {

    // [START receive_message]
    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        Log.d(TAG, "From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            // Check if data needs to be processed by long running job
            handleNow(remoteMessage)
        }

        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            it.body?.let { sendNotification(remoteMessage) }
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    // [END receive_message]


    private fun needsToBeScheduled() = true

    // [START on_new_token]
    /**
     * Called if the FCM registration token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the
     * FCM registration token is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")

        sendRegistrationToServer(token)
    }
    private fun handleNow(remoteMessage: RemoteMessage) {
        val page = remoteMessage.data["page"]
        val schUnqNo = remoteMessage.data["schUnqNo"]
        MySharedPreference.setFcmDataPage(page?:"")
        MySharedPreference.setFcmDataSchUnqNo(schUnqNo?:"")
        Log.d(TAG, "handleNow")
    }

    private fun sendRegistrationToServer(token: String?) {
        // TODO: Implement this method to send token to your app server.
        Log.d(TAG, "sendRegistrationTokenToServer($token)")
    }

    @SuppressLint("ServiceCast")
    private fun sendNotification(remoteMessage: RemoteMessage) {

        val deepLinkIntent = Intent(this, MainActivity::class.java).apply {
            action = Intent.ACTION_VIEW
            data = Uri.parse("http://pettip.kr/${remoteMessage.data["schUnqNo"]}")
        }

        // Create a TaskStackBuilder to handle the back stack
        val stackBuilder = TaskStackBuilder.create(this)

        // Add the deep link intent to the stack
        stackBuilder.addNextIntentWithParentStack(deepLinkIntent)

        // Get a PendingIntent containing the entire back stack
        val pendingIntent = stackBuilder.getPendingIntent(
            0,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val channelId = "fcm_default_channel"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.icon_sole)
            .setContentTitle(remoteMessage.notification?.title ?: "Pet Tip")
            .setContentText(remoteMessage.notification?.body ?: "새로운 알림이 왔어요!")
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent) // Set the PendingIntent

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        //// Since android Oreo notification channel is needed.
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        //    val channel = NotificationChannel(
        //        channelId,
        //        "Channel human readable title",
        //        NotificationManager.IMPORTANCE_DEFAULT,
        //    )
        //    notificationManager.createNotificationChannel(channel)
        //}

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel.
            val name = getString(R.string.default_notification_channel_id)
            val descriptionText = "default channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel(channelId, name, importance)
            mChannel.description = descriptionText
            // Register the channel with the system. You can't change the importance
            // or other notification behaviors after this.
            notificationManager.createNotificationChannel(mChannel)
        }

        val notificationId = 0
        notificationManager.notify(notificationId, notificationBuilder.build())
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }

}