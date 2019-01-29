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
        private var MAX_OFFSET = DensityUtil.dp2px(50F)
    }

    private val mMeasureInfo = MeasureInfo()
    private val mTouchInfo = TouchInfo()

    private var mBgIconStartOffset = DensityUtil.dp2px(8F)
    private var mBgIconEndOffset = DensityUtil.dp2px(50F)
    private var mIconBmp: Bitmap?

    private var mArcStartOffset = -DensityUtil.dp2px(4F)
    private var mArcEndOffset = DensityUtil.dp2px(24F)
    private val mCommonPaint = Paint()

    private var mTextMaxRightOffset = DensityUtil.dp2px(4F)
    private var mTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10F, resources.displayMetrics)
    private var mTextColor = Color.parseColor("#888888")
    private var mTextPulling = "滑动更多"
    private var mTextRelease = "松手查看"
    private val mTextBounds: Rect = Rect()

    private var mForeIcon: Bitmap?
    private var mForeIconOffset = DensityUtil.dp2px(2F)

    private val mTextPaint = TextPaint()
    private var mShowText = ""

    private var mTotalOffset = 0F
    private var mRealOffset = 0F
    private var mDragRate = 0F;

    init {
        mTextPaint.isAntiAlias = true;
        mTextPaint.textSize = mTextSize
        mTextPaint.color = mTextColor

        mCommonPaint.isAntiAlias = true
        mCommonPaint.color = Color.parseColor("#fff6d1")
        mIconBmp = getBitmap(context, R.mipmap.ic_hy_head)
        mForeIcon = getBitmap(context, R.mipmap.ic_hy_hands)
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
        mTotalOffset = 0F
        mRealOffset = 0F
        mDragRate = 0F
        mMeasureInfo.mChildView?.translationX = 0F
        invalidate()
    }

    override fun onNestedScroll(target: View, dxConsumed: Int, dyConsumed: Int,
                                dxUnconsumed: Int, dyUnconsumed: Int, type: Int) {
        UL.d(TAG, "onNestedScroll, dxConsumed=$dxConsumed, dxUnconsumed=$dxUnconsumed, type=$type")

        if (type == ViewCompat.TYPE_TOUCH) {
            if (dxUnconsumed > 0) {
                if (mRealOffset >= MAX_OFFSET) {
                    return
                }
                mTotalOffset += dxUnconsumed
            } else if (mTotalOffset > 0) {
                mTotalOffset += dxConsumed
            }
            if (mTotalOffset != 0F) {
                var realOffset = mTotalOffset * 0.5F
                realOffset = if (realOffset > MAX_OFFSET) {
                    mShowText = mTextRelease
                    MAX_OFFSET.toFloat()
                } else {
                    mShowText = mTextPulling
                    realOffset
                }
                mRealOffset = realOffset
                mDragRate = mRealOffset / MAX_OFFSET
                mMeasureInfo.mChildView?.translationX = -realOffset
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
        drawForegroundIcon(canvas)
        drawText(canvas)
    }

    private fun drawIcon(canvas: Canvas) {
        val bmp = mIconBmp
        bmp?.let {
            val offset = mBgIconStartOffset + (mBgIconEndOffset - mBgIconStartOffset) * mDragRate
            val left = (measuredWidth - offset).toFloat()
            val top = (measuredHeight - bmp.height) / 2F
            canvas.drawBitmap(bmp, left, top, mCommonPaint)
        }
    }

    private fun drawBg(canvas: Canvas) {
        val offset = mArcStartOffset + (mArcEndOffset - mArcStartOffset) * mDragRate
        if (offset <= 0) {
            return
        }
        val oval = RectF((measuredWidth - offset).toFloat(), y, (measuredWidth + offset).toFloat(), measuredHeight.toFloat())
        canvas.drawArc(oval, -90F, -180F, true, mCommonPaint);
    }

    private fun drawForegroundIcon(canvas: Canvas) {
        val bmp = mForeIcon
        bmp?.let {
            val offset = mArcStartOffset + (mArcEndOffset - mArcStartOffset) * mDragRate
            val left = (measuredWidth - offset - bmp.width / 4).toFloat()
            val top = (measuredHeight - bmp.height) / 2F
            canvas.drawBitmap(bmp, left, top, mCommonPaint)
        }
    }

    private fun drawText(canvas: Canvas) {
        val arcOffset = mArcStartOffset + (mArcEndOffset - mArcStartOffset) * mDragRate
        if (arcOffset <= 0) {
            return
        }

        val rect = mTextBounds
        mTextPaint.getTextBounds(mShowText, 0, mShowText.length, rect)

        val fontMetrics = mTextPaint.fontMetrics
        val textTopMargin = (measuredHeight - rect.height() * mShowText.length) / 2F
        // 计算文字baseline
        val fontHeight = fontMetrics.bottom - fontMetrics.top
        var textBaseY = rect.height() - (rect.height() - fontHeight) / 2 - fontMetrics.bottom + textTopMargin

        val singleTextWidth = rect.width() / mShowText.length.toFloat()
        val endOffset = measuredWidth - (singleTextWidth + mTextMaxRightOffset)

        var startX = measuredWidth - arcOffset + (mArcEndOffset - singleTextWidth) / 2

        if (startX < endOffset) {
            startX = endOffset
        }
        mShowText.forEach {
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
