package com.rod.uidemo.ext

import android.app.Activity

/**
 *
 * @author Rod
 * @date 2018/9/9
 */

fun Activity.getIntentString(key: String) = intent.getStringExtra(key)