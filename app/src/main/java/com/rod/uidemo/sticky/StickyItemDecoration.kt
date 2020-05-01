package com.rod.uidemo.sticky

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.support.v7.widget.RecyclerView
import android.view.View
import java.util.*

/**
 *
 * @author Rod
 * @date 2020/5/1
 */
class StickyItemDecoration(private val stickyCallback: StickyCallback) : RecyclerView.ItemDecoration() {
    companion object {
        private const val POS_DEFAULT = -1;
    }

    private var curStickyPos = POS_DEFAULT
    private var curBitmap: Bitmap? = null

    private val stickyStack = Stack<Pair<Int, Bitmap>>()

    private val paint = Paint()

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val firstStickyViewPos = getFirstStickyPos(parent)
        if (firstStickyViewPos < 0) {
            return
        }
        val firstStickyView = parent.getChildAt(firstStickyViewPos)
        val firstStickyViewAdapterPos = getAdapterPos(parent, firstStickyViewPos)
        var top = 0F
        if (firstStickyView.top <= 0) {
            if (curStickyPos != firstStickyViewAdapterPos) {
                if (curBitmap != null) {
                    stickyStack.push(Pair(curStickyPos, curBitmap!!))
                }
                curStickyPos = firstStickyViewAdapterPos
                curBitmap = getViewBitmap(firstStickyView)
            }
        } else {
            if (curStickyPos == firstStickyViewAdapterPos && !stickyStack.empty()) {
                with(stickyStack.pop()) {
                    curStickyPos = first
                    curBitmap = second
                }
            }
            if (curBitmap != null) {
                top = (firstStickyView.top - curBitmap!!.height).toFloat()
                if (top > 0) {
                    top = 0F
                }
            }
        }
        if (curBitmap != null) {
            c.drawBitmap(curBitmap, 0F, top, paint)
        }
    }

    private fun getFirstStickyPos(parent: RecyclerView): Int {
        val callback = stickyCallback
        for (pos in 0..parent.childCount) {
            if (callback.isSticky(getAdapterPos(parent, pos))) {
                return pos
            }
        }
        return POS_DEFAULT
    }

    private fun getViewBitmap(view: View): Bitmap {
        view.isDrawingCacheEnabled = true
        val bitmap = Bitmap.createBitmap(view.drawingCache)
        //如果不调用这个方法，每次生成的bitmap相同
        view.isDrawingCacheEnabled = false
        return bitmap
    }

    private fun getAdapterPos(parent: RecyclerView, layoutPos: Int): Int {
        return parent.layoutManager.getPosition(parent.getChildAt(layoutPos))
    }

    interface StickyCallback {
        fun isSticky(adapterPos: Int): Boolean
    }
}