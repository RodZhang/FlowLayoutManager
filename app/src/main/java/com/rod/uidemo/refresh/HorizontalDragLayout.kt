package com.rod.uidemo.refresh

import android.content.Context
import android.graphics.Canvas
import android.support.v4.view.NestedScrollingParent2
import android.support.v4.view.NestedScrollingParentHelper
import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.View.MeasureSpec.*
import android.view.ViewGroup
import android.widget.Scroller
import com.rod.uidemo.UL
import com.scwang.smartrefresh.layout.util.DensityUtil

/**
 * @author Rod
 * @date 2019/1/11
 */
class HorizontalDragLayout @JvmOverloads constructor(context: Context,
                                                     attrs: AttributeSet? = null,
                                                     defStyleAttr: Int = 0)
    : ViewGroup(context, attrs, defStyleAttr), NestedScrollingParent2 {

    companion object {
        private const val TAG = "HorizontalDragLayout"
        private var MAX_OFFSET = DensityUtil.dp2px(80F)
    }

    private val mMeasureInfo = MeasureInfo()
    private var mOffset = 0F
//    private val mScrollHelper = NestedScrollingParentHelper(this)

    override fun onStartNestedScroll(child: View, target: View, axes: Int, type: Int): Boolean {
        UL.d(TAG, "onStartNestedScroll")
        return true
    }

    override fun onNestedScrollAccepted(child: View, target: View, axes: Int, type: Int) {
        UL.d(TAG, "onNestedScrollAccepted")
    }

    override fun onStopNestedScroll(target: View, type: Int) {
        UL.d(TAG, "onStopNestedScroll")
        mOffset = 0F
        mMeasureInfo.mChildView?.translationX = mOffset
    }

    override fun onNestedScroll(target: View, dxConsumed: Int, dyConsumed: Int,
                                dxUnconsumed: Int, dyUnconsumed: Int, type: Int) {
        UL.d(TAG, "onNestedScroll, dxConsumed=$dxConsumed, dxUnconsumed=$dxUnconsumed, type=$type")

        if (type == ViewCompat.TYPE_TOUCH) {
            if (dxUnconsumed > 0) {
                mOffset += dxUnconsumed
            } else if (mOffset > 0) {
                mOffset += dxConsumed
            }
            if (mOffset != 0F) {
                var realOffset = mOffset * 3 / 5
                realOffset = if (realOffset > MAX_OFFSET) MAX_OFFSET.toFloat() else realOffset
                mMeasureInfo.mChildView?.translationX = -realOffset
            }
        }
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int,
                                   consumed: IntArray, type: Int) {
        UL.d(TAG, "onNestedPreScroll")
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        resetMeasureInfo(mMeasureInfo, widthMeasureSpec, heightMeasureSpec)
        UL.d(TAG, "onMeasure start, mMeasureInfo=$mMeasureInfo")

        if (mMeasureInfo.mChildCount == 0) {
            setMeasuredDimension(mMeasureInfo.mWidthSpecSize, mMeasureInfo.mHeightSpecSize)
            return
        }

        measureWidth(mMeasureInfo)
        setMeasuredDimension(mMeasureInfo.mMeasuredWidthSize, mMeasureInfo.mMeasuredHeightSize)
        UL.d(TAG, "onMeasure end, mMeasureInfo=$mMeasureInfo")
    }

    private fun resetMeasureInfo(measureInfo: MeasureInfo,
                                 widthMeasureSpec: Int, heightMeasureSpec: Int) {
        with(measureInfo) {
            mWidthSpecMode = View.MeasureSpec.getMode(widthMeasureSpec)
            mWidthSpecSize = View.MeasureSpec.getSize(widthMeasureSpec)
            mHeightSpecMode = View.MeasureSpec.getMode(heightMeasureSpec)
            mHeightSpecSize = View.MeasureSpec.getSize(heightMeasureSpec)
            mMeasuredWidthSize = 0
            mMeasuredHeightSize = 0
            mChildCount = childCount
            mChildView = if (mChildCount > 0) getChildAt(0) else null
            mChildWidth = 0
            mChildHeight = 0
            mChildView?.let {
                measureChild(it, widthMeasureSpec, heightMeasureSpec)
                mChildWidth = it.measuredWidth
                mChildHeight = it.measuredHeight
            }
        }
    }

    private fun measureWidth(measureInfo: MeasureInfo) {
        with(measureInfo) {
            when (mWidthSpecMode) {
                EXACTLY -> mMeasuredWidthSize = mWidthSpecSize
                UNSPECIFIED -> mMeasuredWidthSize = mChildWidth
                AT_MOST -> mMeasuredWidthSize = Math.min(mChildWidth, mWidthSpecSize)
            }
            when (mHeightSpecMode) {
                EXACTLY -> mMeasuredHeightSize = mHeightSpecSize
                UNSPECIFIED -> mMeasuredHeightSize = mChildHeight
                AT_MOST -> mMeasuredHeightSize = Math.min(mChildHeight, mHeightSpecSize)
            }
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        val childView = mMeasureInfo.mChildView
        if (changed && childView != null) {
            childView.layout(left, top, right, bottom)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }

    private class MeasureInfo {
        internal var mWidthSpecMode = 0
        internal var mWidthSpecSize = 0
        internal var mHeightSpecMode = 0
        internal var mHeightSpecSize = 0
        internal var mMeasuredWidthSize = 0;
        internal var mMeasuredHeightSize = 0;
        internal var mChildView: View? = null
        internal var mChildCount = 0;
        internal var mChildWidth = 0;
        internal var mChildHeight = 0;

        override fun toString(): String {
            return "MeasureInfo(mWidthSpecMode=$mWidthSpecMode," +
                    " mWidthSpecSize=$mWidthSpecSize," +
                    " mHeightSpecMode=$mHeightSpecMode," +
                    " mHeightSpecSize=$mHeightSpecSize," +
                    " mMeasuredWidthSize=$mMeasuredWidthSize," +
                    " mMeasuredHeightSize=$mMeasuredHeightSize," +
                    " mChildCount=$mChildCount," +
                    " mChildWidth=$mChildWidth," +
                    " mChildHeight=$mChildHeight)"
        }
    }
}
