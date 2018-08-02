package com.rod.uidemo.flow;

import com.rod.uidemo.UL;

/**
 * @author Rod
 * @date 2018/8/2
 */
final class LayoutState {
    private static final String TAG = "LayoutState";
    /**
     * 从上往下布局（手指往下滑动）
     */
    static final int LAYOUT_TO_TAIL = 1;
    /**
     * 从下往上布局（手指往上滑动）
     */
    static final int LAYOUT_TO_HEAD = -1;

    /**
     * layout的方向
     */
    int mLayoutDirection;
    /**
     * 当前item在adapter中的下标
     */
    int mCurrentItemPos;
    /**
     * 当前滑动的偏移量
     */
    int mScrollOffset;
    int mXOffset;
    int mYOffset;
    /**
     * 当次滑动的距离
     */
    int mAbsDy;
    /**
     * 是否需要回收view，当滑动时要回收被滑出屏幕的view
     */
    boolean mNeedRecycle;
    int mLeftBounds;
    int mTopBounds;
    int mRightBounds;
    int mBottomBounds;

    void updateBounds(int left, int top, int right, int bottom) {
        UL.Companion.d(TAG, "updateBounds, left=%d, top=%d, right=%d, bottom=%d", left, top, right, bottom);
        mLeftBounds = left;
        mTopBounds = top;
        mRightBounds = right;
        mBottomBounds = bottom;
    }
}
