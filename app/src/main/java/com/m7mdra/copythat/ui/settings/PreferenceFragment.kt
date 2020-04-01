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

package com.m7mdra.copythat.ui.settings


import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.m7mdra.copythat.R
import com.m7mdra.copythat.RecordsDeleteFailedEvent
import com.m7mdra.copythat.RecordsDeletedEvent
import com.m7mdra.copythat.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

class PreferenceFragment : PreferenceFragmentCompat() {
    private lateinit var activity: Activity
    private val settingsViewModel: SettingsViewModel by viewModel()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as Activity
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings_prefence)
        settingsViewModel.loadClipsCount()
        findPreference<SwitchPreference>("nightmode_key")?.setOnPreferenceChangeListener { preference, newValue ->
            AppCompatDelegate.setDefaultNightMode(if (newValue as Boolean) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
            true
        }
        findPreference<Preference>("key_delete_records")?.setOnPreferenceClickListener {
            AlertDialog.Builder(activity, R.style.Theme_MaterialComponents_Light_Dialog)
                .setTitle(getString(R.string.remove_entry_dialog_title))
                .setMessage(getString(R.string.remove_entry_dialog_message))
                .setPositiveButton(getString(R.string.remove_entry_dialog_positive_button)) { dialogInterface: DialogInterface, _: Int ->
                    settingsViewModel.clearRecords()
                    dialogInterface.dismiss()
                }
                .setNegativeButton(getString(R.string.remove_entry_dialog_negative_button)) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
            true
        }
        settingsViewModel.clipsCountLiveData.observe(this, Observer {
            findPreference<Preference>("key_clips_count")?.summary = "$it clips saved."
        })

        settingsViewModel.deleteEventLiveData.observe(this, Observer {
            if (it is RecordsDeletedEvent) {
                activity.toast("records deleted successfully")
            }
            if (it is RecordsDeleteFailedEvent) {
                activity.toast("failed to delete records.")

            }
        })
    }


}
