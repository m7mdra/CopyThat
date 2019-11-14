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

import android.annotation.TargetApi
import android.content.Intent
import android.graphics.drawable.Icon
import android.os.Build
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import com.m7mdra.copythat.R
import com.m7mdra.copythat.ServiceEvent
import com.m7mdra.copythat.isMyServiceRunning
import com.m7mdra.copythat.log
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

@TargetApi(Build.VERSION_CODES.N)
class ClipTileService : TileService() {

    override fun onClick() {
        super.onClick()
        val serviceIntent = Intent(this, ClipBoardService::class.java)
        if (isMyServiceRunning()) {
            stopService(serviceIntent)
            updateTile()

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(serviceIntent)
            } else {
                startService(serviceIntent)
            }
            updateTile()

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: ServiceEvent) {
        updateTile()
    }


    override fun onStartListening() {
        super.onStartListening()
        updateTile()

    }

    private fun updateTile() {
        if (isMyServiceRunning()) {
            activeTile()
        } else {
            inactiveTile()

        }
        qsTile.updateTile()
    }

    private fun inactiveTile() {
        qsTile.state = Tile.STATE_INACTIVE
        qsTile.icon = Icon.createWithResource(this, R.drawable.ic_typewriter_without_paper)
    }

    private fun activeTile() {
        qsTile.state = Tile.STATE_ACTIVE
        qsTile.icon = Icon.createWithResource(this, R.drawable.ic_typewriter_with_paper)
    }

    override fun onTileAdded() {
        super.onTileAdded()
        updateTile()
    }

    override fun onTileRemoved() {
        super.onTileRemoved()
        updateTile()
    }

    override fun onStopListening() {
        super.onStopListening()
        updateTile()
    }
}