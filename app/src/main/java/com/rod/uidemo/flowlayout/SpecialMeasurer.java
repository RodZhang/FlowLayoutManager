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
            mLineHeightMap.put(state.mLineIndex, state.mLineHeight);
            calculateAtFoldLine(child, state, property);
        } else if (state.mStartX + state.mChildWidth > property.mXBeforeEnd) {
            boolean lineCountExtra = state.mStartX == property.mXStartPadding;
            state.mStartX = property.mXStartPadding;
            onChangeLine(child, state, property, lineCountExtra);
        } else {
            state.mStartX += state.mChildWidth + property.mPadH;
            mLineHeightMap.put(state.mLineIndex, state.mLineHeight);
        }
    }

    private void onChangeLine(View child, MeasureState state,
                              FlowLayout.LayoutProperty property, boolean needAddLine) {
        if (canShowSpecialView(state.mLineIndex)) {
            calculateAtFoldLine(child, state, property);
        } else if (state.mChildWidth > property.mXSpace) {
            state.mChildWidth = property.mXSpace;
            child.measure(
                    MeasureSpec.makeMeasureSpec(state.mChildWidth, MeasureSpec.AT_MOST),
                    property.mChildMeasureSpace);
            mLineHeightMap.put(state.mLineIndex, state.mLineHeight);
            state.mStartX = property.mXStartPadding;
        } else {
            state.mStartX += property.mPadH + state.mChildWidth;
            mLineHeightMap.put(state.mLineIndex, state.mLineHeight);
            state.mLineIndex++;
        }
    }

    private void calculateAtFoldLine(View child, MeasureState state,
                                     FlowLayout.LayoutProperty property) {
        ensureSpecialViewMeasured(state);

        if (state.mStartX + state.mChildWidth + property.mPadH + state.mSpecialViewWidth
                > property.mXBeforeEnd) {
            state.mChildWidth = property.mXBeforeEnd - property.mXStartPadding
                    - property.mPadH - state.mSpecialViewWidth;
            child.measure(
                    MeasureSpec.makeMeasureSpec(state.mChildWidth, MeasureSpec.AT_MOST),
                    property.mChildMeasureSpace);
            state.mIndex++;
            state.mParent.addView(mSpecialView, state.mIndex);
        } else {
            state.mStartX += state.mChildWidth + property.mPadH;
        }
    }

    private void ensureSpecialViewMeasured(MeasureState state) {
        if (state.mSpecialViewWidth == 0) {
            mSpecialView.measure(
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                    MeasureSpec.makeMeasureSpec(state.mLineHeight, MeasureSpec.EXACTLY));
            state.mSpecialViewWidth = mSpecialView.getMeasuredWidth();
        }
    }

    private boolean canShowSpecialView(int curLineIndex) {
        return mSpecialView != null && mNeedFold && curLineIndex + 1 == mFoldLineCount;
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
}
