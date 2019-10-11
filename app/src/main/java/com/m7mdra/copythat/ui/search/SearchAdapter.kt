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

import android.graphics.Color
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.format.DateUtils
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.m7mdra.copythat.R
import com.m7mdra.copythat.database.ClipEntry
import java.util.*
import java.util.regex.Pattern


class SearchAdapter : RecyclerView.Adapter<SearchViewHolder>() {
    private val searchList: MutableList<ClipEntry> = mutableListOf()
    private var currentQuery = ""

    fun changeQuery(query: String) {
        currentQuery = query
    }

    fun addItems(newSearchResult:List<ClipEntry>){
        searchList.clear()
        searchList.addAll(newSearchResult)
        searchList.sortByDescending { it.id }
        searchList.sortByDescending { it.isFavorite }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder =
        SearchViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.search_row,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = searchList.size

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val clipEntry = searchList[position]
        val spannableBuilder = SpannableStringBuilder(clipEntry.data)
        val word = Pattern.compile(currentQuery.toLowerCase(Locale.getDefault()))
        val matcher = word.matcher(clipEntry.data.toLowerCase(Locale.getDefault()))
        while (matcher.find()) {
            spannableBuilder.setSpan(
                StyleSpan(Typeface.BOLD),
                matcher.start(),
                matcher.end(),
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
        }
        holder.searchTextView.text = spannableBuilder
        val dateRange = DateUtils.getRelativeTimeSpanString(
            clipEntry.date,
            System.currentTimeMillis(),
            DateUtils.SECOND_IN_MILLIS,
            DateUtils.FORMAT_ABBREV_RELATIVE
        )
        holder.searchDate.text = dateRange
    }
}


class SearchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val searchTextView = view.findViewById<TextView>(R.id.searchTextView)
    val searchDate = view.findViewById<TextView>(R.id.searchDate)


}