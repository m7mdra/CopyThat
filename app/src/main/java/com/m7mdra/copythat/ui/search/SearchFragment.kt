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


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.m7mdra.copythat.*
import kotlinx.android.synthetic.main.fragment_search.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class SearchFragment : Fragment() {
    val searchFragmentViewModel: SearchFragmentViewModel by viewModel()
    val adapter = SearchAdapter()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchRecyclerView.adapter = adapter
        searchRecyclerView.layoutManager = LinearLayoutManager(context)
        searchRecyclerView.addItemDecoration(DividerItemDecoration(context,DividerItemDecoration.VERTICAL))
        searchFragmentViewModel.observe(this, Observer {
            if (it is QuerySuccessEvent) {
                emptyText.invisible()
                searchRecyclerView.visible()
                adapter.addItems(it.entries)

            }
            if (it is QueryEventError) {
                context?.toast("Error occurred.")
            }
            if (it is QueryEmptyEvent) {
                emptyText.visible()
                searchRecyclerView.invisible()

            }
        })
        searchBar.requestFocus()
        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                submitQuery(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                submitQuery(newText)

                return true
            }

            private fun submitQuery(newText: String?) = newText?.apply {
                searchFragmentViewModel.findClipEntry(newText)
                adapter.changeQuery(newText)
            }

        })


    }
}
