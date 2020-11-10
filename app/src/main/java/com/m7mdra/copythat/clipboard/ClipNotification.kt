/*
 * Copyright (c) 2019.
 *             ______               _
 *            |____  |             | |
 *   _ __ ___     / /_ __ ___    __| | _ __  __ _
 *  | '_ ` _ \   / /| '_ ` _ \  / _` || '__|/ _` |
 *  | | | | | | / / | | | | | || (_| || |  | (_| |
 *  |_| |_| |_|/_/  |_| |_| |_| \__,_||_|   \__,_|
 *
 *
 */

package com.m7mdra.copythat.clipboard

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.m7mdra.copythat.*
import com.m7mdra.copythat.ui.main.MainActivity

class ClipNotification(private val context: Context) {
    var builder: NotificationCompat.Builder

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }
        builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("CopyThat is running")
            .setContentText("CopyThat will keep track of you clipboard actions and save them.")
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(getContentIntent())
            .setSmallIcon(R.drawable.ic_typewriter_with_paper)
            .addAction(NotificationCompat.Action(
                0,
                "Show bubble",
                getShowBubblePendingIntent()
            ))
            .addAction(
                NotificationCompat.Action(
                    R.drawable.ic_stop_black_24dp,
                    "Stop",
                    getStopPendingIntent()
                )
            )
    }

    private fun getShowBubblePendingIntent() = PendingIntent.getActivity(
        context,
        32,
        Intent(context, BubbleActivity::class.java),
        PendingIntent.FLAG_UPDATE_CURRENT
    )


    private fun getContentIntent(): PendingIntent {
        return PendingIntent.getActivity(
            context, 92, Intent(context, MainActivity::class.java)
            , PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun getStopPendingIntent(): PendingIntent {
        val intent = Intent(context, ClipBoardService::class.java)
        intent.action = ACTION_STOP_SERVICE
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            PendingIntent.getForegroundService(
                context, REQUEST_CODE_STOP_SERVICE,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        } else {
            PendingIntent.getService(
                context, REQUEST_CODE_STOP_SERVICE,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val notificationChannel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            "CopyThat service channel",
            NotificationManager.IMPORTANCE_LOW
        )
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
    }
}