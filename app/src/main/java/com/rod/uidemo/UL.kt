package com.rod.uidemo

import android.util.Log

/**
 * @author Rod
 * @date 2018/7/29
 */
class UL {

    companion object {
        fun d(tag: String, format: String, vararg args: Any) {
            Log.d(tag, String.format(format, *args))
        }
    }
}