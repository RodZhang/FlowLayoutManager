package com.rod.uidemo.textclock

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rod.uidemo.R
import kotlinx.android.synthetic.main.fragment_text_clock.*

/**
 *
 * @author Rod
 * @date 2019/8/14
 */
class TextClockFragment : Fragment() {
    companion object {
        const val TAG = "TextClockFragment"
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_text_clock, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mClockView.setOnClickListener { mClockView.invalidate() }
    }
}