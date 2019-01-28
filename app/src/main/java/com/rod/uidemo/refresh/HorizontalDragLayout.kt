package com.rod.uidemo.refresh

import android.content.Context
import android.graphics.*
import android.os.Build
import android.support.v4.view.NestedScrollingParent2
import android.support.v4.view.ViewCompat
import android.text.TextPaint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.View.MeasureSpec.*
import android.view.ViewGroup
import com.rod.uidemo.R
import com.rod.uidemo.UL
import com.scwang.smartrefresh.layout.util.DensityUtil



/**
 * @author Rod
 * @date 2019/1/11
 */
class HorizontalDragLayout
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : ViewGroup(context, attrs, defStyleAttr), NestedScrollingParent2 {

    companion object {
        private const val TAG = "HorizontalDragLayout"
        private var MAX_OFFSET = DensityUtil.dp2px(80F)
    }

    private val mMeasureInfo = MeasureInfo()
    private var mOffset = 0F
    private var mRealOffset = 0F
    private val mTouchInfo = TouchInfo()
    private val mTextPaint = TextPaint()
    private val mBgPaint = Paint()
    private var mText = "继续左滑刷新"
    private var mIconBmp: Bitmap?

    private val mTextBounds: Rect = Rect()

    init {
        mTextPaint.color = Color.BLACK
        mTextPaint.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14F, resources.displayMetrics)
        mTextPaint.isAntiAlias = true;

        mBgPaint.isAntiAlias = true
        mBgPaint.color = Color.YELLOW
        mIconBmp = getBitmap(context, R.mipmap.ic_launcher)
    }

    private fun getBitmap(context: Context, vectorDrawableId: Int): Bitmap? {
        var bitmap: Bitmap? = null
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            val vectorDrawable = context.getDrawable(vectorDrawableId)
            bitmap = Bitmap.createBitmap(vectorDrawable!!.intrinsicWidth,
                    vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_4444)
            val canvas = Canvas(bitmap)
            vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
            vectorDrawable.draw(canvas)
        } else {
            bitmap = BitmapFactory.decodeResource(context.resources, vectorDrawableId)
        }
        return bitmap
    }

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
        mRealOffset = 0F
        mMeasureInfo.mChildView?.translationX = mOffset
        invalidate()
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
                var tOffset = realOffset
                realOffset = if (realOffset > MAX_OFFSET / 2) {
                    mText = "松开刷新"
                    MAX_OFFSET / 2F
                } else {
                    mText = "继续左滑刷新"
                    realOffset
                }
                mRealOffset = realOffset
                mMeasureInfo.mChildView?.translationX = -tOffset
                invalidate()
            }
        }
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int,
                                   consumed: IntArray, type: Int) {
        UL.d(TAG, "onNestedPreScroll")
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        UL.d(TAG, "onInterceptTouchEvent")
        requestDisallowInterceptTouchEvent(mTouchInfo.consumed)
        return super.onInterceptTouchEvent(ev)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        UL.d(TAG, "dispatchTouchEvent")
        if (!mTouchInfo.consumed) {
            when (ev.action) {
                MotionEvent.ACTION_DOWN -> {
                    mTouchInfo.downX = ev.x
                    mTouchInfo.downY = ev.y
                    mTouchInfo.consumed = false
                }
                MotionEvent.ACTION_MOVE -> {
                    mTouchInfo.consumed = Math.abs(ev.x - mTouchInfo.downX) > Math.abs(ev.y - mTouchInfo.downY)
                }
            }
        }
        when (ev.action) {
            MotionEvent.ACTION_CANCEL,
            MotionEvent.ACTION_UP -> {
                requestDisallowInterceptTouchEvent(false)
                mTouchInfo.consumed = false
            }
        }
        return super.dispatchTouchEvent(ev)
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
            childView.layout(0, 0, measuredWidth, measuredHeight)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawIcon(canvas)
        drawBg(canvas)
        drawText(canvas)
    }

    private fun drawIcon(canvas: Canvas) {
        val bmp = mIconBmp
        bmp?.let {
            val left = (measuredWidth - mRealOffset - 30).toFloat()
            val top = (measuredHeight - bmp.height) / 2F
            canvas.drawBitmap(mIconBmp, left, top, mBgPaint)
        }
    }

    private fun drawBg(canvas: Canvas) {
        val oval = RectF((measuredWidth - mRealOffset).toFloat(), y, (measuredWidth + mRealOffset).toFloat(), measuredHeight.toFloat())
        canvas.drawArc(oval, -90F, -180F, true, mBgPaint);
    }

    private fun drawText(canvas: Canvas) {
        val rect = mTextBounds
        mTextPaint.getTextBounds(mText, 0, mText.length, rect)

        val fontMetrics = mTextPaint.fontMetrics
        val textTopMargin = (measuredHeight - rect.height() * mText.length) / 2F
        // 计算文字baseline
        val fontHeight = fontMetrics.bottom - fontMetrics.top
        var textBaseY = rect.height() - (rect.height() - fontHeight) / 2 - fontMetrics.bottom + textTopMargin

        var startX = measuredWidth - mRealOffset / 2
        if (startX < measuredWidth - 20 - rect.width() / mText.length.toFloat()) {
            startX = measuredWidth - 20 - rect.width() / mText.length.toFloat()
        }
        mText.forEach {
            canvas.drawText(it.toString(), startX, textBaseY, mTextPaint);
            textBaseY += rect.height()
        }
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

    private data class TouchInfo(var downX: Float = 0F, var downY: Float = 0F, var consumed: Boolean = false)
}
