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
import androidx.lifecycle.MutableLiveData
import com.m7mdra.copythat.ACTION_STOP_SERVICE
import com.m7mdra.copythat.NOTIFICATION_ID
import com.m7mdra.copythat.log
import org.koin.android.ext.android.inject

class ClipBoardService : Service() {

    private val clipBoard: ClipBoard by inject()
    private val clipNotification: ClipNotification by inject()

    companion object {
        var isServiceRunning = false
        val runningLiveData = MutableLiveData<Boolean>()

    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        "Service Created".log()
        clipBoard.start()
    }

    override fun onDestroy() {
        clipBoard.stop()
        super.onDestroy()
        "Service stopped".log()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return if (intent.action == ACTION_STOP_SERVICE) {
            stopForeground(true)
            stopSelf()
            runningLiveData.value = false
            isServiceRunning = false
            super.onStartCommand(intent, flags, startId)
        } else {
            runningLiveData.value = true
            isServiceRunning = true

            startForeground(NOTIFICATION_ID, clipNotification.builder.build())
            START_STICKY
        }
    }

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        runningLiveData.value = false
        isServiceRunning = false
    }
}
