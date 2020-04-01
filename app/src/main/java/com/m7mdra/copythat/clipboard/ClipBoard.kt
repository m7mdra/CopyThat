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


import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import com.m7mdra.copythat.database.ClipEntry
import com.m7mdra.copythat.database.ClipEntryDao
import com.m7mdra.copythat.dispose
import com.m7mdra.copythat.ioMainTransformer
import com.m7mdra.copythat.log
import com.m7mdra.copythat.md5
import io.reactivex.disposables.Disposable

class ClipBoard(private val context: Context, private val dao: ClipEntryDao) :
    ClipboardManager.OnPrimaryClipChangedListener {
    private val disposables = mutableListOf<Disposable>()
    override fun onPrimaryClipChanged() {

        val primaryClip = clipboardManager.primaryClip
        if (primaryClip != null) {
            val description = primaryClip.description
            val item = primaryClip.getItemAt(0)
            val text = item.text

            val mimeType = description.getMimeType(0)

            val date = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                description.timestamp
            } else {
                System.currentTimeMillis()
            }
            if (text != null && text.isNotEmpty()) {

                val clipEntry = ClipEntry(
                    data = text.toString(),
                    date = date,
                    mimeType = mimeType,
                    hash = text.toString().md5()
                )
                clipEntry.log()
                insertToDb(clipEntry)
            }
        }
    }

    private fun insertToDb(clipEntry: ClipEntry) {

        disposables + dao.insert(clipEntry)
            .compose(ioMainTransformer())
            .subscribe({
                "inserted item $clipEntry to database successfully".log()
            }, { })
    }


    private var clipboardManager: ClipboardManager =
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    fun start() {
        clipboardManager.addPrimaryClipChangedListener(this)
    }

    fun stop() {
        disposables.dispose()
        clipboardManager.removePrimaryClipChangedListener(this)
    }

}
