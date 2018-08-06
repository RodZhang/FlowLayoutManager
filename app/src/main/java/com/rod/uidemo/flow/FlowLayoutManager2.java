package com.rod.uidemo.flow;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author Rod
 * @date 2018/8/5
 */
public class FlowLayoutManager2 extends RecyclerView.LayoutManager {

    private ItemRecoder mItemRecoder = new ItemRecoder();
    private LayoutState mLayoutState = new LayoutState();

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
        dy = fixDy(dy);

        updateLayoutState(dy);
        fillToTail(recycler);
        mLayoutState.mScrollOffset += dy;
        offsetChildrenVertical(-dy);
        return dy;
    }

    private void updateLayoutState(int dy) {
        mLayoutState.mAbsDy = Math.abs(dy);
        if (dy < 0) {
            View firstChild = getChildAt(0);
            int firstChildIndex = getPosition(firstChild);
            mLayoutState.mCurrentItemPos = firstChildIndex - 1;
            ItemInfo itemInfo = mItemRecoder.getItemInfo(firstChildIndex);
            mLayoutState.mCurrentRowIndex = itemInfo == null ? -1 : itemInfo.mRowIndex - 1;
            mLayoutState.mRemainSpace = getDecoratedTop(firstChild) - mLayoutState.mTopBounds + mLayoutState.mAbsDy;
            mLayoutState.mLayoutDirection = LayoutState.LAYOUT_TO_HEAD;
        } else {
            View lastChild = getChildAt(getChildCount() - 1);
            int lastChildIndex = getPosition(lastChild);
            mLayoutState.mCurrentItemPos = lastChildIndex + 1;
            ItemInfo itemInfo = mItemRecoder.getItemInfo(lastChildIndex);
            mLayoutState.mCurrentRowIndex = itemInfo == null ? -1 : itemInfo.mRowIndex + 1;
            int lastViewBottom = getDecoratedBottom(lastChild);
            mLayoutState.mRemainSpace = mLayoutState.mBottomBounds - lastViewBottom + mLayoutState.mAbsDy;
            mLayoutState.mYOffset = lastViewBottom;
            mLayoutState.mLayoutDirection = LayoutState.LAYOUT_TO_TAIL;
        }
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

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (state.isPreLayout()) {
            return;
        }
        removeAndRecycleAllViews(recycler);
        mItemRecoder.clear();
        resetLayoutState();
        fill(recycler);
    }

    private void resetLayoutState() {
        mLayoutState.mAbsDy = 0;
        mLayoutState.mRemainSpace = mLayoutState.mBottomBounds - mLayoutState.mTopBounds;
        mLayoutState.mLayoutDirection = LayoutState.LAYOUT_TO_TAIL;
        mLayoutState.mScrollOffset = 0;
        mLayoutState.mNeedRecycle = false;
        mLayoutState.mCurrentItemPos = 0;
        mLayoutState.mCurrentRowIndex = 0;
        mLayoutState.updateBounds(getPaddingLeft(), getPaddingTop(),
                getWidth() - getPaddingRight(),
                getHeight() - getPaddingBottom());
    }

    private void fill(RecyclerView.Recycler recycler) {
        if (mLayoutState.mNeedRecycle) {
            recycle();
        }

        if (mLayoutState.mLayoutDirection == LayoutState.LAYOUT_TO_TAIL) {
            fillToTail(recycler);
        } else {
            fillToHead(recycler);
        }
    }

    private void fillToTail(RecyclerView.Recycler recycler) {
        while (mLayoutState.mRemainSpace > 0 && mLayoutState.mCurrentItemPos < getItemCount()) {
            mLayoutState.mRemainSpace -= fillRowToTail(recycler);
        }
    }

    private int fillRowToTail(RecyclerView.Recycler recycler) {
        if (mLayoutState.mCurrentItemPos >= getItemCount()) {
            return 0;
        }
        ItemInfo itemInfo = mItemRecoder.getItemInfo(mLayoutState.mCurrentRowIndex);
        int consumed = itemInfo == null ? fillRowToTailWithNew(recycler) : fillRowToTailWithCache(recycler);
        mLayoutState.mYOffset += consumed;
        return consumed;
    }

    private int fillRowToTailWithNew(RecyclerView.Recycler recycler) {
        int startX = mLayoutState.mLeftBounds;
        View view = recycler.getViewForPosition(mLayoutState.mCurrentItemPos);
        addView(view);
        measureChildWithMargins(view, 0, 0);
        int viewHeight = getDecoratedMeasuredHeight(view);
        int viewWidth = getDecoratedMeasuredWidth(view);
        if (startX + viewWidth >= mLayoutState.mRightBounds) {
            layoutDecoratedWithMargins(view, mLayoutState.mLeftBounds, mLayoutState.mYOffset, mLayoutState.mRightBounds, mLayoutState.mYOffset + viewHeight);
            mLayoutState.mCurrentItemPos++;
            mLayoutState.mCurrentRowIndex++;
            return viewHeight;
        }

        int newStartX = startX + viewWidth;
        layoutDecoratedWithMargins(view, startX, mLayoutState.mYOffset, startX + viewWidth, mLayoutState.mYOffset + viewHeight);
        mLayoutState.mCurrentItemPos++;
        startX = newStartX;

        int maxHeight = viewHeight;
        while (mLayoutState.mCurrentItemPos < getItemCount()) {
            view = recycler.getViewForPosition(mLayoutState.mCurrentItemPos);
            addView(view);
            measureChildWithMargins(view, 0, 0);
            viewWidth = getDecoratedMeasuredWidth(view);
            newStartX = startX + viewWidth;
            if (newStartX >= mLayoutState.mRightBounds) {
                removeAndRecycleView(view, recycler);
                break;
            }

            viewHeight = getDecoratedMeasuredHeight(view);
            layoutDecoratedWithMargins(view, startX, mLayoutState.mYOffset, newStartX, mLayoutState.mYOffset + viewHeight);
            maxHeight = Math.max(maxHeight, viewHeight);
            mLayoutState.mCurrentItemPos++;
            startX = newStartX;
        }
        mLayoutState.mCurrentRowIndex++;
        return maxHeight;
    }

    private int fillRowToTailWithCache(RecyclerView.Recycler recycler) {
        return 0;
    }

    private void fillToHead(RecyclerView.Recycler recycler) {
        while (mLayoutState.mRemainSpace > 0 && mLayoutState.mCurrentItemPos >= 0) {
            mLayoutState.mRemainSpace -= fillRowToHead(recycler);
        }
    }

    private int fillRowToHead(RecyclerView.Recycler recycler) {
        return 0;
    }

    private void recycle() {

    }
}
