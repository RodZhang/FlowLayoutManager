package com.rod.uidemo.flow;

import android.graphics.Rect;

import java.util.Objects;

/**
 * @author Rod
 * @date 2018/8/5
 */
class ItemInfo {
    final Rect mRect;
    final int mRowIndex;
    final int mIndexInAdapter;
    final int mIndexInRow;

    public ItemInfo(Rect rect, int rowIndex, int indexInAdapter, int indexInRow) {
        mRect = rect;
        mRowIndex = rowIndex;
        mIndexInAdapter = indexInAdapter;
        mIndexInRow = indexInRow;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ItemInfo itemInfo = (ItemInfo) o;
        return mRowIndex == itemInfo.mRowIndex &&
                mIndexInAdapter == itemInfo.mIndexInAdapter &&
                mIndexInRow == itemInfo.mIndexInRow;
    }

    @Override
    public int hashCode() {
        return Objects.hash(mRowIndex, mIndexInAdapter, mIndexInRow);
    }
}
