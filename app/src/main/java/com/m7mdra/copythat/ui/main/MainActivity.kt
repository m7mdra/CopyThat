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

import android.app.ActivityManager
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.m7mdra.copythat.*
import com.m7mdra.copythat.clipboard.ClipBoardService
import com.m7mdra.copythat.ui.search.SearchFragment
import com.m7mdra.copythat.ui.settings.SettingsFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T




class MainActivity : AppCompatActivity() {


    private val viewModel: MainViewModel by viewModel()
    private lateinit var adapter: ClipEntriesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        adapter = ClipEntriesAdapter(
            onClick = {

            }, onToggleFavoriteClicked = {
                viewModel.toggleFavorite(it)
            },
            onDeleteClicked = {
                AlertDialog.Builder(this,R.style.Theme_MaterialComponents_Light_Dialog)
                    .setTitle("Remove Entry?")

                    .setMessage("Entry will remove for ever, are you sure?")
                    .setPositiveButton("Remove") { dialogInterface: DialogInterface, _: Int ->
                        viewModel.deleteEntry(it)
                        dialogInterface.dismiss()
                    }.setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            },onShareClicked = {
                val share = Intent(Intent.ACTION_SEND)
                share.type = "text/plain"
                share.putExtra(Intent.EXTRA_TEXT, it.data)

                startActivity(Intent.createChooser(share, "Share link!"))
            })
        entriesRecyclerView.apply {
            adapter = this@MainActivity.adapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
        viewModel.deleteLiveData.observe(this, Observer {
            when (it) {
                is DeleteFailedEvent -> Snackbar.make(
                    main,
                    "Failed to delete record",
                    Snackbar.LENGTH_SHORT
                ).show()
                is DeleteSuccessEvent -> {
                    adapter.notifyDataSetChanged()
                    Snackbar.make(
                        main,
                        "Record delete successfully",
                        Snackbar.LENGTH_LONG
                    ).setAction("Undo") {
                        viewModel.unDoDelete()
                    }.show()
                }
            }
        })
        viewModel.clipEntriesLiveData.observe(this, Observer { it ->
            it.log()
            if (it is QuerySuccessEvent) {
                statusTextView.visibility = View.GONE
                entriesRecyclerView.visibility = View.VISIBLE

                adapter.addItems(it.entries)
            }
            if (it is QueryEventError) {
                statusTextView.text = it.throwable.message
                statusTextView.visibility = View.VISIBLE

            }
            if (it is QueryEmptyEvent) {
                entriesRecyclerView.visibility = View.GONE
                statusTextView.visibility = View.VISIBLE
            }

        })
        viewModel.loadEntries()
        if (isMyServiceRunning(ClipBoardService::class.java)) {
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
            if (hasFocus)
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.main, SearchFragment(), "searchFragment")
                    setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    addToBackStack("")
                    commit()

                }
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

    private fun isMyServiceRunning(serviceClass: Class<*>): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }
}
