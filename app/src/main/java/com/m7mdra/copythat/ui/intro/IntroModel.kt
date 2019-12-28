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

package com.m7mdra.copythat.ui.intro

import androidx.annotation.DrawableRes

data class IntroModel(@DrawableRes val image: Int, val title: String, val message: String) {
    companion object {
        fun empty(): IntroModel {
            return IntroModel(0, "", "")
        }
    }
}