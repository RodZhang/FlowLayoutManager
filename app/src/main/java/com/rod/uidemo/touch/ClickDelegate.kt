package com.rod.uidemo.touch

import android.view.MotionEvent
import android.view.View

/**
 *
 * @author Rod
 * @date 2018/11/26
 */
class ClickDelegate(val view: View, private val clickListener: OnClickListener) {

    private var mDownX = 0;
    private var mDownY = 0;
    private var mUpX = 0;
    private var mUpY = 0;

    init {
        view.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    mDownX = event.x.toInt()
                    mDownY = event.y.toInt()
                }
                MotionEvent.ACTION_UP -> {
                    mUpX = event.x.toInt()
                    mUpY = event.y.toInt()
                }
            }
            false
        }
        view.setOnClickListener { v -> clickListener.onClick(v, mDownX, mDownY, mUpX, mUpY) }
    }


    interface OnClickListener {
        fun onClick(view: View, dx: Int, dy: Int, ux: Int, uy: Int);
    }
}