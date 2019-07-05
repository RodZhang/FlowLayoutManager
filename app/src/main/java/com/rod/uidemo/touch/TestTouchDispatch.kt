package com.rod.uidemo.touch

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.rod.uidemo.R
import kotlinx.android.synthetic.main.fragment_touch_trace.*

/**
 *
 * @author Rod
 * @date 2019/6/18
 */
class TestTouchDispatch: Fragment() {

    companion object {
        const val TAG = "TestTouchDispatch"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_touch_trace, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        container.mTag = "container"
        trace1.mTag = "trace1"
        trace2.mTag = "trace2"
        trace3.mTag = "trace3"
        trace4.mTag = "trace4"
        trace2.setOnClickListener {
            Toast.makeText(activity, "trace2 click", Toast.LENGTH_SHORT).show()
        }
    }
}