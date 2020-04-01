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
import io.reactivex.Single

class ClipEntryRepository(private val dao: ClipEntryDao) : ClipRepository {
    override fun getClipsCount(): Flowable<Int> {
        return dao.getCount()
    }

    override fun doseExists(hash: String): Single<Boolean> {
        return dao.doseClipExists(hash)

            .flatMap { clip: ClipEntry? ->
                Single.just(clip != null)
            }.onErrorReturn { false }

    }

    override fun getEntries(): Flowable<List<ClipEntry>> = dao.getEntries()

    override fun deleteAll(): Completable = dao.deleteAll()

    override fun findEntries(query: String): Flowable<List<ClipEntry>> {
        return dao.findEntries(query)
    }

    override fun findClipById(id: Int): Flowable<ClipEntry> = dao.findClipById(id)

    override fun deleteEntry(id: Int): Completable = dao.deleteEntry(id)

    override fun insert(clipEntry: ClipEntry): Completable = dao.insert(clipEntry)

    override fun toggleFavorite(clipEntry: ClipEntry): Completable = dao.toggleFavorite(clipEntry)
    override fun getFavoriteEntries(): Flowable<List<ClipEntry>> = dao.getFavoriteEntries()


}