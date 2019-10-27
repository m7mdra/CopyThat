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
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MainViewModel(private val clipDatabase: ClipDatabase) : ViewModel() {
    private val disposables = mutableListOf<Disposable>()
    val deleteLiveData = MutableLiveData<DeleteEvent>()
    val clipEntriesLiveData = MutableLiveData<QueryEvent>()
    private val lastDeleteItem = MutableLiveData<ClipEntry>()

    fun toggleFavorite(clipEntry: ClipEntry) {
        clipEntry.isFavorite = if (clipEntry.isFavorite == 0) 1 else 0
        disposables + clipDatabase.dao()
            .toggleFavorite(clipEntry)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe()
    }

    fun deleteEntry(clipEntry: ClipEntry) {
        disposables + Single.just(clipEntry).flatMapCompletable {
            clipDatabase.dao().deleteEntry(clipEntry.id)
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                deleteLiveData.value = DeleteSuccessEvent(clipEntry)
                lastDeleteItem.value = clipEntry

            }, {
                deleteLiveData.value = DeleteFailedEvent(it)

            })
    }

    fun findClip(id: Int) {
        disposables + clipDatabase.dao().findClipById(id)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe({
                clipEntriesLiveData.value = QuerySuccessEvent(listOf(it))
            }, {
                clipEntriesLiveData.value = QueryEventError(it)
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

    fun unDoDelete() {
        lastDeleteItem.value?.apply {

            disposables + clipDatabase.dao().insert(this)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        }
    }
}
