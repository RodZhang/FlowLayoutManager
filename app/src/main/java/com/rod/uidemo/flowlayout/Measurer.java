package com.rod.uidemo.flowlayout;

import android.support.annotation.NonNull;

/**
 * @author Rod
 * @date 2018/8/21
 */
public interface Measurer {

    int measure(@NonNull FlowLayout.LayoutProperty property);
}
