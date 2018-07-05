package com.rod.uidemo.loadingcomponent.annotations;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * No pains, no gains.
 * <p>
 * Created by Rod on 2018/7/6.
 */
@IntDef({RefreshType.REPLACE_ALL, RefreshType.LOAD_MORE})
@Retention(RetentionPolicy.SOURCE)
public @interface RefreshType {

    int REPLACE_ALL = 0;
    int LOAD_MORE = 1;

}
