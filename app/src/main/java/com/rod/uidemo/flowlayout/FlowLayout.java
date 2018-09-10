package com.rod.uidemo.flowlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.SparseArray;
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
    private SpecialViewEventListener mSpecialViewListener;

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

    public void config(int foldLineCount, boolean needFold, View specialView, int maxLineCount) {
        mFoldLineCount = foldLineCount;
        mNeedFold = needFold;
        mSpecialView = specialView;
        mMaxLineCount = maxLineCount;
    }

    public void setSpecialViewListener(SpecialViewEventListener specialViewListener) {
        mSpecialViewListener = specialViewListener;
    }

    public void setNeedFold(boolean needFold) {
        mNeedFold = needFold;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mSpecialView != null && mSpecialView.getParent() != null) {
            removeView(mSpecialView);
        }
        resetLayoutProperties(widthMeasureSpec, heightMeasureSpec);

        int contentHeight = measure(mProperty);
        if (mProperty.mHeightMode == MeasureSpec.UNSPECIFIED) {
            mProperty.mHeight = contentHeight;
        } else if (mProperty.mHeightMode == MeasureSpec.AT_MOST) {
            if (contentHeight < mProperty.mHeight) {
                mProperty.mHeight = contentHeight;
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
        mProperty.mChildRects.clear();
    }

    private int measure(@NonNull LayoutProperty property) {
        int specialViewWidth = 0;
        if (mSpecialView != null) {
            mSpecialView.measure(
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            specialViewWidth = mSpecialView.getMeasuredWidth();
        }
        int lineIndex = 0;
        int contentHeight = 0;
        int childWidth;
        int childHeight;
        int lineHeight = 0;
        View child;
        int startX = property.mXStartPadding;
        int startY = property.mYStartPadding;
        for (int i = 0; i < property.mChildCount; ) {
            child = getChildAt(i);
            child.measure(MeasureSpec.makeMeasureSpec(property.mXSpace, MeasureSpec.AT_MOST),
                    property.mChildMeasureSpace);
            childWidth = child.getMeasuredWidth();
            childHeight = child.getMeasuredHeight();

            if (canShowSpecialView(lineIndex, i == property.mLastChildIndex)) {
                lineHeight = Math.max(lineHeight, childHeight);
                // TODO: 2018/9/10 check is last child
                if (startX + childWidth + mPadH + specialViewWidth <= property.mXBeforeEnd) {
                    Rect rect = new Rect(startX, startY, startX + childWidth, startY + childHeight);
                    property.mChildRects.put(i, rect);
                    startX += childWidth + mPadH;
                    i++;
                    continue;
                } else {
                    if (startX == property.mXStartPadding) {
                        boolean hasMore = i < property.mChildCount - 1;
                        int extra = hasMore ? specialViewWidth + mPadH : 0;
                        int newWidth = property.mXBeforeEnd - extra - startX;
                        child.measure(MeasureSpec.makeMeasureSpec(newWidth, MeasureSpec.AT_MOST),
                                property.mChildMeasureSpace);
                        Rect rect = new Rect(startX, startY, startX + newWidth, startY + childHeight);
                        property.mChildRects.put(i, rect);
                        startX += newWidth + mPadH;
                        if (hasMore) {
                            showSpecialView(i + 1);
                            rect = new Rect(startX, startY, startX + mSpecialView.getMeasuredWidth(), startY + lineHeight);
                            property.mChildRects.put(i + 1, rect);
                        }
                    } else {
                        showSpecialView(i);
                        Rect rect = new Rect(startX, startY, startX + mSpecialView.getMeasuredWidth(), startY + lineHeight);
                        property.mChildRects.put(i, rect);
                    }
                    break;
                }
            }

            if (startX + childWidth <= property.mXBeforeEnd) {
                Rect rect = new Rect(startX, startY, startX + childWidth, startY + childHeight);
                property.mChildRects.put(i, rect);
                startX += childWidth + mPadH;
                lineHeight = Math.max(lineHeight, childHeight);
                i++;
            } else {
                lineIndex++;
                if (lineIndex >= mMaxLineCount) {
                    break;
                }
                startY += lineHeight + mPadV;
                startX = property.mXStartPadding;
            }
        }
        return startY + lineHeight + property.mYEndPadding;
    }

    private void showSpecialView(int index) {
        addView(mSpecialView, index);
        mSpecialView.setOnClickListener(v -> {
            if (mSpecialViewListener != null) {
                mSpecialViewListener.onClickSpecialView(mSpecialView);
            }
        });
        if (mSpecialViewListener != null) {
            mSpecialViewListener.onSpecialViewShown(mSpecialView);
        }
    }

    private boolean canShowSpecialView(int curLineIndex, boolean isLastChild) {
        return mSpecialView != null && mNeedFold && curLineIndex + 1 == mFoldLineCount
                && !isLastChild;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);
            Rect rect = mProperty.mChildRects.get(i);
            if (rect == null) {
                child.setVisibility(GONE);
            } else {
                child.setVisibility(VISIBLE);
                child.layout(rect.left, rect.top, rect.right, rect.bottom);
            }
        }
    }

    public interface SpecialViewEventListener {
        void onSpecialViewShown(View specialView);

        void onClickSpecialView(View specialView);
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
        final int mChildMeasureSpace = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        final SparseArray<Rect> mChildRects = new SparseArray<>();
    }
}
