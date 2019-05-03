package com.rod.uidemo.limitcount

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rod.uidemo.R
import kotlinx.android.synthetic.main.fragment_testview.*

/**
 *
 * @author Rod
 * @date 2019/5/2
 */
class TestViewFragment : Fragment() {

    companion object {
        const val TAG = "TestViewFragment"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_testview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        counter.setAnchorEditText(edit)
    }
}