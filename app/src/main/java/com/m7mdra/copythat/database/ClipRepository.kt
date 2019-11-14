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

package com.m7mdra.copythat.database

import io.reactivex.Completable
import io.reactivex.Flowable

interface ClipRepository {

    fun getEntries(): Flowable<List<ClipEntry>>

    fun deleteAll(): Completable

    fun findEntries(query: String): Flowable<List<ClipEntry>>

    fun findClipById(id: Int): Flowable<ClipEntry>

    fun deleteEntry(id: Int): Completable

    fun insert(clipEntry: ClipEntry): Completable

    fun toggleFavorite(clipEntry: ClipEntry): Completable

    fun getFavoriteEntries(): Flowable<List<ClipEntry>>
    fun getClipsCount(): Flowable<Int>

}