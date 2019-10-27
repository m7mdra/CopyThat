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

package com.m7mdra.copythat.di

import android.preference.PreferenceManager
import androidx.room.Room
import com.m7mdra.copythat.clipboard.ClipBoard
import com.m7mdra.copythat.clipboard.ClipNotification
import com.m7mdra.copythat.database.ClipDatabase
import com.m7mdra.copythat.ui.main.MainViewModel
import com.m7mdra.copythat.ui.search.SearchFragmentViewModel
import com.m7mdra.copythat.ui.settings.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val appModule = module {
    single { ClipNotification(androidContext()) }
    single { ClipBoard(androidContext(), get()) }
    single { PreferenceManager.getDefaultSharedPreferences(androidContext()) }
}

val databaseModule = module {
    single { Room.databaseBuilder(androidContext(), ClipDatabase::class.java, "clips")

        .build() }

}

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { SearchFragmentViewModel(get()) }
    viewModel { SettingsViewModel(get()) }
}
