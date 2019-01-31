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


import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.m7mdra.copythat.R

class PreferenceFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.settings_prefence)

    }


}
