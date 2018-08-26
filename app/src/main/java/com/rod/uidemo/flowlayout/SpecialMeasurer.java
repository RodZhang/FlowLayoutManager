package com.rod.uidemo.flowlayout;

import android.support.annotation.NonNull;
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
        if (property.mChildCount == 0) {
            return 0;
        }

        int lineHeight = 0;
        int startX = property.mXStartPadding;
        int startY = 0;
        ViewGroup parent = property.mParent;
        if (mSpecialView != null && mSpecialView.getParent() != null) {
            parent.removeView(mSpecialView);
        }
        int lineIndex = 0;
        int childCount = parent.getChildCount();
        int specialViewWidth = 0;

        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            if (lineIndex == mMaxLineCount || (lineIndex == mFoldLineCount && mNeedFold)) {
                child.setVisibility(GONE);
                continue;
            }
            child.setVisibility(View.VISIBLE);

            child.measure(property.mChildMeasureSpace, property.mChildMeasureSpace);
            lineHeight = Math.max(lineHeight, child.getMeasuredHeight());
            int childWidth = child.getMeasuredWidth();
            if (canShowSpecialView(lineIndex)) {
                if (specialViewWidth == 0) {
                    mSpecialView.measure(MeasureSpec.makeMeasureSpec(80, MeasureSpec.EXACTLY),
                            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                    specialViewWidth = mSpecialView.getMeasuredWidth();
                }

                if (startX + childWidth + specialViewWidth > property.mXBeforeEnd) {
                    if (startX == property.mXStartPadding) {
                        childWidth = property.mXSpace - property.mPadH - specialViewWidth;
                        child.measure(MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.AT_MOST),
                                property.mChildMeasureSpace);
                        i++;
                    } else {
                        child.setVisibility(GONE);
                    }
                    parent.addView(mSpecialView, i);
                    lineIndex++;
                } else {
                    startX += childWidth + property.mPadH;
                }
                continue;
            }

            if (startX + childWidth > property.mXBeforeEnd) {
                if (startX == property.mXStartPadding) {
                    // 说明此行只能容下当前child做为单独一行
                    child.measure(MeasureSpec.makeMeasureSpec(property.mXSpace, MeasureSpec.AT_MOST),
                            property.mChildMeasureSpace);
                    startX = property.mXStartPadding;
                } else {
                    startX = property.mXStartPadding + childWidth + property.mPadH;
                }

                lineIndex++;
                if (lineIndex < mMaxLineCount && i != property.mChildCount - 1) {
                    startY += lineHeight + property.mPadV;
                }
            } else {
                startX += childWidth + property.mPadH;
            }
        }
        return startY + lineHeight;
    }

    private boolean canShowSpecialView(int curLineIndex) {
        return mSpecialView != null && mNeedFold && curLineIndex + 1 == mFoldLineCount;
    }
}
