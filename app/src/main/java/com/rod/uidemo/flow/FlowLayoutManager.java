package com.rod.uidemo.flow;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.Locale;

/**
 * No pains, no gains.
 * <p>
 * Created by Rod on 2018/6/25.
 */
public class FlowLayoutManager extends RecyclerView.LayoutManager {
    private static final String TAG = "FlowLayoutManager";
    private int mHorizontalSpace;
    private int mVerticalSpace;
    private int mScrollOffset;

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public boolean canScrollVertically() {
        return true;
    }

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (state.isPreLayout()) {
            return 0;
        }
        dy = fixDy(dy);
        log("scrollVerticallyBy, dy=%d, mScrollOffset=%d", dy, mScrollOffset);
        if (dy != 0) {
//            recycle(dy, recycler);
            fill(dy, recycler);
            offsetChildrenVertical(-dy);
            mScrollOffset += dy;
        }
        return dy;
    }

    private int fixDy(int dy) {
        if (getChildCount() == 0) {
            return 0;
        }

        if (dy < 0) {
            if (mScrollOffset + dy <= 0) {
                return -mScrollOffset;
            } else {
                return dy;
            }
        } else {
            final View lastView = getChildAt(getChildCount() - 1);
            if (getPosition(lastView) == getItemCount() - 1) {
                final int lastViewBottom = getDecoratedBottom(lastView);
                final int recyclerViewHeight = getHeight();
                if (lastViewBottom - dy <= recyclerViewHeight) {
                    // 最后一个item完全可见了
                    return lastViewBottom - recyclerViewHeight;
                } else {
                    return dy;
                }
            } else {
                return dy;
            }
        }
    }

    private void recycle(int dy, RecyclerView.Recycler recycler) {
        View childView;
        if (dy < 0) {
            for (int i = getChildCount() - 1; i >= 0; i--) {
                childView = getChildAt(i);
                if (getDecoratedTop(childView) - dy <= mVerticalSpace) {
                    removeAndRecycleViews(recycler, getChildCount() - 1, i);
                    return;
                }
            }
        } else if (dy > 0) {
            for (int i = 0, count = getChildCount(); i < count; i++) {
                childView = getChildAt(i);
                if (getDecoratedBottom(childView) + dy > 0) {
                    removeAndRecycleViews(recycler, 0, i);
                    return;
                }
            }
        }
    }

    private void fill(int dy, RecyclerView.Recycler recycler) {
        if (dy < 0) {
            // 手指从上往下滑动

        } else if (dy > 0) {
            // 手指从下往上滑动
            View lastChild = getChildAt(getChildCount() - 1);
            int newBottom = getDecoratedBottom(lastChild) + dy;
            View itemView;
            int startX = getPaddingLeft();
            int startY = getDecoratedBottom(lastChild);
            for (int i = getPosition(lastChild) + 1, size = getItemCount(); i < size; i++) {
                itemView = recycler.getViewForPosition(i);
                addView(itemView);
                measureChildWithMargins(itemView, 0, 0);
                if (startX + getDecoratedMeasuredWidth(itemView) > mHorizontalSpace) {
                    startX = getPaddingLeft();
                    startY += getDecoratedMeasuredHeight(itemView);
                    if (startY > newBottom) {
                        detachAndScrapView(itemView, recycler);
                        break;
                    }
                }
                layoutDecoratedWithMargins(itemView, startX, startY, startX + getDecoratedMeasuredWidth(itemView), startY + getDecoratedMeasuredHeight(itemView));
                startX += getDecoratedMeasuredWidth(itemView);
            }
        }
    }

    private void removeAndRecycleViews(RecyclerView.Recycler recycler, int startIndex, int endIndex) {
        if (startIndex == endIndex) {
            return;
        }

        if (endIndex > startIndex) {
            for (int i = endIndex - 1; i >= startIndex; i--) {
                removeAndRecycleViewAt(i, recycler);
            }
        } else {
            for (int i = startIndex; i > endIndex; i--) {
                removeAndRecycleViewAt(i, recycler);
            }
        }
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (state.getItemCount() == 0 || state.isPreLayout()) {
            return;
        }
        detachAndScrapAttachedViews(recycler);

        mHorizontalSpace = getWidth() - getPaddingLeft() - getPaddingRight();
        mVerticalSpace = getHeight() - getPaddingTop() - getPaddingBottom();
        log("onLayoutChildren, mHorizontalSpace=%d, mVerticalSpace=%d", mHorizontalSpace, mVerticalSpace);

        View itemView;
        int startX = getPaddingLeft();
        int startY = getPaddingTop();
        for (int i = 0, size = state.getItemCount(); i < size; i++) {
            itemView = recycler.getViewForPosition(i);
            addView(itemView);
            measureChildWithMargins(itemView, 0, 0);
            if (startX + getDecoratedMeasuredWidth(itemView) > mHorizontalSpace) {
                startX = getPaddingLeft();
                startY += getDecoratedMeasuredHeight(itemView);
                if (startY > mVerticalSpace) {
                    detachAndScrapView(itemView, recycler);
                    log("index=%d, startY(%d) > mVerticalSpace(%d), end", i, startY, mVerticalSpace);
                    break;
                }
            }
            layoutDecoratedWithMargins(itemView, startX, startY, startX + getDecoratedMeasuredWidth(itemView), startY + getDecoratedMeasuredHeight(itemView));
            log("index=%d, startX=%d, startY=%d", i, startX, startY);
            startX += getDecoratedMeasuredWidth(itemView);

        }
    }

    private void log(String format, Object... args) {
        Log.d(TAG, String.format(Locale.getDefault(), format, args));
    }
}
