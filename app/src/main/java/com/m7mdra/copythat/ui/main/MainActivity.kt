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

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.m7mdra.copythat.*
import com.m7mdra.copythat.ui.settings.SettingsFragment
import com.m7mdra.copythat.clipboard.ClipBoardService
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModel()
    private lateinit var adapter: ClipEntriesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        adapter = ClipEntriesAdapter({

        }, {
            viewModel.toggleFavorite(it)
        })
        entriesRecyclerView.apply {
            adapter = this@MainActivity.adapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
        viewModel.clipEntriesLiveData.observe(this, Observer { it ->
            it.log()
            if (it is QuerySuccessEvent) {
                statusTextView.visibility = View.GONE
                adapter.addItems(it.entries)
            }
            if (it is QueryEventError) {
                statusTextView.text = it.throwable.message
                statusTextView.visibility=View.VISIBLE

            }
            if (it is QueryEmptyEvent){
                statusTextView.visibility=View.VISIBLE
            }

        })
        viewModel.loadEntries()
        if (ClipBoardService.isActive) {
            checked()
        } else {
            notChecked()
        }

        _switch.setOnCheckedChangeListener { buttonView, isChecked ->
            val intent = Intent(this@MainActivity, ClipBoardService::class.java)
            if (isChecked) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(intent)
                } else {
                    startService(intent)
                }
                checked()
            } else {
                stopService(intent)
                notChecked()
            }
        }
        searchBar.setOnQueryTextFocusChangeListener { v, hasFocus ->
            searchLayout.visibility = if (hasFocus)
                View.VISIBLE
            else
                View.GONE
        }
        settingsButton.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.main, SettingsFragment(), "settingsFragment")
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                addToBackStack("")
                commit()

            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.settings, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun notChecked() {
        _switch.text = "CopyThat is not Running"
        _switch.isChecked = false

    }

    private fun checked() {
        _switch.text = "CopyThat is Running"
        _switch.isChecked = true

    }


}
