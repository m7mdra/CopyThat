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

import android.os.SystemClock
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.m7mdra.copythat.database.ClipEntry
import com.m7mdra.copythat.database.ClipRepository
import com.m7mdra.copythat.dispose
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class ContextMenuCopyViewModel(private val repository: ClipRepository) : ViewModel() {
    private val disposables = mutableListOf<Disposable>()
    val eventLiveData = MutableLiveData<ContextMenuCopyEvent>()

    fun saveClip(text: String) {
        disposables + repository.insert(
            ClipEntry(
                data = text,
                date = SystemClock.elapsedRealtime(),
                mimeType = "text"
            )
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                eventLiveData.value = OnCopySuccess()
            }, {
                eventLiveData.value = OnCopyFailed()
            })
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }
}