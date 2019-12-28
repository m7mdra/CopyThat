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

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.m7mdra.copythat.R
import com.m7mdra.copythat.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_intro.*

class IntroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position != 0) {
                    previousButton.visibility = View.VISIBLE
                } else {
                    previousButton.visibility = View.GONE
                }
                if (position == 2) {
                    nextButton.text = "Finish"
                    nextButton.setOnClickListener {
                        startActivity(Intent(this@IntroActivity, MainActivity::class.java))
                        finish()
                    }
                } else {
                    nextButton.text = "Next"
                    nextButton.setOnClickListener {
                        viewPager.currentItem = viewPager.currentItem + 1
                    }
                }

            }
        })

        previousButton.setOnClickListener {
            viewPager.currentItem = viewPager.currentItem - 1

        }
        viewPager.adapter = IntroAdapter(
            listOf(
                IntroModel(
                    R.drawable.intro1, "RUNNING IN THE BACKGROUND", "CopyThat will run in the " +
                            "background and keep track of " +
                            "you actions without any hassle"
                )
                ,
                IntroModel(
                    R.drawable.intro2,
                    "Select text and save it",
                    "Even if CopyThat is not running you can still save text"
                ), IntroModel(
                    R.drawable.intro3,
                    "Easy Access",
                    "You enable and disable CopyThat from menu"
                )
            )
        )
    }
}

