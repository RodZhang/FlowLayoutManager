package com.rod.uidemo.flow;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;

import com.rod.uidemo.UL;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Rod
 * @date 2018/8/2
 */
final class ItemRecoder {
    private static final String TAG = "ItemRecoder";


    private final SparseArray<ItemInfo> mItemMap = new SparseArray<>();
    private final SparseArray<List<ItemInfo>> mRowItemMap = new SparseArray<>();

    void clear() {
        mItemMap.clear();
        mRowItemMap.clear();
    }

    @Nullable
    ItemInfo getItemInfo(int indexInAdapter) {
        return mItemMap.get(indexInAdapter, null);
    }

    @NonNull
    List<ItemInfo> getItemsOnRow(int rowIndex) {
        return mRowItemMap.get(rowIndex, new ArrayList<ItemInfo>());
    }

    void putItemToRow(int rowIndex, ItemInfo itemInfo) {
        if (itemInfo == null) {
            return;
        }
        List<ItemInfo> items = mRowItemMap.get(rowIndex);
        if (items == null) {
            items = new ArrayList<>();
            mRowItemMap.put(rowIndex, items);
        }
        if (!items.contains(itemInfo)) {
            items.add(itemInfo);
        }
        UL.Companion.d(TAG, "putItemToRow, rowIndex=%d, top=%d", rowIndex, itemInfo.mRect.top);
        mItemMap.put(itemInfo.mIndexInAdapter, itemInfo);
    }

    int getRowItemSize(int currentRowIndex) {
        List<ItemInfo> items = getItemsOnRow(currentRowIndex);
        return items == null ? 0 : items.size();
    }
}
