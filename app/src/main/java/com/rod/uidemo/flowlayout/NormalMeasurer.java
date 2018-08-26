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
        int lineHeight = 0;
        int startX = property.mXStartPadding;
        int startY = 0;
        ViewGroup parent = property.mParent;
        for (int i = 0; i < property.mChildCount; i++) {
            final View child = parent.getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            child.measure(property.mChildMeasureSpace, property.mChildMeasureSpace);
            lineHeight = Math.max(lineHeight, child.getMeasuredHeight());
            int childWidth = child.getMeasuredWidth();
            if (startX + childWidth > property.mXBeforeEnd) {
                if (startX == property.mXStartPadding) {
                    // 说明此行只能容下当前child做为单独一行
                    child.measure(MeasureSpec.makeMeasureSpec(property.mXSpace, MeasureSpec.AT_MOST),
                            property.mChildMeasureSpace);
                    startX = property.mXStartPadding;
                } else {
                    startX = property.mXStartPadding + childWidth + property.mPadH;
                }

                if (i != property.mChildCount - 1) {
                    startY += lineHeight + property.mPadV;
                }
            } else {
                startX += childWidth + property.mPadH;
            }
        }
        return startY + lineHeight;
    }
}
