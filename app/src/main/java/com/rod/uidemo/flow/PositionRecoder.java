package com.rod.uidemo.flow;

import android.util.SparseArray;
import android.util.SparseIntArray;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Rod
 * @date 2018/8/2
 */
final class PositionRecoder {
    private final static int NOT_FOUND = Integer.MIN_VALUE;

    private final SparseIntArray mColumnMap = new SparseIntArray();
    private final SparseArray<Set<Integer>> mRowMap = new SparseArray<>();

    void clear() {
        mColumnMap.clear();
        mRowMap.clear();
    }

    int getRowIndex(int position) {
        return mColumnMap.get(position, NOT_FOUND);
    }

    void update(int rowIndex, int position) {
        updateColumn(rowIndex, position);
        updateRow(rowIndex, position);
    }

    private void updateColumn(int rowIndex, int position) {
        mColumnMap.put(position, rowIndex);
    }

    private void updateRow(int rowIndex, int position) {
        Set<Integer> columnSet = mRowMap.get(rowIndex);
        if (columnSet == null) {
            columnSet = new LinkedHashSet<>();
            mRowMap.put(rowIndex, columnSet);
        }
        columnSet.add(position);
    }
}
