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
        int preLineHeight = 0;
        int startX = property.mXStartPadding;
        int startY = 0;
        boolean isChangeLine;
        ViewGroup parent = property.mParent;
        if (mSpecialView != null && mSpecialView.getParent() != null) {
            parent.removeView(mSpecialView);
        }
        int lineIndex = 0;
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            if (lineIndex == mMaxLineCount || (lineIndex == mFoldLineCount) && mNeedFold) {
                child.setVisibility(GONE);
                continue;
            }
            child.setVisibility(View.VISIBLE);

            child.measure(property.mChildMeasureSpace, property.mChildMeasureSpace);
            preLineHeight = Math.max(preLineHeight, child.getMeasuredHeight());
            int childWidth = child.getMeasuredWidth();
            if (mSpecialView != null && mNeedFold && lineIndex == mFoldLineCount - 1) {
                mSpecialView.measure(MeasureSpec.makeMeasureSpec(40, MeasureSpec.EXACTLY),
                        MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                int specialViewWidth = mSpecialView.getMeasuredWidth();
                if (startX + childWidth + specialViewWidth > property.mXBeforeEnd) {
                    if (i == childCount - 1) {
                        int newWidth = property.mXBeforeEnd - startX;
                        if (newWidth < childWidth) {
                            child.measure(MeasureSpec.makeMeasureSpec(newWidth, MeasureSpec.EXACTLY),
                                    property.mChildMeasureSpace);
                        }
                    } else {
                        child.setVisibility(GONE);
                        parent.addView(mSpecialView, i);
                    }
                    startX = property.mXStartPadding + childWidth;
                    lineIndex++;
                } else {
                    startX += childWidth + property.mPadH;
                }
                isChangeLine = false;
            } else if (startX + childWidth > property.mXBeforeEnd) {
                childWidth = Math.min(property.mXBeforeEnd - property.mXStartPadding, childWidth);
                child.measure(MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY),
                        property.mChildMeasureSpace);
                startX = property.mXStartPadding + childWidth;
                isChangeLine = true;
                lineIndex++;
                if (lineIndex == mMaxLineCount) {
                    child.setVisibility(GONE);
                    isChangeLine = false;
                }
            } else {
                startX += childWidth + property.mPadH;
                isChangeLine = false;
            }
            if (isChangeLine || i == 0) {
                startY += preLineHeight + property.mPadV;
            }
        }
        return Math.max(startY - property.mPadV, 0);
    }

    private boolean isLimited() {
        boolean needFold = mNeedFold && mFoldLineCount != Integer.MAX_VALUE;
        return mSpecialView != null
                && (needFold || mMaxLineCount != Integer.MAX_VALUE);
    }
}
