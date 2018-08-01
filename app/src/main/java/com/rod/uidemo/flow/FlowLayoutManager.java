package com.rod.uidemo.flow;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.rod.uidemo.UL;

import java.util.List;
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
    private SparseArray<Rect> mViewRectArr = new SparseArray<>();

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
            recycle(dy, recycler);
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
            // 手指从上往下滑动
            int removeCount = 0;
            for (int i = getChildCount() - 1; i >= 0; i--) {
                childView = getChildAt(i);
                if (getDecoratedTop(childView) + Math.abs(dy) >= getHeight()) {
                    removeCount++;
                } else {
                    break;
                }
            }
            if (removeCount == getChildCount()) {
                removeCount--;
            }
            if (removeCount > 0) {
                UL.Companion.d(TAG, "removeCount=%d, startIndex=%d, endIndex=%d", removeCount, getChildCount() - 1, getChildCount() - 1 - removeCount);
                removeAndRecycleViews(recycler, getChildCount() - 1, getChildCount() - 1 - removeCount);
            }
        } else if (dy > 0) {
            int removeCount = 0;
            for (int i = 0, count = getChildCount(); i < count; i++) {
                childView = getChildAt(i);
                if (getDecoratedBottom(childView) - dy <= 0) {
                    removeCount++;
                } else {
                    break;
                }
            }
            if (removeCount > 0) {
                UL.Companion.d(TAG, "removeCount=%d, startIndex=0, endIndex=%d", removeCount, removeCount);
                removeAndRecycleViews(recycler, 0, removeCount);
            }
        }
    }

    private void fill(int dy, RecyclerView.Recycler recycler) {
        if (dy < 0) {
            // 手指从上往下滑动
            View firstChild = getChildAt(0);
            if (firstChild == null) {
                UL.Companion.d(TAG, "firstChild=null");
                return;
            }
            int newTop = getDecoratedTop(firstChild) - dy;
            if (newTop < 0) {
                // 说明滑动dy后，当前第一行仍没有完全展示，因此不用加载之前的数据
                return;
            }
            int indexOfFirstChild = getPosition(firstChild);
            int startPos = indexOfFirstChild - 1;
            if (startPos < 0) {
                return;
            }

            int offset = Math.abs(mScrollOffset);
            int viewBottom = mViewRectArr.get(startPos).bottom - offset;
            View itemView;
            Rect viewRect;
            while (startPos >= 0) {
                itemView = recycler.getViewForPosition(startPos);
                addView(itemView, 0);
                measureChildWithMargins(itemView, 0, 0);
                viewRect = mViewRectArr.get(startPos);
                int newViewBottom = viewRect.bottom - offset;
                if (viewBottom == newViewBottom || newViewBottom > 0) {
                    layoutDecoratedWithMargins(itemView, viewRect.left, viewRect.top - offset, viewRect.right, newViewBottom);
                    UL.Companion.d(TAG, "fill to top, pos=%d", startPos);
                    startPos--;
                    viewBottom = newViewBottom;
                } else {
                    detachAndScrapView(itemView, recycler);
                    break;
                }
            }
        } else if (dy > 0) {
            // 手指从下往上滑动
            View itemView;
            int startX = getPaddingLeft();
            View lastChild = getChildAt(getChildCount() - 1);
            int startY = getDecoratedBottom(lastChild);
            int newBottom = startY + dy;
            if (startY - dy > getHeight()) {
                // 说明滑动dy后，最后一行还没有完全显示，因此不需要绘制展示后面的数据
                return;
            }
            UL.Companion.d(TAG, "startY=%d, newBottom=%d, dy=%d, startPos=%d", startY, newBottom, dy, getPosition(lastChild));
            for (int i = getPosition(lastChild) + 1, size = getItemCount(); i < size; i++) {
                itemView = recycler.getViewForPosition(i);
                addView(itemView);
                measureChildWithMargins(itemView, 0, 0);
                if (startX + getDecoratedMeasuredWidth(itemView) > mHorizontalSpace) {
                    startX = getPaddingLeft();
                    startY += getDecoratedMeasuredHeight(itemView);
                    UL.Companion.d(TAG, "startY=%d, newBottom=%d, dy=%d, pos=%d", startY, newBottom, dy, i);
                    if (startY > newBottom) {
                        detachAndScrapView(itemView, recycler);
                        break;
                    }
                }
                int left = startX;
                int top = startY;
                int right = startX + getDecoratedMeasuredWidth(itemView);
                int bottom = startY + getDecoratedMeasuredHeight(itemView);
                Rect rect = new Rect(left, top + mScrollOffset, right, bottom + mScrollOffset);
                mViewRectArr.put(i, rect);
                layoutDecoratedWithMargins(itemView, left, top, right, bottom);
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
        mViewRectArr.clear();
        detachAndScrapAttachedViews(recycler);

        mHorizontalSpace = getWidth() - getPaddingLeft() - getPaddingRight();
        mVerticalSpace = getHeight() - getPaddingTop() - getPaddingBottom();
        log("onLayoutChildren, mHorizontalSpace=%d, mVerticalSpace=%d", mHorizontalSpace, mVerticalSpace);

        View itemView;
        int startX = getPaddingLeft();
        int startY = getPaddingTop();
        int rowIndex = 0, columnCount = 0;
        List<Integer> dataIndex;
        for (int i = 0, size = state.getItemCount(); i < size; i++) {
            itemView = recycler.getViewForPosition(i);
            addView(itemView);
            measureChildWithMargins(itemView, 0, 0);
            if (startX + getDecoratedMeasuredWidth(itemView) > mHorizontalSpace) {
                startX = getPaddingLeft();
                startY += getDecoratedMeasuredHeight(itemView);
                rowIndex++;
                columnCount = 0;
                if (startY > mVerticalSpace) {
                    detachAndScrapView(itemView, recycler);
                    log("index=%d, startY(%d) > mVerticalSpace(%d), end", i, startY, mVerticalSpace);
                    break;
                }
            }
            int left = startX;
            int top = startY;
            int right = startX + getDecoratedMeasuredWidth(itemView);
            int bottom = startY + getDecoratedMeasuredHeight(itemView);
            Rect rect = new Rect(left, top, right, bottom);
            mViewRectArr.put(i, rect);
            layoutDecoratedWithMargins(itemView, left, top, right, bottom);
            log("index=%d, startX=%d, startY=%d", i, startX, startY);
            startX += getDecoratedMeasuredWidth(itemView);
            columnCount++;
        }
    }

    private void log(String format, Object... args) {
        Log.d(TAG, String.format(Locale.getDefault(), format, args));
    }
}
