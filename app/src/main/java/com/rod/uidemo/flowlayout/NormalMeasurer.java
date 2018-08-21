package com.rod.uidemo.flowlayout;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;

import static android.view.View.GONE;

/**
 * @author Rod
 * @date 2018/8/21
 */
public class NormalMeasurer implements Measurer {

    @Override
    public int measure(@NonNull FlowLayout.LayoutProperty property) {
        int preLineHeight = 0;
        int startX = property.mXStartPadding;
        int startY = 0;
        boolean isChangeLine;
        ViewGroup parent = property.mParent;
        for (int i = 0; i < property.mChildCount; i++) {
            final View child = parent.getChildAt(i);
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
                startX += childWidth + property.mPadH;
                isChangeLine = false;
            }
            if (isChangeLine || i == 0) {
                startY += preLineHeight + property.mPadV;
            }
        }
        return Math.max(startY - property.mPadV, 0);
    }
}
