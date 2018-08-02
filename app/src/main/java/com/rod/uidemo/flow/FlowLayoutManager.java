package com.rod.uidemo.flow;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.rod.uidemo.UL;

import java.util.Locale;

/**
 * No pains, no gains.
 * <p>
 * Created by Rod on 2018/6/25.
 */
public class FlowLayoutManager extends RecyclerView.LayoutManager {
    private static final String TAG = "FlowLayoutManager";

    private final LayoutState mLayoutState = new LayoutState();
    private final PositionRecoder mPositionRecoder = new PositionRecoder();

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
        if (state.isPreLayout() || dy == 0 || getChildCount() == 0) {
            return 0;
        }
        return scrollBy(dy, recycler);
    }

    private int scrollBy(int dy, RecyclerView.Recycler recycler) {
        dy = fixDy(dy);
        updateLayoutState(dy, mLayoutState);
        log("scrollVerticallyBy, dy=%d, mScrollOffset=%d", dy, mLayoutState.mScrollOffset);
        if (dy != 0) {
            fill(mLayoutState, recycler);
            offsetChildrenVertical(-dy);
            mLayoutState.mScrollOffset += dy;
        }
        return dy;
    }

    private void updateLayoutState(int dy, LayoutState layoutState) {
        layoutState.mLayoutDirection = dy < 0 ? LayoutState.LAYOUT_TO_HEAD : LayoutState.LAYOUT_TO_TAIL;
        layoutState.mAbsDy = Math.abs(dy);
    }

    private int fixDy(int dy) {
        return dy < 0 ? fixDyScrollToDown(dy) : fixDyScrollToUp(dy);
    }

    private int fixDyScrollToDown(int dy) {
        return (mLayoutState.mScrollOffset + dy < 0) ? -mLayoutState.mScrollOffset : dy;
    }

    private int fixDyScrollToUp(int dy) {
        final View lastView = getChildAt(getChildCount() - 1);
        final int lastViewIndex = getPosition(lastView);
        final int viewBottom = getDecoratedBottom(lastView);
        if (lastViewIndex == getItemCount() - 1) {
            if (viewBottom - dy < getHeight()) {
                return viewBottom - getHeight();
            } else {
                return dy;
            }
        } else {
            if (viewBottom - dy < 0) {
                return viewBottom;
            } else {
                return dy;
            }
        }
    }

    private void fill(LayoutState layoutState, RecyclerView.Recycler recycler) {
        recycle(layoutState, recycler);
        if (layoutState.mLayoutDirection == LayoutState.LAYOUT_TO_HEAD) {
            // 手指从上往下滑动
//            View firstChild = getChildAt(0);
//            if (getDecoratedTop(firstChild) - dy < 0) {
//                // 说明滑动dy后，当前第一行仍没有完全展示，因此不用加载之前的数据
//                log(TAG, "getDecoratedTop(firstChild) - dy < 0");
//                return;
//            }
//            int indexOfFirstChild = getPosition(firstChild);
//            int startPos = indexOfFirstChild - 1;
//            if (startPos < 0) {
//                log(TAG, "startPos=%d", startPos);
//                return;
//            }
//
//            int offset = Math.abs(mLayoutState.mScrollOffset);
//            int viewBottom = mViewRectArr.get(startPos).bottom - offset;
//            View itemView;
//            Rect viewRect;
//            while (startPos >= 0) {
//                itemView = recycler.getViewForPosition(startPos);
//                addView(itemView, 0);
//                measureChildWithMargins(itemView, 0, 0);
//                viewRect = mViewRectArr.get(startPos);
//                int newViewBottom = viewRect.bottom - offset;
//                if (viewBottom == newViewBottom || newViewBottom > 0) {
//                    layoutDecoratedWithMargins(itemView, viewRect.left, viewRect.top - offset, viewRect.right, newViewBottom);
//                    log(TAG, "fill to top, pos=%d", startPos);
//                    startPos--;
//                    viewBottom = newViewBottom;
//                } else {
//                    detachAndScrapView(itemView, recycler);
//                    break;
//                }
//            }
        } else {
            // 手指从下往上滑动
            View itemView;
            int startX = mLayoutState.mLeftBounds;
            View lastChild = getChildAt(getChildCount() - 1);
            // TODO: 2018/8/1 Null check
            int startY = getDecoratedBottom(lastChild);
            int newBottom = startY + mLayoutState.mAbsDy;
            if (startY - mLayoutState.mAbsDy > getHeight()) {
                // 说明滑动dy后，最后一行还没有完全显示，因此不需要绘制展示后面的数据
                return;
            }
            int lastViewIndex = getPosition(lastChild);
            log(TAG, "startY=%d, newBottom=%d, dy=%d, startPos=%d, childCount=%d", startY, newBottom, mLayoutState.mAbsDy, lastViewIndex, getChildCount());
            int rowIndex = mPositionRecoder.getRowIndex(lastViewIndex) + 1;
            for (int i = lastViewIndex + 1, size = getItemCount(); i < size; i++) {
                itemView = recycler.getViewForPosition(i);
                addView(itemView);
                measureChildWithMargins(itemView, 0, 0);
                if (startX + getDecoratedMeasuredWidth(itemView) > mLayoutState.mRightBounds) {
                    startX = getPaddingLeft();
                    startY += getDecoratedMeasuredHeight(itemView);
                    rowIndex++;
                    log(TAG, "startY=%d, newBottom=%d, dy=%d, pos=%d, childCount=%d", startY, newBottom, mLayoutState.mAbsDy, i, getChildCount());
                    if (startY > newBottom) {
                        detachAndScrapView(itemView, recycler);
                        break;
                    }
                }
                mPositionRecoder.update(rowIndex, i);
                int left = startX;
                int top = startY;
                int right = startX + getDecoratedMeasuredWidth(itemView);
                int bottom = startY + getDecoratedMeasuredHeight(itemView);
                layoutDecoratedWithMargins(itemView, left, top, right, bottom);
                startX += getDecoratedMeasuredWidth(itemView);
            }
        }
    }

    private void recycle(LayoutState layoutState, RecyclerView.Recycler recycler) {
        View childView;
        if (layoutState.mLayoutDirection < LayoutState.LAYOUT_TO_HEAD) {
            // 手指从上往下滑动
            int removeCount = 0;
            for (int i = getChildCount() - 1; i > 0; i--) {
                childView = getChildAt(i);
                if (getDecoratedTop(childView) + layoutState.mAbsDy > getHeight()) {
                    removeCount++;
                } else {
                    break;
                }
            }
            if (removeCount > 0) {
                log(TAG, "up to down, removeCount=%d, startIndex=%d, endIndex=%d, childCount=%d", removeCount, getChildCount() - 1, getChildCount() - 1 - removeCount, getChildCount());
                removeAndRecycleViews(recycler, getChildCount() - 1, getChildCount() - 1 - removeCount);
            }
        } else {
            int removeCount = 0;
            for (int i = 0, count = getChildCount(); i < count; i++) {
                childView = getChildAt(i);
                if (getDecoratedBottom(childView) - layoutState.mAbsDy < 0) {
                    removeCount++;
                } else {
                    break;
                }
            }
            if (removeCount > 0) {
                log(TAG, "down to up, removeCount=%d, startIndex=0, endIndex=%d, childCount=%d", removeCount, removeCount, getChildCount());
                removeAndRecycleViews(recycler, 0, removeCount);
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
        mPositionRecoder.clear();
        detachAndScrapAttachedViews(recycler);

        mLayoutState.mScrollOffset = 0;
        mLayoutState.updateBounds(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());

        View itemView;
        int startX = getPaddingLeft();
        int startY = getPaddingTop();
        int rowIndex = 0;
        for (int i = 0, size = state.getItemCount(); i < size; i++) {
            itemView = recycler.getViewForPosition(i);
            addView(itemView);
            measureChildWithMargins(itemView, 0, 0);
            if (startX + getDecoratedMeasuredWidth(itemView) > mLayoutState.mRightBounds) {
                startX = getPaddingLeft();
                startY += getDecoratedMeasuredHeight(itemView);
                rowIndex++;
                if (startY > mLayoutState.mBottomBounds) {
                    detachAndScrapView(itemView, recycler);
                    log("index=%d, startY(%d) > mVerticalSpace(%d), end", i, startY, mLayoutState.mBottomBounds);
                    break;
                }
            }

            mPositionRecoder.update(rowIndex, i);
            int left = startX;
            int top = startY;
            int right = startX + getDecoratedMeasuredWidth(itemView);
            int bottom = startY + getDecoratedMeasuredHeight(itemView);
            Rect rect = new Rect(left, top, right, bottom);
            layoutDecoratedWithMargins(itemView, left, top, right, bottom);
            log("index=%d, startX=%d, startY=%d", i, startX, startY);
            startX += getDecoratedMeasuredWidth(itemView);
        }
        log("onLayoutChildren end");
    }

    private void log(String format, Object... args) {
        UL.Companion.d(TAG, String.format(Locale.getDefault(), format, args));
    }
}
