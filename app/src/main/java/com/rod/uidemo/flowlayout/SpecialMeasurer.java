package com.rod.uidemo.flowlayout;

import android.support.annotation.NonNull;
import android.util.SparseIntArray;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;

import static android.view.View.GONE;

/**
 * @author Rod
 * @date 2018/8/21
 */
public class SpecialMeasurer implements Measurer {

    private int mMaxLineCount = Integer.MAX_VALUE;
    private int mFoldLineCount = Integer.MAX_VALUE;
    private boolean mNeedFold;
    private View mSpecialView;

    private final SparseIntArray mLineHeightMap = new SparseIntArray();

    public SpecialMeasurer(int maxLineCount, int foldLineCount, boolean needFold, View specialView) {
        mMaxLineCount = maxLineCount;
        mFoldLineCount = foldLineCount;
        mNeedFold = needFold;
        mSpecialView = specialView;
    }

    public void setNeedFold(boolean needFold) {
        mNeedFold = needFold;
    }

    @Override
    public int measure(@NonNull FlowLayout.LayoutProperty property) {
        mLineHeightMap.clear();
        if (property.mChildCount == 0) {
            return 0;
        }
        MeasureState state = new MeasureState();
        state.mStartX = property.mXStartPadding;
        state.mParent = property.mParent;
        if (mSpecialView != null && mSpecialView.getParent() != null) {
            state.mParent.removeView(mSpecialView);
        }
        int childCount = state.mParent.getChildCount();

        for (; state.mIndex < childCount; state.mIndex++) {
            final View child = state.mParent.getChildAt(state.mIndex);
            if (state.mLineIndex == mMaxLineCount
                    || (state.mLineIndex == mFoldLineCount && mNeedFold)) {
                child.setVisibility(GONE);
                continue;
            }
            child.setVisibility(View.VISIBLE);
            calculate(child, state, property);
        }
        int lineCount = mLineHeightMap.size();
        int contentHeight = lineCount * property.mPadV - property.mPadV;
        for (int i = 0; i < lineCount; i++) {
            contentHeight += mLineHeightMap.valueAt(i);
        }
        return contentHeight;
    }

    private void calculate(View child, MeasureState state,
                           FlowLayout.LayoutProperty property) {
        child.measure(property.mChildMeasureSpace, property.mChildMeasureSpace);
        state.mLineHeight = Math.max(state.mLineHeight, child.getMeasuredHeight());
        state.mChildWidth = child.getMeasuredWidth();

        if (canShowSpecialView(state.mLineIndex)) {
        }

        if (state.mStartX + state.mChildWidth > property.mXBeforeEnd) {
            onChangeLine(child, state, property);
        } else {
            state.mStartX += state.mChildWidth + property.mPadH;
            mLineHeightMap.put(state.mLineIndex, state.mLineHeight);
        }
    }

    private void onChangeLine(View child, MeasureState state,
                              FlowLayout.LayoutProperty property) {
        state.mLineIndex++;
        mLineHeightMap.put(state.mLineIndex, state.mLineHeight);
        if (canShowSpecialView(state.mLineIndex)) {
            calculateAtFoldLine(child, state, property, true);
            return;
        }

        if (state.mStartX + state.mChildWidth > property.mXBeforeEnd) {

        } else {

        }
    }

    private void calculateAtFoldLine(View child, MeasureState state,
                                     FlowLayout.LayoutProperty property,
                                     boolean isFirst) {
        if (!isFirst) {
            child.setVisibility(GONE);
            state.mParent.addView(mSpecialView, state.mIndex);
            return;
        }
        if (state.mSpecialViewWidth == 0) {
            mSpecialView.measure(
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                    MeasureSpec.makeMeasureSpec(state.mLineHeight, MeasureSpec.EXACTLY));
            state.mSpecialViewWidth = mSpecialView.getMeasuredWidth();
        }
        if (property.mXStartPadding + state.mChildWidth + state.mSpecialViewWidth + property.mPadH
                > property.mXBeforeEnd) {
            state.mChildWidth = property.mXBeforeEnd  - property.mXStartPadding
                    - property.mPadH - state.mSpecialViewWidth;
            child.measure(
                    MeasureSpec.makeMeasureSpec(state.mChildWidth, MeasureSpec.AT_MOST),
                    property.mChildMeasureSpace);
            state.mIndex++;
        }
    }

    private static class MeasureState {
        ViewGroup mParent;
        int mLineIndex;
        int mChildWidth;
        int mStartX;
        int mLineHeight;
        int mIndex;
        int mSpecialViewWidth;
    }

    private boolean canShowSpecialView(int curLineIndex) {
        return mSpecialView != null && mNeedFold && curLineIndex + 1 == mFoldLineCount;
    }
}
