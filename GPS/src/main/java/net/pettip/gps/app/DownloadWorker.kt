/*
 * Copyright (c) 2023. PetTip All right reserved.
 * This software is the proprietary information of Care Biz.
 *
 * Revision History
 *   Author                         Date          Description
 *   --------------------------     ----------    ----------------------------------------
 *   isyuun                         2023.11.30
 *
 */

package net.pettip.gps.app

import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import net.pettip.gps.R
import net.pettip.gps._app.NOTIFICATION_CHANNEL_ID
import net.pettip.gps._app.NOTIFICATION_ID

/**
 * @Project     : PetTip-Android
 * @FileName    : DownloadWorker
 * @Date        : 2023-11-30
 * @author      : isyuun
 * @description : net.pettip.gps.app
 * @see net.pettip.gps.app.DownloadWorker
 */
class DownloadWorker(context: Context, parameters: WorkerParameters) :
    CoroutineWorker(context, parameters) {

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as
                NotificationManager

    override suspend fun doWork(): Result {
        val inputUrl = inputData.getString(KEY_INPUT_URL)
            ?: return Result.failure()
        val outputFile = inputData.getString(KEY_OUTPUT_FILE_NAME)
            ?: return Result.failure()
        // Mark the Worker as important
        val progress = "Starting Download"
        setForeground(createForegroundInfo(progress))
        download(inputUrl, outputFile)
        return Result.success()
    }

    private fun download(inputUrl: String, outputFile: String) {
        // Downloads a file and updates bytes read
        // Calls setForeground() periodically when it needs to update
        // the ongoing Notification
    }

    // Creates an instance of ForegroundInfo which can be used to update the
    // ongoing notification.
    private fun createForegroundInfo(progress: String): ForegroundInfo {
        //val id = applicationContext.getString(R.string.notification_channel_id)
        //val title = applicationContext.getString(R.string.notification_title)
        //val cancel = applicationContext.getString(R.string.cancel_download)
        val id = NOTIFICATION_CHANNEL_ID
        val title = applicationContext.getString(R.string.app_name)
        val cancel = applicationContext.getString(R.string.cancel)
        // This PendingIntent can be used to cancel the worker
        val intent = WorkManager.getInstance(applicationContext)
            .createCancelPendingIntent(getId())

        // Create a Notification channel if necessary
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }

        val notification = NotificationCompat.Builder(applicationContext, id)
            .setContentTitle(title)
            .setTicker(title)
            .setContentText(progress)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setOngoing(true)
            // Add the cancel action to the notification which can
            // be used to cancel the worker
            .addAction(android.R.drawable.ic_delete, cancel, intent)
            .build()

        return ForegroundInfo(NOTIFICATION_ID, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        // Create a Notification channel
    }

    companion object {
        const val KEY_INPUT_URL = "KEY_INPUT_URL"
        const val KEY_OUTPUT_FILE_NAME = "KEY_OUTPUT_FILE_NAME"
    }
}