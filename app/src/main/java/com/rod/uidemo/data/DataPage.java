package com.rod.uidemo.data;

import android.support.annotation.NonNull;

/**
 * @author Rod
 * @date 2018/9/7
 */
public interface DataPage<D extends DataUnit> {

    @NonNull
    D generateDataUnit();

    @NonNull
    String generatePageId();
}
