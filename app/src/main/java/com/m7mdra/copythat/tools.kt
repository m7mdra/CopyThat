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

package com.m7mdra.copythat

import android.util.Log
import android.content.Context.ACTIVITY_SERVICE
import androidx.core.content.ContextCompat.getSystemService
import android.app.ActivityManager
import android.content.Context
import android.widget.Toast
import androidx.recyclerview.widget.SortedList
import io.reactivex.CompletableTransformer
import io.reactivex.FlowableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*


fun Any?.log(tag: String = "MEGA") = this?.also {
    Log.d(tag, "$this")
}


fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Long?.toDate(): Date {
    return if (this != null)
        Date(this)
    else Date()
}

fun List<Disposable>.dispose() = filter { !it.isDisposed }.forEach { it.dispose() }

fun ioMainTransformer() = CompletableTransformer { upstream ->
    upstream.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
}

fun <T> SortedList<T>.toList(): List<T> {
    val list = mutableListOf<T>()
    val size = this.size()
    for (i in 0 until size) {
        val t = get(i)
        list.add(t)
    }
    return list
}

