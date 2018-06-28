package com.rod.flowlayoutmanager.flow;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * No pains, no gains.
 * <p>
 * Created by Rod on 2018/6/25.
 */
public class FlowLayoutManager extends RecyclerView.LayoutManager {

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
        return super.scrollVerticallyBy(dy, recycler, state);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (state.getItemCount() == 0 || state.isPreLayout()) {
            return;
        }
        detachAndScrapAttachedViews(recycler);

        View child;
        int startX = getPaddingLeft();
        int startY = getPaddingTop();
        for (int i = 0, size = state.getItemCount(); i < size; i++) {
            child = recycler.getViewForPosition(i);
            addView(child);
            measureChildWithMargins(child, 0, 0);
            if (startX + getDecoratedMeasuredWidth(child) > getWidth() - getPaddingLeft() - getPaddingRight()) {
                startX = getPaddingLeft();
                startY += getDecoratedMeasuredHeight(child);
                layoutDecoratedWithMargins(child, startX, startY, startX + getDecoratedMeasuredWidth(child), startY + getDecoratedMeasuredHeight(child));
            } else {
                layoutDecoratedWithMargins(child, startX, startY, startX + getDecoratedMeasuredWidth(child), startY + getDecoratedMeasuredHeight(child));
                startX += getDecoratedMeasuredWidth(child);
            }
        }
    }
}
