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

package com.m7mdra.copythat.ui.contextmenucopy

import android.content.Intent.EXTRA_PROCESS_TEXT
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.m7mdra.copythat.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

class ContextMenuCopyActionActivity : AppCompatActivity() {
    val viewModel: ContextMenuCopyViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val copyText = intent?.getCharSequenceExtra(EXTRA_PROCESS_TEXT)
            if (copyText != null) {
                viewModel.saveClip(copyText.toString())
                viewModel.eventLiveData.observe(this, Observer {
                    if (it is OnCopyFailed) {
                        toast("failed to copy text")
                        finishAffinity()
                    }
                    if (it is OnCopySuccess) {
                        toast("Text saved successfully")
                        finishAffinity()

                    }
                })
            } else {
                finishAffinity()
            }
        } else {
            finishAffinity()
        }

    }
}
