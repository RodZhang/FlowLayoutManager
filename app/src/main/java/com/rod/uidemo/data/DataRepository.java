package com.rod.uidemo.data;

import android.support.annotation.Nullable;

/**
 * @author Rod
 * @date 2018/9/7
 */
interface DataRepository {

    <T extends DataUnit> void put(String key, T dataUnit);

    void remove(String key);

    @Nullable
    <T extends DataUnit> T get(String key);
}
