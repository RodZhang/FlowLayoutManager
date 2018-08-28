package com.rod.uidemo.flowlayout;

import android.support.annotation.NonNull;
import android.util.SparseIntArray;
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
        int lineIndex = 0;
        int startX = property.mXStartPadding;
        SparseIntArray lineHeightMap = new SparseIntArray();

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
                lineIndex++;
                lineHeightMap.put(lineIndex, lineHeight);
                if (childWidth > property.mXSpace) {
                    child.measure(MeasureSpec.makeMeasureSpec(property.mXSpace, MeasureSpec.AT_MOST),
                            property.mChildMeasureSpace);
                    startX = property.mXStartPadding;
                    lineIndex++;
                } else {
                    startX = property.mXStartPadding + childWidth + property.mPadH;
                }
            } else {
                startX += childWidth + property.mPadH;
                lineHeightMap.put(lineIndex, lineHeight);
            }
        }
        int lineCount = lineHeightMap.size();
        int contentHeight = lineCount * property.mPadV - property.mPadV;
        for (int i = 0; i < lineCount; i++) {
            contentHeight += lineHeightMap.valueAt(i);
        }
        return contentHeight;
    }
}
