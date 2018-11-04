package com.rod.uidemo.hotsearch;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.rod.uidemo.UL;

/**
 * @author Rod
 * @date 2018/10/30
 */
public class CustomAppBarBehavior2 extends AppBarLayout.Behavior {
    private static final String TAG = "CustomAppBarBehavior2";
    private static final int TARGET_HEIGHT = 300;
    private static final String TARGET_VIEW_TAG = "overScroll";

    private View mTargetView;
    private int mParentHeight;
    /**
     * 是否处于惯性滑动状态
     */
    private boolean isFlinging = false;

    public CustomAppBarBehavior2() {
    }

    public CustomAppBarBehavior2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * AppBarLayout布局时调用
     *
     * @param parent          父布局CoordinatorLayout
     * @param abl             使用此Behavior的AppBarLayout
     * @param layoutDirection 布局方向
     * @return 返回true表示子View重新布局，返回false表示请求默认布局
     */
    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, AppBarLayout abl, int layoutDirection) {
        boolean layoutResult = super.onLayoutChild(parent, abl, layoutDirection);
        if (mTargetView == null) {
            mTargetView = parent.findViewWithTag(TARGET_VIEW_TAG);
            if (mTargetView != null) {
                abl.setClipChildren(false);
                mParentHeight = abl.getHeight();
            }
        }
        return layoutResult;
    }

    private float mDownY;

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, AppBarLayout child, MotionEvent ev) {
        if (child.getBottom() == mParentHeight - child.getTotalScrollRange()) {
            return super.onInterceptTouchEvent(parent, child, ev);
        }
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            mDownY = ev.getY();
            return super.onInterceptTouchEvent(parent, child, ev);
        }

        float dy = 0;
        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            dy = ev.getY() - mDownY;
            mDownY = ev.getY();
        }

        UL.Companion.d(TAG, "onInterceptTouchEvent dy=%f, mDownY=%f", dy, mDownY);
        if (dy + child.getBottom() > mParentHeight) {
            return true;
        } else {
            return super.onInterceptTouchEvent(parent, child, ev);
        }
    }

    @Override
    public boolean onTouchEvent(CoordinatorLayout parent, AppBarLayout child, MotionEvent ev) {
        if (mTargetView != null) {
            float dy = 0;
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mDownY = ev.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mDownY != 0) {
                        dy = ev.getY() - mDownY;
                    }
                    mDownY = ev.getY();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                default:
                    if (child.getBottom() > mParentHeight) {
                        mTargetView.setTranslationY(0);
                        child.setBottom(mParentHeight);
                    }
                    break;
            }

            UL.Companion.d(TAG, "onTouchEvent, dy=%f, mDownY=%f", dy, mDownY);
            if (dy + child.getBottom() > mParentHeight) {
                mTargetView.setTranslationY(mTargetView.getTranslationY() + dy);
                child.setBottom((int) (child.getBottom() + dy));
                return true;
            } else {
                parent.requestDisallowInterceptTouchEvent(false);
                return super.onTouchEvent(parent, child, ev);
            }
        } else {
            return super.onTouchEvent(parent, child, ev);
        }
    }

}
