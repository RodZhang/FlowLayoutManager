package com.rod.uidemo.testtouch

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.FrameLayout

/**
 *
 * @author Rod
 * @date 2019/4/8
 */
class TestTouchView
@JvmOverloads constructor(context: Context, attributes: AttributeSet? = null, style: Int = 0)
    : FrameLayout(context, attributes, style) {
    private var mPointer1: MotionEvent.PointerCoords? = null
    private var mPointer2: MotionEvent.PointerCoords? = null

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val result = super.onInterceptTouchEvent(ev)
        Log.d("TestTouchView", "onInterceptTouchEvent: $result")
        return result
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val result = super.dispatchTouchEvent(ev)
        Log.d("TestTouchView", "dispatchTouchEvent: $result")
        return result
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                Log.d("TestTouchView", "onTouchEvent, ACTION_DOWN")
                mPointer1 = MotionEvent.PointerCoords()
                event.getPointerCoords(0, mPointer1)
                if (event.pointerCount >= 2) {
                    mPointer2 = MotionEvent.PointerCoords()
                    event.getPointerCoords(1, mPointer2)
                    return true;
                }
            }
            MotionEvent.ACTION_MOVE -> {
                val pointer1 = MotionEvent.PointerCoords()
                event.getPointerCoords(0, pointer1)
                if (event.pointerCount >= 2) {
                    val pointer2 = MotionEvent.PointerCoords()
                    event.getPointerCoords(1, pointer2)
                    if (mPointer1 == null || mPointer2 == null) {
                        mPointer1 = pointer1
                        mPointer2 = pointer2
                        return true
                    }
                    val dx = pointer1.x - mPointer1!!.x
                    val dy = pointer2.y - mPointer2!!.y

                    layoutParams.width = measuredWidth - dx.toInt()
                    layoutParams.height = measuredHeight - dy.toInt()
                    Log.i("TestScale", "dx=$dx, dy=$dy, width=${layoutParams.width}, height=${layoutParams.height}")
                    requestLayout()

                    mPointer1 = pointer1
                    mPointer2 = pointer2
                    return true;
                }
                val dx = pointer1.x - mPointer1!!.x
                if (Math.abs(dx) >= 3) {
                    translationX += dx
                }
                val dy = pointer1.y - mPointer1!!.y
                if (Math.abs(dy) >= 3) {
                    translationY += dy
                }
                Log.i("TestTouchView", "dx=$dx, dy=$dy")
                mPointer1 = pointer1;
            }
            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_CANCEL -> {
                mPointer1 = null
                mPointer2 = null
            }
        }
        val result = super.onTouchEvent(event)
        Log.d("TestTouchView", "onTouchEvent: $result")
        return true
    }
}