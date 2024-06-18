package net.pettip.gps._app

import android.app.Activity
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle


/**
 * @Project     : PetTip-Android
 * @FileName    : NotificationActivity
 * @Date        : 2024-04-01
 * @author      : CareBiz
 * @description : net.pettip.gps._app
 * @see net.pettip.gps._app.NotificationActivity
 */
class NotificationActivity : Activity() {
 val NOTIFICATION_ID = "NOTIFICATION_ID"

 override fun onCreate(savedInstanceState: Bundle?) {
  super.onCreate(savedInstanceState)
  val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
  manager.cancel(intent.getIntExtra(NOTIFICATION_ID, -1))
  finish() // since finish() is called in onCreate(), onDestroy() will be called immediately
 }

 fun getDismissIntent(notificationId: Int, context: Context?): PendingIntent? {
  val intent = Intent(context, NotificationActivity::class.java)
  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
  intent.putExtra(NOTIFICATION_ID, notificationId)
  return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE)
 }
}