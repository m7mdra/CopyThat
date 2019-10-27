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

import android.graphics.Color
import android.graphics.Typeface
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.m7mdra.copythat.R
import com.m7mdra.copythat.database.ClipEntry
import com.m7mdra.copythat.removeNewLineAndTaps
import kotlinx.android.synthetic.main.row_clip_entry.view.*
import java.util.*


class ClipEntriesAdapter(
    private val onClick: (ClipEntry) -> Unit,
    private val onToggleFavoriteClicked: (ClipEntry) -> Unit,
    private val onShareClicked: (ClipEntry) -> Unit,
    private val onDeleteClicked: (ClipEntry) -> Unit
) : RecyclerView.Adapter<ClipEntriesViewHolder>() {
    private val clipList = mutableListOf<ClipEntry>()

    fun addItems(list: List<ClipEntry>) {
        if (clipList.isNotEmpty()) {
            val diffUlti = EntiresDiffUlti(clipList, list)
            val calculateDiff = DiffUtil.calculateDiff(diffUlti)
            clipList.clear()
            clipList.addAll(list)
            calculateDiff.dispatchUpdatesTo(this)
            notifyDataSetChanged()
        } else {
            clipList.addAll(list)
            notifyDataSetChanged()
        }
    }

    private fun clear() {
        clipList.clear()
    }

    fun clearAndNotify() {
        clipList.clear()
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClipEntriesViewHolder {
        return ClipEntriesViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_clip_entry, parent, false)
        )
    }

    override fun getItemCount(): Int = clipList.size

    override fun onBindViewHolder(holder: ClipEntriesViewHolder, position: Int) {
        val clipEntry = clipList[holder.adapterPosition]
        val dateRange = DateUtils.getRelativeTimeSpanString(
            clipEntry.date,
            System.currentTimeMillis(),
            DateUtils.SECOND_IN_MILLIS,
            DateUtils.FORMAT_ABBREV_RELATIVE
        )
        holder.apply {
            clipDataTextView.text = clipEntry.data.trim().removeNewLineAndTaps()
            clipDateTextView.text = dateRange
            toggleFavoriteButton.setOnClickListener {
                onToggleFavoriteClicked(clipEntry)
            }
            itemView.setOnClickListener {
                onClick(clipEntry)
            }
            itemView.deleteButton.setOnClickListener {
                onDeleteClicked(clipEntry)
            }
            itemView.shareButton.setOnClickListener {
                onShareClicked(clipEntry)
            }

            if (clipEntry.isFavorite == 0) {
                toggleFavoriteButton.setImageResource(R.drawable.ic_favorite_border_black_24dp)
                toggleFavoriteButton.setColorFilter(Color.BLACK)
                clipDataTextView.typeface = Typeface.DEFAULT

            } else {
                toggleFavoriteButton.setImageResource(R.drawable.ic_favorite_black_24dp)
                toggleFavoriteButton.setColorFilter(Color.RED)
                clipDataTextView.typeface = Typeface.DEFAULT_BOLD

            }
        }
    }


}