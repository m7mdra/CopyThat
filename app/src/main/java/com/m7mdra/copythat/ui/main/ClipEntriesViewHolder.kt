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

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.m7mdra.copythat.R
import kotlinx.android.synthetic.main.row_clip_entry.view.*

class ClipEntriesViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    val clipDataTextView: TextView = view.findViewById(R.id.clipDataTextView)
    val clipDateTextView: TextView = view.findViewById(R.id.clipDateTextView)
    val toggleFavoriteButton: AppCompatImageView = view.findViewById(R.id.toggleFavoriteButton)


}
