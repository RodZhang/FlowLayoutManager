package com.rod.uidemo.flow;

import android.content.Context;
import android.content.res.TypedArray;
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
    private int mSingleLineHeight;

    private int mPadH, mPadV;

    private int mMaxLineCount = Integer.MAX_VALUE;
    private int mFoldLineCount = Integer.MAX_VALUE;
    private boolean mNeedFold;

    private View mSpecialView;

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
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int spaceWidth = width - getPaddingRight() - getPaddingLeft();
        int xEnd = width - getPaddingRight();
        int height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
        int count = getChildCount();
        int xPos = getPaddingLeft();
        int yPos = getPaddingTop();

        int childHeightMeasureSpec;
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
            childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST);
        } else {
            childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }

        int specialViewWidth = 0;
        if (mSpecialView != null) {
            measureChild(mSpecialView, childHeightMeasureSpec, childHeightMeasureSpec);
            specialViewWidth = mSpecialView.getMeasuredWidth();
        }

        mSingleLineHeight = 0;
        int lineIndex = 0;
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (lineIndex >= mMaxLineCount) {
                child.setVisibility(GONE);
                continue;
            }
            child.setVisibility(VISIBLE);

            child.measure(MeasureSpec.makeMeasureSpec(spaceWidth, MeasureSpec.AT_MOST), childHeightMeasureSpec);
            int childWidth = child.getMeasuredWidth();
            mSingleLineHeight = Math.max(mSingleLineHeight, child.getMeasuredHeight() + mPadV);

            if (mNeedFold && lineIndex + 1 == mFoldLineCount && mSpecialView != null) {
                if (xPos + childWidth + specialViewWidth > xEnd) {
                    if (xPos == getPaddingLeft()) {
                        childWidth = xEnd - xPos - specialViewWidth;
                        child.getLayoutParams().width = childWidth;
                    } else {
                        child.setVisibility(GONE);
                    }
                    removeView(mSpecialView);
                    mSpecialView.setVisibility(VISIBLE);
                    mSpecialView.getLayoutParams().height = mSingleLineHeight;
                    addView(mSpecialView, indexOfChild(child));
                }
            } else if (xPos + childWidth > xEnd) {
                xPos = getPaddingLeft();
                yPos += mSingleLineHeight;
                lineIndex++;
                if (lineIndex == mMaxLineCount) {
                    child.setVisibility(GONE);
                }
            }
            xPos += childWidth + mPadH;
        }

        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.UNSPECIFIED) {
            height = yPos + mSingleLineHeight;
        } else if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
            if (yPos + mSingleLineHeight < height) {
                height = yPos + mSingleLineHeight;
            }
        }
        height += getPaddingBottom();
        setMeasuredDimension(width, height - mPadV);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int parentWidth = right - left - getPaddingRight() - getPaddingLeft();
        int childPosX = getPaddingLeft();
        int childPosY = getPaddingTop();
        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                int childWidth = child.getMeasuredWidth();
                int childHeight = child.getMeasuredHeight();
                if (childPosX + childWidth > parentWidth) {
                    childPosX = getPaddingLeft();
                    childPosY += mSingleLineHeight;
                }
                child.layout(childPosX, childPosY, childPosX + childWidth, childPosY + childHeight);
                childPosX += childWidth + mPadH;
            }
        }
    }
}
