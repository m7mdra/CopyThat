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

package com.m7mdra.copythat.ui.settings

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.m7mdra.copythat.DeleteAllEvent
import com.m7mdra.copythat.RecordsDeleteFailedEvent
import com.m7mdra.copythat.RecordsDeletedEvent
import com.m7mdra.copythat.database.ClipRepository
import com.m7mdra.copythat.dispose
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class SettingsViewModel(private val repository: ClipRepository) : ViewModel() {
    private val disposables = mutableListOf<Disposable>()
    val deleteEventLiveData = MutableLiveData<DeleteAllEvent>()
    val clipsCountLiveData = MutableLiveData<Int>()
    fun clearRecords() {
        disposables + repository.deleteAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                deleteEventLiveData.value = RecordsDeletedEvent()

            }, {
                deleteEventLiveData.value = RecordsDeleteFailedEvent()
            })
    }

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }

    fun loadClipsCount() {
        disposables + repository.getClipsCount()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ count ->
                clipsCountLiveData.value = count
            }, {
                clipsCountLiveData.value = 0
            })
    }

}