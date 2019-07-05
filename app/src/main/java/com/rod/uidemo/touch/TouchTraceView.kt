package com.rod.uidemo.touch

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.LinearLayout

/**
 *
 * @author Rod
 * @date 2019/6/18
 */
class TouchTraceView
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, style: Int = 0)
    : LinearLayout(context, attrs, style) {

    var mTag: String = ""

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        Log.d("TouchTraceView", "$mTag, dispatchTouchEvent->ev.action=${ev.action}")
        return super.dispatchTouchEvent(ev)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        Log.d("TouchTraceView", "$mTag, onInterceptTouchEvent->ev.action=${ev.action}")
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        Log.d("TouchTraceView", "$mTag, onTouchEvent->ev.action=${ev.action}")
        return super.onTouchEvent(ev)
    }
}