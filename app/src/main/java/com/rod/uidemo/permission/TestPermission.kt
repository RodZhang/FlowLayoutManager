package com.rod.uidemo.permission

import android.Manifest
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rod.uidemo.R
import kotlinx.android.synthetic.main.fragment_test_permission.*

/**
 *
 * @author Rod
 * @date 2019/6/4
 */
class TestPermission : Fragment() {

    companion object {
        const val TAG = "TestPermission"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_test_permission, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val invoker = AppOpsPermission(context!!)
        invoker.doWithPermission(
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
        ) { permissions ->
            var str = ""
            permissions.forEach { str += it.toString() + "\n" }
            text.text = str
        }
    }
}