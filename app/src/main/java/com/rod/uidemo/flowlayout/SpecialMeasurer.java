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
//        boolean isChangeLine;
        ViewGroup parent = property.mParent;
        if (mSpecialView != null && mSpecialView.getParent() != null) {
            parent.removeView(mSpecialView);
        }
        int lineIndex = 0;
        int colIndex = 0;
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; ) {
            final View child = parent.getChildAt(i);
            if (lineIndex == mMaxLineCount || (lineIndex == mFoldLineCount) && mNeedFold) {
                child.setVisibility(GONE);
                i++;
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
//                    isChangeLine = colIndex == 0;
                    startY += preLineHeight + property.mPadV;
                    if (i == childCount - 1) {
                        int newWidth = property.mXBeforeEnd - startX;
                        if (newWidth < childWidth) {
                            child.measure(MeasureSpec.makeMeasureSpec(newWidth, MeasureSpec.EXACTLY),
                                    property.mChildMeasureSpace);
                        }
                        colIndex++;
                    } else {
                        if (colIndex == 0) {
                            startX = property.mXStartPadding;
                            int newWidth = property.mXBeforeEnd - startX - specialViewWidth;
                            if (newWidth < childWidth) {
                                child.measure(MeasureSpec.makeMeasureSpec(newWidth, MeasureSpec.EXACTLY),
                                        property.mChildMeasureSpace);
                            }
                            parent.addView(mSpecialView, i + 1);
                            startX += newWidth + specialViewWidth;
                            colIndex += 2;
                            i++;
                        } else {
                            child.setVisibility(GONE);
                            parent.addView(mSpecialView, i);
                            colIndex++;
                        }
                    }
                    startX = property.mXStartPadding + childWidth;
                    lineIndex++;
                } else {
                    startX += childWidth + property.mPadH;
                    colIndex++;
//                    isChangeLine = false;
                    if (i == property.mChildCount - 1) {
                        startY += preLineHeight + property.mPadV;
                    }
                }
            } else if (startX + childWidth > property.mXBeforeEnd) {
                childWidth = Math.min(property.mXBeforeEnd - property.mXStartPadding, childWidth);
                child.measure(MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY),
                        property.mChildMeasureSpace);
                startX = property.mXStartPadding + childWidth;
//                isChangeLine = true;
                lineIndex++;
                colIndex = 0;
                startY += preLineHeight + property.mPadV;
            } else {
                colIndex++;
                startX += childWidth + property.mPadH;
//                isChangeLine = false;

                if (i == property.mChildCount - 1) {
                    startY += preLineHeight + property.mPadV;
                }
            }
//            if (isChangeLine || i == 0) {
//                startY += preLineHeight + property.mPadV;
//            }
            i++;
        }
        return Math.max(startY - property.mPadV, 0);
    }

    private boolean isLimited() {
        boolean needFold = mNeedFold && mFoldLineCount != Integer.MAX_VALUE;
        return mSpecialView != null
                && (needFold || mMaxLineCount != Integer.MAX_VALUE);
    }
}
