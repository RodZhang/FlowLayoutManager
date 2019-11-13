package com.rod.uidemo.sweepback

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout

/**
 *
 * @author Rod
 * @date 2019/5/11
 */
class SweepBackLayout
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0)
    : FrameLayout(context, attrs, defStyle) {

    private val mMaxX = 100F

    private var mDownX = 0F
    private var mDownY = 0F
    private var mLastX = 0F
    private var mLastY = 0F

    private val mPaint = Paint()

    init {
        setWillNotDraw(false)
        mPaint.color = Color.parseColor("#FF0000")
        mPaint.strokeWidth = 6F
        mPaint.style = Paint.Style.STROKE
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return ev.x <= mMaxX && Math.abs(ev.x - mDownX) > Math.abs(ev.y - mDownY)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                mLastX = ev.x
                mLastY = ev.y
                mDownX = mLastX
                mDownY = mLastY
                return super.dispatchTouchEvent(ev)
            }
            MotionEvent.ACTION_MOVE -> {
                if (mDownX > mMaxX) {
                    return super.dispatchTouchEvent(ev)
                }
                onInterceptTouchEvent(ev)
            }
        }

        return super.dispatchTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                translationX = Math.max(event.x - mDownX + translationX, 0F)
            }
            MotionEvent.ACTION_UP,
            MotionEvent.ACTION_CANCEL -> {
                translationX = 0F
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawLine(mMaxX, 0F, mMaxX, height.toFloat(), mPaint)
    }
}