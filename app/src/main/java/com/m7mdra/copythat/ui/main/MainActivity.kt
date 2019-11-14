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

import android.content.*
import android.os.Build
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.m7mdra.copythat.*
import com.m7mdra.copythat.clipboard.ClipBoardService
import com.m7mdra.copythat.ui.details.ClipEntryDetailsFragment
import com.m7mdra.copythat.ui.search.SearchFragment
import com.m7mdra.copythat.ui.settings.SettingsFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {


    private val viewModel: MainViewModel by viewModel()
    private lateinit var adapter: ClipEntriesAdapter
    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: ServiceEvent) {
        updateSwitch()
    }

    private fun updateSwitch() {
        if (isMyServiceRunning()) {
            checked()
        } else {
            notChecked()
        }
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        LocalBroadcastManager.getInstance(this).registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                intent?.action.log()
            }
        }, IntentFilter(ClipBoardService::class.java.simpleName))
        copyText.setOnClickListener {
            (getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager).primaryClip =
                ClipData.newPlainText(
                    "Copythat",
                    "Awesome, you copied this text, click to see details\n"
                )
        }
        updateSwitch()
        adapter = ClipEntriesAdapter(
            onClick = {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main, ClipEntryDetailsFragment.from(it), "details")
                    .addToBackStack(null)
                    .commit()
            }, onToggleFavoriteClicked = {
                viewModel.toggleFavorite(it)
            },
            onDeleteClicked = {
                AlertDialog.Builder(this, R.style.Theme_MaterialComponents_Light_Dialog)
                    .setTitle(getString(R.string.remove_entry_dialog_title))
                    .setMessage(getString(R.string.remove_entry_dialog_message))
                    .setPositiveButton(getString(R.string.remove_entry_dialog_positive_button)) { dialogInterface: DialogInterface, _: Int ->
                        viewModel.deleteEntry(it)
                        dialogInterface.dismiss()
                    }
                    .setNegativeButton(getString(R.string.remove_entry_dialog_negative_button)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }, onShareClicked = {
                val share = Intent(Intent.ACTION_SEND)
                share.type = "text/plain"
                share.putExtra(Intent.EXTRA_TEXT, it.data)
                startActivity(Intent.createChooser(share, "Share a Clip"))
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
        viewModel.clipEntriesLiveData.observeForever {
            if (it is QuerySuccessEvent) {
                statusTextView.invisible()
                progressBar.invisible()
                adapter.addItems(it.entries)
                entriesRecyclerView.visible()
            }
            if (it is LoadingEvent) {
                statusTextView.invisible()
                entriesRecyclerView.invisible()
                progressBar.visible()
            }
            if (it is QueryEmptyEvent) {
                entriesRecyclerView.invisible()
                progressBar.invisible()
                statusTextView.visible()
            }

        }
        viewModel.loadEntries()

        switchWidget.setOnCheckedChangeListener { _, isChecked ->
            val intent = Intent(this@MainActivity, ClipBoardService::class.java)
            if (isChecked) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(intent)
                } else {
                    startService(intent)
                }
            } else {
                stopService(intent)
            }
        }
        searchBar.setOnQueryTextFocusChangeListener { _, hasFocus ->
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
        switchWidget.text = "CopyThat is not Running"
        switchWidget.isChecked = false

    }

    private fun checked() {
        switchWidget.text = "CopyThat is Running"
        switchWidget.isChecked = true

    }


}
