package com.rod.uidemo.test

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rod.uidemo.R

/**
 *
 * @author Rod
 * @date 2018/10/9
 */
class TestLayoutAnimFragment: Fragment() {

    companion object {
        const val TAG = "TestLayoutAnimFragment"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_test_layout_anim, container, false)
    }
}