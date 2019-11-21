package com.rod.uidemo.testtouch

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.widget.FrameLayout

/**
 *
 * @author Rod
 * @date 2019/4/8
 */
class TestTouchItemView
@JvmOverloads constructor(context: Context, attributes: AttributeSet? = null, style: Int = 0) :
        FrameLayout(context, attributes, style) {
    private var mPointer1: MotionEvent.PointerCoords? = null
    private var mPointer2: MotionEvent.PointerCoords? = null

    companion object {
        const val TAG = "TestTouchItemView"
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val result = super.dispatchTouchEvent(ev)
        Log.d(TAG, "dispatchTouchEvent: $result, ev:$ev")
        return result
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val result = super.onTouchEvent(event)
        Log.d(TAG, "onTouchEvent, result=$result event=$event")
        return result
    }
}