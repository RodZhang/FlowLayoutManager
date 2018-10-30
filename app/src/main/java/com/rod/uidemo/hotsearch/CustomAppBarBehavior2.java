package com.rod.uidemo.hotsearch;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnticipateInterpolator;

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
    private int mTargetViewHeight;
    private int mTotalDy;
    private int mLastBottom;
    private boolean isAnimate;
    private ValueAnimator mAnimator;
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
                mTargetViewHeight = mTargetView.getHeight();
                mParentHeight = abl.getHeight();
            }
        }
        return layoutResult;
    }

    /**
     * 当CoordinatorLayout的子View尝试发起嵌套滚动时调用
     *
     * @param parent            父布局CoordinatorLayout
     * @param child             使用此Behavior的AppBarLayout
     * @param directTargetChild CoordinatorLayout的子View，或者是包含嵌套滚动操作的目标View
     * @param target            发起嵌套滚动的目标View(即AppBarLayout下面的ScrollView或RecyclerView)
     * @param nestedScrollAxes  嵌套滚动的方向
     * @return 返回true表示接受滚动
     */
    @Override
    public boolean onStartNestedScroll(CoordinatorLayout parent, AppBarLayout child, View directTargetChild, View target, int nestedScrollAxes, int type) {
        return super.onStartNestedScroll(parent, child, directTargetChild, target, nestedScrollAxes, type);
    }

    /**
     * 当嵌套滚动已由CoordinatorLayout接受时调用
     *
     * @param coordinatorLayout 父布局CoordinatorLayout
     * @param child             使用此Behavior的AppBarLayout
     * @param directTargetChild CoordinatorLayout的子View，或者是包含嵌套滚动操作的目标View
     * @param target            发起嵌套滚动的目标View(即AppBarLayout下面的ScrollView或RecyclerView)
     * @param nestedScrollAxes  嵌套滚动的方向
     */
    @Override
    public void onNestedScrollAccepted(CoordinatorLayout coordinatorLayout, AppBarLayout child, View directTargetChild, View target, int nestedScrollAxes) {
        super.onNestedScrollAccepted(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }

    /**
     * 当准备开始嵌套滚动时调用
     *
     * @param coordinatorLayout 父布局CoordinatorLayout
     * @param child             使用此Behavior的AppBarLayout
     * @param target            发起嵌套滚动的目标View(即AppBarLayout下面的ScrollView或RecyclerView)
     * @param dx                用户在水平方向上滑动的像素数
     * @param dy                用户在垂直方向上滑动的像素数
     * @param consumed          输出参数，consumed[0]为水平方向应该消耗的距离，consumed[1]为垂直方向应该消耗的距离
     */
    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout,
                                  AppBarLayout child, View target,
                                  int dx, int dy, int[] consumed, int type) {
        //type==1时处于非惯性滑动
        if (type == 1) {
            isFlinging = false;
        }
        // 是向下滑动，dy<0表示向下滑动
        // AppBarLayout已经完全展开，child.getBottom() >= mParentHeight
        if (dy < 0 && child.getBottom() >= mParentHeight) {
            // 累加垂直方向上滑动的像素数
            mTotalDy += -dy * getResistance(Math.abs(dy));
            // 不能大于最大滑动距离
            mTotalDy = Math.min(mTotalDy, TARGET_HEIGHT);
            // 修改AppBarLayout的高度
            mTargetView.setTranslationY(mTotalDy);
            child.setBottom(mParentHeight + mTotalDy);
        } else if (dy > 0 && child.getBottom() > mParentHeight) {
            mTotalDy -= dy;
            mTotalDy = Math.max(0, mTotalDy);
            mTargetView.setTranslationY(mTotalDy);
            child.setBottom(mParentHeight + mTotalDy);
        } else {
            super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
        }
    }

    private float getResistance(int dy) {
        return 1;
//        if (mTotalDy + dy > TARGET_HEIGHT) {
//            dy = TARGET_HEIGHT - mTotalDy;
//        }
//        return 1 - (mTotalDy + dy) * 1F / TARGET_HEIGHT;
    }

    /**
     * 嵌套滚动时调用
     *
     * @param coordinatorLayout 父布局CoordinatorLayout
     * @param child             使用此Behavior的AppBarLayout
     * @param target            发起嵌套滚动的目标View(即AppBarLayout下面的ScrollView或RecyclerView)
     * @param dxConsumed        由目标View滚动操作消耗的水平像素数
     * @param dyConsumed        由目标View滚动操作消耗的垂直像素数
     * @param dxUnconsumed      由用户请求但是目标View滚动操作未消耗的水平像素数
     * @param dyUnconsumed      由用户请求但是目标View滚动操作未消耗的垂直像素数
     */
    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
        UL.Companion.d(TAG, "onNestedScroll mTotalDy=%d", mTotalDy);
    }

    /**
     * 当嵌套滚动的子View准备快速滚动时调用
     *
     * @param coordinatorLayout 父布局CoordinatorLayout
     * @param child             使用此Behavior的AppBarLayout
     * @param target            发起嵌套滚动的目标View(即AppBarLayout下面的ScrollView或RecyclerView)
     * @param velocityX         水平方向的速度
     * @param velocityY         垂直方向的速度
     * @return 如果Behavior消耗了快速滚动返回true
     */
    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, float velocityX, float velocityY) {
        UL.Companion.d(TAG, "onNestedPreFling mTotalDy=%d", mTotalDy);
        return super.onNestedPreFling(coordinatorLayout, child, target, velocityX, velocityY);
    }

    /**
     * 当嵌套滚动的子View快速滚动时调用
     *
     * @param coordinatorLayout 父布局CoordinatorLayout
     * @param child             使用此Behavior的AppBarLayout
     * @param target            发起嵌套滚动的目标View(即AppBarLayout下面的ScrollView或RecyclerView)
     * @param velocityX         水平方向的速度
     * @param velocityY         垂直方向的速度
     * @param consumed          如果嵌套的子View消耗了快速滚动则为true
     * @return 如果Behavior消耗了快速滚动返回true
     */
    @Override
    public boolean onNestedFling(CoordinatorLayout coordinatorLayout,
                                 AppBarLayout child, View target,
                                 float velocityX, float velocityY,
                                 boolean consumed) {
        UL.Companion.d(TAG, "onNestedFling mTotalDy=%d", mTotalDy);

        isFlinging = true;
        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
    }

    /**
     * 当停止滚动时调用
     *
     * @param coordinatorLayout 父布局CoordinatorLayout
     * @param abl               使用此Behavior的AppBarLayout
     * @param target            发起嵌套滚动的目标View(即AppBarLayout下面的ScrollView或RecyclerView)
     */
    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout abl, View target, int type) {
        //如果不是惯性滑动,让他可以执行紧贴操作
        if (!isFlinging) {
            UL.Companion.d(TAG, "onStopNestedScroll mTotalDy=%d", mTotalDy);
            recovery(abl);
            super.onStopNestedScroll(coordinatorLayout, abl, target, type);
        }
    }

    private void recovery(final AppBarLayout abl) {
        UL.Companion.d(TAG, "recovery, isAnimate=%b, mTotalDy=%d", isAnimate, mTotalDy);
        if (isAnimate) {
            return;
        }
        if (mTotalDy > 0) {
            isAnimate = true;
            mAnimator = ValueAnimator.ofFloat(mTotalDy, 0).setDuration(200);
            mAnimator.addUpdateListener(animation -> {
                float value = (float) animation.getAnimatedValue();
                mTotalDy -= value;
                mTargetView.setTranslationY(value);
                abl.setBottom((int) (mParentHeight + value));
            });
            mAnimator.addListener(new AnimatorListenerAdapter() {

                @Override
                public void onAnimationCancel(Animator animation) {
                    isAnimate = false;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    isAnimate = false;
                    mTotalDy = 0;
                }
            });
            mAnimator.setInterpolator(new AnticipateInterpolator());
            mAnimator.start();
        }
    }
}
