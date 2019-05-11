package com.rod.uidemo.limitcount

import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.util.Log
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

        Log.d(TAG, "externalStorageDirectory: ${Environment.getExternalStorageDirectory().absolutePath}")
        Log.d(TAG, "externalStoragePublicDirectory: ${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).absolutePath}")
        Log.d(TAG, "getExternalFilesDir: ${context!!.getExternalFilesDir(Environment.DIRECTORY_MUSIC).absolutePath}")
        Log.d(TAG, "externalCacheDir: ${context!!.externalCacheDir.absolutePath}")
        Log.d(TAG, "filesDir: ${context!!.filesDir.absolutePath}")
        Log.d(TAG, "cacheDir: ${context!!.cacheDir.absolutePath}")
    }
}