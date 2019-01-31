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

import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Flowable
import org.intellij.lang.annotations.Flow

@Dao
interface ClipEntryDao {
    @Query("select * from clipEntries")
    fun getEntries(): Flowable<List<ClipEntry>>

    @Query("DELETE FROM clipEntries")
    fun deleteAll(): Completable

    @Query("SELECT * FROM clipEntries WHERE data LIKE :query ")
    fun findEntries(query: String): Flowable<List<ClipEntry>>

    @Query("DELETE FROM clipEntries where id = :id")
    fun deleteEntry(id: Int): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(clipEntry: ClipEntry): Completable

    @Update()
    fun toggleFavorite(clipEntry: ClipEntry): Completable

    @Query("SELECT * FROM clipEntries WHERE isFavorite=1")
    fun getFavoriteEntries(): Flowable<List<ClipEntry>>

}