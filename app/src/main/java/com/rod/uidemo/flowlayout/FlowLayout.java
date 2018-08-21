package com.rod.uidemo.flowlayout;

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

    private Measurer mMeasurer = new NormalMeasurer();

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

    public void setMeasurer(Measurer measurer) {
        mMeasurer = measurer;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        resetLayoutProperties(widthMeasureSpec, heightMeasureSpec);

        int contentHeight = mMeasurer.measure(mProperty);
        if (mProperty.mHeightMode == MeasureSpec.UNSPECIFIED) {
            mProperty.mHeight = contentHeight + mProperty.mYStartPadding + mProperty.mYEndPadding;
        } else if (mProperty.mHeightMode == MeasureSpec.AT_MOST) {
            int newHeight = contentHeight + mProperty.mYStartPadding + mProperty.mYEndPadding;
            if (newHeight < mProperty.mHeight) {
                mProperty.mHeight = newHeight;
            }
        }
        setMeasuredDimension(mProperty.mWidth, mProperty.mHeight);
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
        mProperty.mPadH = mPadH;
        mProperty.mPadV = mPadV;
        mProperty.mParent = this;
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

    static class LayoutProperty {
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
        int mPadV;
        int mPadH;
        ViewGroup mParent;
        final int mChildMeasureSpace = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
    }
}
