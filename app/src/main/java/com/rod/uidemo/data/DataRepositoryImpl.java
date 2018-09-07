package com.rod.uidemo.data;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.rod.uidemo.UL;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Rod
 * @date 2018/9/7
 */
public class DataRepositoryImpl implements DataRepository {
    private static final String TAG = "DataRepositoryImpl";
    private static final DataRepositoryImpl INSTANCE = new DataRepositoryImpl();

    private Map<String, DataUnit> mUnitMap = new HashMap<>();

    private DataRepositoryImpl() {

    }

    public static DataRepositoryImpl instance() {
        return INSTANCE;
    }

    @Override
    public <T extends DataUnit> void put(String key, T dataUnit) {
        UL.Companion.d(TAG, "put(key=%s, dataUnit=%s)", key, dataUnit);
        if (TextUtils.isEmpty(key)) {
            return;
        }
        mUnitMap.put(key, dataUnit);
    }

    @Override
    public void remove(String key) {
        UL.Companion.d(TAG, "remove(key=%s)", key);
        mUnitMap.remove(key);
    }

    @Nullable
    @Override
    public <T extends DataUnit> T get(String key) {
        return (T) mUnitMap.get(key);
    }
}
