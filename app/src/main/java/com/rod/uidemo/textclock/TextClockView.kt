package com.rod.uidemo.textclock

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import java.text.SimpleDateFormat
import java.util.*

/**
 *
 * @author Rod
 * @date 2019/8/14
 */
class TextClockView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defaultStyle: Int = 0
) : View(context, attrs, defaultStyle) {

    companion object {
        val FORMAT_CENTER_TIME = SimpleDateFormat("HH:mm", Locale.CHINA)
        val FORMAT_CENTER_DATE = SimpleDateFormat("MM.dd E", Locale.CHINA)
    }

    private val mPaint = Paint().apply {
        isAntiAlias = true
    }
    private var mContentWidth = 0F
    private var mContentHeight = 0F
    private var mCenterTextColor = 0xFF000000.toInt()
    private val mRect = Rect()

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        mContentWidth = (measuredWidth - paddingLeft - paddingRight).toFloat()
        mContentHeight = (measuredHeight - paddingTop - paddingBottom).toFloat()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        drawLine(canvas)
        drawCenterDate(canvas)
        drawHours(canvas)
    }

    private fun drawLine(canvas: Canvas?) {
        mPaint.color = 0xffff0000.toInt()
        canvas?.drawLine(
                paddingLeft.toFloat(),
                paddingTop + mContentHeight / 2,
                paddingLeft + mContentWidth,
                paddingTop + mContentHeight / 2,
                mPaint
        )
        mPaint.color = 0xff00ff00.toInt()
        canvas?.drawLine(
                paddingLeft + mContentWidth / 2,
                paddingTop.toFloat(),
                paddingLeft + mContentWidth / 2,
                paddingTop + mContentHeight,
                mPaint
        )
    }

    private fun drawCenterDate(canvas: Canvas?) {
        mPaint.color = mCenterTextColor
        mPaint.textSize = 120F
        mPaint.isFakeBoldText = true

        val curTime = Calendar.getInstance().time
        val time = FORMAT_CENTER_TIME.format(curTime)
        val timeWidth = mPaint.measureText(time)
        val timeX = paddingLeft + mContentWidth / 2 - timeWidth / 2
        val timeY = paddingTop + mContentHeight / 2 - 24
        canvas?.drawText(time, timeX, timeY, mPaint)

        mPaint.textSize = 45F
        mPaint.isFakeBoldText = false

        val date = FORMAT_CENTER_DATE.format(curTime)
        mPaint.getTextBounds(date, 0, date.length, mRect)
        val dateWidth = mRect.width()
        val dateX = paddingLeft + mContentWidth / 2 - dateWidth / 2
        val dateY = paddingTop + mContentHeight / 2 + mRect.height() + 24
        canvas?.drawText(date, dateX, dateY, mPaint)
    }

    private fun drawHours(canvas: Canvas?) {
        canvas?.translate(mContentWidth / 2, mContentHeight / 2)
        canvas?.save()
        for (i in 0 until 12) {
            canvas?.save()
            canvas?.rotate(360 / 12F * i)
            canvas?.drawText("${i + 1} 点", 210F, mPaint.fontSpacing / 2 - 10, mPaint)
            canvas?.restore()
        }
        canvas?.restore()
    }

}