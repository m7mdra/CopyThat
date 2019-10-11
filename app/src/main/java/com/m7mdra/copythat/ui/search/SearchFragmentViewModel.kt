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

package com.m7mdra.copythat.ui.search

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.m7mdra.copythat.*
import com.m7mdra.copythat.database.ClipDatabase
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class SearchFragmentViewModel(private val clipDatabase: ClipDatabase) : ViewModel() {
    private val queryEvent = MutableLiveData<QueryEvent>()
    private val disposables = mutableListOf<Disposable>()
    fun observe(owner: LifecycleOwner, observable: Observer<QueryEvent>) =
        queryEvent.observe(owner, observable)

    fun findClipEntry(keyword: String) {
        disposables + Flowable.just(keyword)
            .filter { it.isNotEmpty() }
            .debounce(2, TimeUnit.SECONDS)
            .flatMap { clipDatabase.dao().findEntries(it) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.isEmpty()) {
                    queryEvent.value = QueryEmptyEvent()

                } else {

                    queryEvent.value = QuerySuccessEvent(it)
                }
            }, {
                queryEvent.value = QueryEventError(it)
            })
    }

    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

}