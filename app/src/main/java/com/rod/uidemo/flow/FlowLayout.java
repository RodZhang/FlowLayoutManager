package com.rod.uidemo.flow;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.rod.uidemo.R;

/**
 * @author Rod
 * @date 2018/8/21
 */
public class FlowLayout extends ViewGroup {
    private final static int PAD_H = 20, PAD_V = 20;

    private int mPadH, mPadV;

    private int mMaxLineCount = Integer.MAX_VALUE;
    private int mFoldLineCount = Integer.MAX_VALUE;
    private boolean mNeedFold;

    private View mSpecialView;

    private final LayoutProperty mProperty = new LayoutProperty();

    public FlowLayout(Context context) {
        super(context);
        mPadH = PAD_H;
        mPadV = PAD_V;
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.FlowLayout);
            mPadH = array.getDimensionPixelOffset(R.styleable.FlowLayout_dividerSizeHorizontal, PAD_H);
            mPadV = array.getDimensionPixelOffset(R.styleable.FlowLayout_dividerSizeVertical, PAD_V);
            array.recycle();
        }
    }

    public void setMaxLineCount(int maxLineCount) {
        mMaxLineCount = maxLineCount;
    }

    public void setFoldLineCount(int foldLineCount) {
        mFoldLineCount = foldLineCount;
    }

    public void setNeedFold(boolean needFold) {
        mNeedFold = needFold;
    }

    public void setSpecialView(View specialView) {
        mSpecialView = specialView;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        resetLayoutProperties(widthMeasureSpec, heightMeasureSpec);
        if (isLimited()) {
            measureWithLimit(mProperty);
        } else {
            measureNormal(mProperty);
        }
    }

    private void resetLayoutProperties(int widthMeasureSpec, int heightMeasureSpec) {
        mProperty.mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mProperty.mWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        mProperty.mXStartPadding = getPaddingLeft();
        mProperty.mXBeforeEnd = mProperty.mWidth - getPaddingRight();
        mProperty.mXSpace = mProperty.mXBeforeEnd - mProperty.mXStartPadding;
        mProperty.mHeight = MeasureSpec.getSize(heightMeasureSpec);
        mProperty.mHeightMode = MeasureSpec.getMode(heightMeasureSpec);
        mProperty.mYStartPadding = getPaddingTop();
        mProperty.mYEndPadding = getPaddingBottom();
        mProperty.mChildCount = getChildCount();
        mProperty.mLastChildIndex = mProperty.mChildCount - 1;
    }

    private void measureWithLimit(@NonNull LayoutProperty property) {

    }

    private void measureNormal(@NonNull LayoutProperty property) {
        int preLineHeight = 0;
        int startX = property.mXStartPadding;
        int startY = 0;
        boolean isChangeLine = false;
        for (int i = 0; i < property.mChildCount; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            child.measure(property.mChildMeasureSpace, property.mChildMeasureSpace);
            preLineHeight = Math.max(preLineHeight, child.getMeasuredHeight());
            int childWidth = child.getMeasuredWidth();
            if (startX + childWidth > property.mXBeforeEnd) {
                childWidth = Math.min(property.mXBeforeEnd - property.mXStartPadding, childWidth);
                child.measure(MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY),
                        property.mChildMeasureSpace);
                startX = property.mXStartPadding + childWidth;
                isChangeLine = true;
            } else {
                startX += childWidth + mPadH;
                isChangeLine = false;
            }
            if (isChangeLine || i == 0) {
                startY += preLineHeight + mPadV;
            }
        }

        int contentHeight = Math.max(startY - mPadV, 0);
        if (property.mHeightMode == MeasureSpec.UNSPECIFIED) {
            property.mHeight = contentHeight + property.mYStartPadding + property.mYEndPadding;
        } else if (property.mHeightMode == MeasureSpec.AT_MOST) {
            int newHeight = contentHeight + property.mYStartPadding + property.mYEndPadding;
            if (newHeight < property.mHeight) {
                property.mHeight = newHeight;
            }
        }
        setMeasuredDimension(property.mWidth, property.mHeight);
    }

    private boolean isLimited() {
        boolean needFold = mNeedFold && mFoldLineCount != Integer.MAX_VALUE;
        return mSpecialView != null
                && (needFold || mMaxLineCount != Integer.MAX_VALUE);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int startX = mProperty.mXStartPadding;
        int startY = mProperty.mYStartPadding;
        int maxLineHeight = 0;
        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            maxLineHeight = Math.max(maxLineHeight, childHeight);
            if (startX + childWidth > mProperty.mWidth) {
                startX = mProperty.mXStartPadding;
                startY += maxLineHeight + mPadV;
            }
            child.layout(startX, startY, startX + childWidth, startY + childHeight);
            startX += childWidth + mPadH;
        }
        startY = 0;
    }

    private static class LayoutProperty {
        int mWidth;
        int mWidthMode;
        int mHeight;
        int mHeightMode;
        int mXSpace;
        int mXStartPadding;
        int mXBeforeEnd;
        int mYStartPadding;
        int mYEndPadding;
        int mChildCount;
        int mLastChildIndex;
        final int mChildMeasureSpace = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
    }
}
