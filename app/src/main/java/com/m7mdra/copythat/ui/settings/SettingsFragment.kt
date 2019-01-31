package com.m7mdra.copythat.ui.settings


import android.content.Context
import android.os.Bundle
import android.view.*

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.m7mdra.copythat.R
import kotlinx.android.synthetic.main.fragment_settings.*


class SettingsFragment : Fragment() {
    private lateinit var ctx: Context
    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.ctx = context
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentManager?.beginTransaction()?.apply {
            replace(R.id.fragmentLayout, PreferenceFragment(),"preference_fragment")
            commit()
        }
        closeFragmentButton.setOnClickListener {
            fragmentManager?.beginTransaction()?.remove(this)
                ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                ?.commit()

        }
    }


}
