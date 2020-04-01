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

import androidx.annotation.VisibleForTesting
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*


@Entity(tableName = "clipEntries", indices = [Index("hash", unique = true)])
data class ClipEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val data: String,
    var date: Long = Date().time,
    val mimeType: String="String",
    var isFavorite: Int = 0,
    var hash: String = ""
) : Serializable {
    companion object {

        @VisibleForTesting
        @JvmStatic
        fun empty(): ClipEntry {
            return ClipEntry(0, "", 0L, "", 0)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ClipEntry

        if (id != other.id) return false
        if (data != other.data) return false
        if (date != other.date) return false
        if (mimeType != other.mimeType) return false
        if (isFavorite != other.isFavorite) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + data.hashCode()
        result = 31 * result + date.hashCode()
        result = 31 * result + mimeType.hashCode()
        result = 31 * result + isFavorite
        return result
    }
}