package com.rod.uidemo

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_test_btn_status.*

/**
 *
 * @author Rod
 * @date 2019/1/21
 */
class TestBtnStatusFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_test_btn_status, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activeBtn.setOnClickListener { statusBtn.isActivated = false }
        enableBtn.setOnClickListener {
            statusBtn.isEnabled = false
            statusBtn.isActivated = true
        }
        normalBtn.setOnClickListener {
            statusBtn.isActivated = true
            statusBtn.isEnabled = true
        }
    }

    companion object {
        const val TAG = "TestBtnStatusFragment"
    }
}