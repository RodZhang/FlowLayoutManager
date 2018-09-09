package com.rod.uidemo.data;

/**
 * @author Rod
 * @date 2018/9/7
 */
public interface DataUnit {

    /**
     * 当被从DataRepository中移除时调用，可用于释放资源
     */
    void clear();
}
