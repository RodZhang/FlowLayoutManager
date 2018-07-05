package com.rod.uidemo.loadingcomponent.annotations;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * No pains, no gains.
 * <p>
 * Created by Rod on 2018/7/6.
 */
@SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
@Retention(RetentionPolicy.SOURCE)
@IntDef({
        RequestResult.SUCCESS_REMOTE,
        RequestResult.SUCCESS_CACHE,
        RequestResult.FAIL_REMOTE,
        RequestResult.FAIL_NET_BROKEN
})
public @interface RequestResult {

    int SUCCESS_REMOTE = 0;
    int SUCCESS_CACHE = 1;
    int FAIL_REMOTE = 2;
    int FAIL_NET_BROKEN = 3;
}
