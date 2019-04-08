package com.rod.uidemo.testtouch

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.rod.uidemo.R
import kotlinx.android.synthetic.main.fragment_test_touch.*

/**
 *
 * @author Rod
 * @date 2019/4/8
 */
class TestTouchFragment: Fragment() {
    companion object {
        const val TAG = "TestTouchFragment"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_test_touch, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        list_view.adapter = ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, android.R.id.text1, getData())
    }

    private fun getData(): Array<String> {
        return (0 until 100).map { "item_$it" }.toTypedArray()
    }
}