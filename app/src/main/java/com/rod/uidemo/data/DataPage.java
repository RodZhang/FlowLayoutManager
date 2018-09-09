package com.rod.uidemo.data;

import android.support.annotation.NonNull;

/**
 * @author Rod
 * @date 2018/9/7
 */
public interface DataPage<D extends DataUnit> {

    /**
     * 生成DataUnit，一般一个页面只需要在全新创建的时候调用一次
     * @return DataUnit
     */
    @NonNull
    D generateDataUnit();

    /**
     * 生成页面id，用于唯一标识一个页面，大多数场景下，不同的页面实际上是同一个类的不同实例
     * @return pageId
     */
    @NonNull
    String generatePageId();
}
