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

package com.m7mdra.copythat

import android.app.Application
import com.m7mdra.copythat.di.appModule
import com.m7mdra.copythat.di.databaseModule
import com.m7mdra.copythat.di.viewModelModule
import org.koin.android.ext.android.startKoin
import org.koin.android.logger.AndroidLogger
import org.koin.core.Koin
import java.util.logging.Logger

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(
            androidContext = this,
            modules = listOf(appModule, databaseModule, viewModelModule),
            logger = AndroidLogger(true))
    }
}