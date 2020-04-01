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

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.SortedList
import com.m7mdra.copythat.clipboard.ClipBoardService
import com.m7mdra.copythat.database.ClipEntry
import io.reactivex.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*

const val TAG = "MEGA"
fun Any?.log(tag: String = "MEGA") = this?.also {
    Log.d(tag, "$this")
}

fun String.md5(): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
}
fun Context.isMyServiceRunning(): Boolean {
    val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
        if (ClipBoardService::class.java.name == service.service.className) {
            return true
        }
    }

    return false
}

fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

object Bus {

    private val publisher = PublishSubject.create<ServiceEvent>()

    fun publish(event: ServiceEvent) {
        publisher.onNext(event)
    }


    fun listen(): Observable<ServiceEvent> = publisher.ofType(ServiceEvent::class.java)
}

sealed class ServiceEvent
class ServiceStarted : ServiceEvent() {
    override fun equals(other: Any?): Boolean {
        return this === other
    }

    override fun hashCode(): Int {
        return System.identityHashCode(this)
    }

    override fun toString(): String {
        return javaClass.simpleName
    }
}

class ServiceStopped : ServiceEvent() {
    override fun equals(other: Any?): Boolean {
        return this === other
    }

    override fun hashCode(): Int {
        return System.identityHashCode(this)
    }

    override fun toString(): String {
        return javaClass.simpleName
    }
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.hide() {
    animate().alpha(0f).withEndAction {
        invisible()
    }.start()
}

fun View.show() {
    animate().alpha(1f).withEndAction {
        visible()
    }.start()

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

@SuppressLint("CheckResult")
fun Completable.ioMainTransform(): Completable {
    subscribeOn(Schedulers.io())
    observeOn(AndroidSchedulers.mainThread())
    return this
}

@SuppressLint("CheckResult")
fun <T> Single<T>.ioMainTransformer() {
    subscribeOn(Schedulers.io())
    observeOn(AndroidSchedulers.mainThread())
}

@SuppressLint("CheckResult")
fun <T> Maybe<T>.ioMainTransformer() {
    subscribeOn(Schedulers.io())
    observeOn(AndroidSchedulers.mainThread())
}

@SuppressLint("CheckResult")
fun <T> Flowable<T>.ioMainTransformer() {
    subscribeOn(Schedulers.io())
    observeOn(AndroidSchedulers.mainThread())
}

@SuppressLint("CheckResult")
fun <T> Observable<T>.ioMainTransformer() {
    subscribeOn(Schedulers.io())
    observeOn(AndroidSchedulers.mainThread())
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

fun String.removeNewLineAndTaps() =
    this.replace(regex = Regex("\n"), replacement = "")
        .replace(regex = Regex("\t"), replacement = "")
        .replace(regex = Regex("\r"), replacement = "")



