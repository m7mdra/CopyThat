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

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.m7mdra.copythat.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.android.ext.android.inject


class ClipBoardService : Service() {


    private val clipBoard: ClipBoard by inject()
    private val clipNotification: ClipNotification by inject()

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        clipBoard.start()
        EventBus.getDefault().post(ServiceStarted())
        LocalBroadcastManager.getInstance(this).sendBroadcast(Intent(this::class.java.simpleName))
    }

    override fun onDestroy() {
        clipBoard.stop()
        EventBus.getDefault().post(ServiceStopped())
        LocalBroadcastManager.getInstance(this).sendBroadcast(Intent(this::class.java.simpleName))
        super.onDestroy()


    }


    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return if (intent.action == ACTION_STOP_SERVICE) {
            stopForeground(true)
            stopSelf()
            super.onStartCommand(intent, flags, startId)
        } else {
            startForeground(NOTIFICATION_ID, clipNotification.builder.build())
            START_STICKY
        }
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        Bus.publish(ServiceStopped())

    }
}
