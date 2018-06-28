package com.rod.flowlayoutmanager.flow

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * No pains, no gains.
 *
 * Created by Rod on 2018/6/28.
 */
class MyItemDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
        outRect?.let {
            outRect.left = 20
            outRect.right = 20
            outRect.top = 40
        }
    }
}