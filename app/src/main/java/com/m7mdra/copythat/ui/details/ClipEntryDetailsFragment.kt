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

package com.m7mdra.copythat.ui.details


import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.m7mdra.copythat.*
import com.m7mdra.copythat.database.ClipEntry
import com.m7mdra.copythat.ui.main.MainViewModel
import kotlinx.android.synthetic.main.fragment_clip_entry_details.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ClipEntryDetailsFragment : Fragment() {
    private val viewModel: MainViewModel by viewModel()
    private lateinit var activity: Activity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as Activity
    }

    lateinit var clipEntry: ClipEntry
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        clipEntry = arguments?.getSerializable("clip") as ClipEntry
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_clip_entry_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateData(clipEntry)
        viewModel.findClip(clipEntry.id)
        backButton.setOnClickListener {
            getActivity()?.supportFragmentManager?.beginTransaction()?.remove(this)?.commit()
        }
        toggleFavoriteButton.setOnClickListener {
            viewModel.toggleFavorite(clipEntry)
        }
        deleteButton.setOnClickListener {
            AlertDialog.Builder(activity, R.style.Theme_MaterialComponents_Light_Dialog)
                .setTitle(getString(R.string.remove_entry_dialog_title))

                .setMessage(getString(R.string.remove_entry_dialog_message))
                .setPositiveButton(getString(R.string.remove_entry_dialog_positive_button)) { dialogInterface: DialogInterface, _: Int ->
                    viewModel.deleteEntry(clipEntry)
                    dialogInterface.dismiss()
                }
                .setNegativeButton(getString(R.string.remove_entry_dialog_negative_button)) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
        undoButton.setOnClickListener {
            viewModel.unDoDelete()
        }
        viewModel.deleteLiveData.observe(this, Observer {
            when (it) {
                is DeleteSuccessEvent -> {
                    undoLayout.show()
                    dataScrollView.hide()
                    clipControls.hide()
                }
                is DeleteFailedEvent -> {

                }

            }
        })
        viewModel.clipEntriesLiveData.observe(this, Observer {
            if (it is QuerySuccessEvent) {
                print(it.entries)
                dataScrollView.show()
                clipControls.show()
                undoLayout.hide()
                updateData(it.entries.first())
            }

        })
    }

    private fun updateData(clipEntry: ClipEntry) {
        clipDataTextView.text = clipEntry.data
        toggleFavoriteButton.setCompoundDrawablesRelativeWithIntrinsicBounds(
            null,
            ContextCompat.getDrawable(
                activity,
                if (clipEntry.isFavorite == 1) R.drawable.ic_favorite_black_24dp else R.drawable.ic_favorite_border_black_24dp
            )?.apply {
                setColorFilter(
                    if (clipEntry.isFavorite == 1) Color.RED else Color.BLACK,
                    PorterDuff.Mode.SRC_IN
                )
            },
            null,
            null
        )

    }

    companion object {
        fun from(clipEntry: ClipEntry): ClipEntryDetailsFragment {
            val fragment = ClipEntryDetailsFragment()
            val bundle = Bundle()
            bundle.putSerializable("clip", clipEntry)
            fragment.arguments = bundle
            return fragment
        }
    }
}
