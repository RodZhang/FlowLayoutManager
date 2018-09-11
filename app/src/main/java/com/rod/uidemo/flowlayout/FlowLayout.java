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
    private final MeasureState mState = new MeasureState();

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
        mProperty.mAtMostSpace = MeasureSpec.makeMeasureSpec(mProperty.mXSpace, MeasureSpec.AT_MOST);
    }

    private int measure(@NonNull LayoutProperty property) {
        int specialViewWidth = 0;
        if (mSpecialView != null) {
            mSpecialView.measure(
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            specialViewWidth = mSpecialView.getMeasuredWidth();
        }
        resetMeasureState();
        mState.mSpecialViewWidth = specialViewWidth;
        mState.mStartX = property.mXStartPadding;
        mState.mStartY = property.mYStartPadding;

        for (; mState.mIndex < property.mChildCount; ) {
            mState.mChildView = getChildAt(mState.mIndex);
            mState.mChildView.measure(property.mAtMostSpace, property.mUnspecifiedSpec);
            mState.mChildWidth = mState.mChildView.getMeasuredWidth();
            mState.mChildHeight = mState.mChildView.getMeasuredHeight();

            if (canShowSpecialView(mState.mLineIndex) && mState.mIndex < property.mLastChildIndex) {
                if (measureFoldLine(property, mState)) {
                    break;
                } else {
                    continue;
                }
            }

            int right = mState.mStartX + mState.mChildWidth;
            if (right <= property.mXBeforeEnd) {
                int bottom = mState.mStartY + mState.mChildHeight;
                Rect rect = new Rect(mState.mStartX, mState.mStartY, right, bottom);
                property.mChildRects.put(mState.mIndex, rect);
                mState.mStartX += mState.mChildWidth + mPadH;
                mState.mLineHeight = Math.max(mState.mLineHeight, mState.mChildHeight);
                mState.mIndex++;
            } else {
                mState.mLineIndex++;
                if (mState.mLineIndex >= mMaxLineCount) {
                    break;
                }
                mState.mStartY += mState.mLineHeight + mPadV;
                mState.mStartX = property.mXStartPadding;
            }
        }
        return mState.mStartY + mState.mLineHeight + property.mYEndPadding;
    }

    /**
     * 测量需要折叠的那行view
     *
     * @param property
     * @param state
     * @return true: 已添加specialView
     */
    private boolean measureFoldLine(@NonNull LayoutProperty property, @NonNull MeasureState state) {
        state.mLineHeight = Math.max(state.mLineHeight, state.mChildHeight);
        if (state.mStartX + state.mChildWidth + mPadH + state.mSpecialViewWidth <= property.mXBeforeEnd) {
            Rect rect = new Rect(state.mStartX, state.mStartY,
                    state.mStartX + state.mChildWidth,
                    state.mStartY + state.mChildHeight);
            property.mChildRects.put(state.mIndex, rect);
            state.mStartX += state.mChildWidth + mPadH;
            state.mIndex++;
            return false;
        }
        if (state.mStartX == property.mXStartPadding) {
            boolean hasMore = state.mIndex < property.mChildCount - 1;
            int extra = hasMore ? state.mSpecialViewWidth + mPadH : 0;
            int newWidth = property.mXBeforeEnd - extra - state.mStartX;
            int widthMeasureSpec = MeasureSpec.makeMeasureSpec(newWidth, MeasureSpec.AT_MOST);
            state.mChildView.measure(widthMeasureSpec, property.mUnspecifiedSpec);
            Rect rect = new Rect(state.mStartX, state.mStartY, state.mStartX + newWidth,
                    state.mStartY + state.mChildHeight);
            property.mChildRects.put(state.mIndex, rect);
            state.mStartX += newWidth + mPadH;
            if (hasMore) {
                showSpecialView(state.mIndex + 1);
                rect = new Rect(state.mStartX, state.mStartY,
                        state.mStartX + mSpecialView.getMeasuredWidth(),
                        state.mStartY + state.mLineHeight);
                property.mChildRects.put(state.mIndex + 1, rect);
            }
        } else {
            showSpecialView(state.mIndex);
            Rect rect = new Rect(state.mStartX, state.mStartY,
                    state.mStartX + mSpecialView.getMeasuredWidth(),
                    state.mStartY + state.mLineHeight);
            property.mChildRects.put(state.mIndex, rect);
        }
        return true;
    }

    private void resetMeasureState() {
        mState.mStartX = 0;
        mState.mStartY = 0;
        mState.mLineIndex = 0;
        mState.mLineHeight = 0;
        mState.mChildWidth = 0;
        mState.mChildHeight = 0;
        mState.mSpecialViewWidth = 0;
        mState.mIndex = 0;
        mState.mChildView = null;
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

    private boolean canShowSpecialView(int curLineIndex) {
        return mSpecialView != null && mNeedFold && curLineIndex + 1 == mFoldLineCount;
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
        int mAtMostSpace;
        final int mUnspecifiedSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        final SparseArray<Rect> mChildRects = new SparseArray<>();
    }

    static class MeasureState {
        int mStartX;
        int mStartY;
        int mLineIndex;
        int mLineHeight;
        int mChildWidth;
        int mChildHeight;
        int mSpecialViewWidth;
        int mIndex;
        View mChildView;
    }
}
