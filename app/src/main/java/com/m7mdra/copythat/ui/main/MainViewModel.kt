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

package com.m7mdra.copythat.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.m7mdra.copythat.*
import com.m7mdra.copythat.database.ClipDatabase
import com.m7mdra.copythat.database.ClipEntry
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MainViewModel(private val clipDatabase: ClipDatabase) : ViewModel() {
    private val disposables = mutableListOf<Disposable>()
    val deleteLiveData = MutableLiveData<DeleteEvent>()
    val clipEntriesLiveData = MutableLiveData<QueryEvent>()


    fun toggleFavorite(clipEntry: ClipEntry) {
        clipEntry.isFavorite = if (clipEntry.isFavorite == 0) 1 else 0
        disposables + clipDatabase.dao()
                .toggleFavorite(clipEntry)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({}, {})
    }

    fun deleteEntry(clipEntry: ClipEntry) {
        disposables + clipDatabase.dao().deleteEntry(clipEntry.id)
                .compose(ioMainTransformer())
                .subscribe({
                    deleteLiveData.value = DeleteSuccessEvent(clipEntry)
                }, {
                    deleteLiveData.value = DeleteFailedEvent(it)
                })
    }

    fun loadEntries() {
        disposables + clipDatabase.dao().getEntries()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    clipEntriesLiveData.value = if (it.isEmpty())
                        QueryEmptyEvent()
                    else
                        QuerySuccessEvent(it)
                }, {
                    clipEntriesLiveData.value = QueryEventError(it)
                })
    }

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }
}