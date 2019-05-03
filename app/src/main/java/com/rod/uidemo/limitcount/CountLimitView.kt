package com.rod.uidemo.limitcount

import android.content.Context
import android.graphics.*
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.animation.CycleInterpolator
import android.view.animation.TranslateAnimation
import android.widget.EditText

/**
 *
 * @author Rod
 * @date 2019/5/2
 */
class CountLimitView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0
) : View(context, attrs, defStyle), TextWatcher {

    var mCurCount = 0
    var mMaxCount = 6
    var mInitColor = Color.parseColor("#333333")
    var mFinalColor = Color.parseColor("#FF938B")

    var mTextSize = 20F
    var mCycleRadius = 40
    var mTextPadding = 24
    var mStrokeWidth = 10F

    var mShowTextIfZero = false

    private val mRect = Rect()
    private val mRectF = RectF();

    private val mTextPaint = Paint().apply {
        isDither = true
        isAntiAlias = true
        textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, mTextSize, resources.displayMetrics)
        color = mInitColor
    }

    private val mProgressPaint = Paint().apply {
        isDither = true
        isAntiAlias = true
        color = mInitColor
        strokeWidth = mStrokeWidth
    }

    private var mAnchorEditText: EditText? = null

    fun setAnchorEditText(editText: EditText?) {
        if (mAnchorEditText == editText) {
            return
        }
        mAnchorEditText?.removeTextChangedListener(this)
        mAnchorEditText = editText
        mAnchorEditText?.addTextChangedListener(this)
        refresh()
    }

    override fun afterTextChanged(s: Editable?) {
        refresh()
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    private fun refresh() {
        val newCount = mAnchorEditText?.text?.length ?: 0
        if (mCurCount != newCount) {
            mCurCount = newCount
            requestLayout()
            triggerAnimation()
        }
    }

    private fun triggerAnimation() {
        if (mCurCount < mMaxCount) {
            return
        }
        animation?.cancel()
        animation = TranslateAnimation(0F, 5F, 0F, 0F).apply {
            interpolator = CycleInterpolator(20F)
            duration = 200
            start()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val realWidthSpec = measureWidth(widthMeasureSpec)
        val realHeightSpec = measureHeight(heightMeasureSpec)
        setMeasuredDimension(realWidthSpec, realHeightSpec)
    }

    private fun measureWidth(widthMeasureSpec: Int): Int {
        val mode = MeasureSpec.getMode(widthMeasureSpec)
        return if (mode == MeasureSpec.EXACTLY || mode == MeasureSpec.UNSPECIFIED) {
            widthMeasureSpec
        } else {
            var contentWidth = mCycleRadius * 2
            if (withText()) {
                contentWidth += mTextPadding + mTextPaint.measureText(mCurCount.toString()).toInt()
            }
            MeasureSpec.makeMeasureSpec(paddingLeft + paddingRight + contentWidth + mStrokeWidth.toInt(), mode)
        }
    }

    private fun measureHeight(heightMeasureSpec: Int): Int {
        val mode = MeasureSpec.getMode(heightMeasureSpec)
        return if (mode == MeasureSpec.EXACTLY || mode == MeasureSpec.UNSPECIFIED) {
            heightMeasureSpec
        } else {
            val text = mCurCount.toString()
            mTextPaint.getTextBounds(text, 0, text.length, mRect)
            return Math.max(mCycleRadius * 2 + mStrokeWidth.toInt(), mRect.height()) + paddingTop + paddingBottom
        }
    }

    private fun withText() = mCurCount > 0 || mShowTextIfZero

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        mRectF.set((paddingLeft + mStrokeWidth / 2).toFloat(), (paddingTop + mStrokeWidth / 2).toFloat(), (paddingLeft + mCycleRadius * 2 + mStrokeWidth / 2).toFloat(), (paddingTop + mCycleRadius * 2 + mStrokeWidth / 2).toFloat());
        if (mCurCount < mMaxCount) {
            mTextPaint.color = mInitColor
            mProgressPaint.color = mInitColor
            mProgressPaint.strokeWidth = mStrokeWidth
            mProgressPaint.style = Paint.Style.STROKE
            canvas.drawOval(mRectF, mProgressPaint)

            val sweepAngle = (mCurCount.toFloat() / mMaxCount) * 360
            if (sweepAngle > 0) {
                mProgressPaint.style = Paint.Style.FILL_AND_STROKE
                mProgressPaint.strokeWidth = 1F
                canvas.drawArc(mRectF, 270F, sweepAngle, true, mProgressPaint)
            }
        } else {
            mTextPaint.color = mFinalColor
            mProgressPaint.strokeWidth = mStrokeWidth
            mProgressPaint.style = Paint.Style.FILL_AND_STROKE
            mProgressPaint.color = mFinalColor
            canvas.drawOval(mRectF, mProgressPaint)
        }

        if (withText()) {
            val fontMetrics = mTextPaint.fontMetrics
            val textHeight = fontMetrics.bottom - fontMetrics.top
            val center = (paddingTop + mStrokeWidth / 2).toFloat() + mCycleRadius
            val top = center - textHeight / 2
            val baseLine = top - fontMetrics.top
//            val ascent = baseLine + fontMetrics.ascent
//            val descent = baseLine + fontMetrics.descent
//            val bottom = center + textHeight / 2
//
//
//            val xEnd = (measuredWidth - paddingRight).toFloat()
//            canvas.drawLine(paddingLeft.toFloat(), top, xEnd, top, mTextPaint)
//            canvas.drawLine(paddingLeft.toFloat(), ascent, xEnd, ascent, mTextPaint)
//            canvas.drawLine(paddingLeft.toFloat(), baseLine, xEnd, baseLine, mTextPaint)
//            canvas.drawLine(paddingLeft.toFloat(), descent, xEnd, descent, mTextPaint)
//            canvas.drawLine(paddingLeft.toFloat(), bottom, xEnd, bottom, mTextPaint)
            canvas.drawText(mCurCount.toString(), (paddingLeft + mCycleRadius * 2 + mTextPadding).toFloat(), baseLine, mTextPaint)
        }
    }
}