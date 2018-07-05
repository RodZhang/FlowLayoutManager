package com.rod.uidemo.loadingcomponent;

import android.support.v4.app.Fragment;

import com.rod.uidemo.loadingcomponent.annotations.RefreshType;

/**
 * No pains, no gains.
 * <p>
 * Created by Rod on 2018/7/6.
 */
public abstract class LoadingFragment extends Fragment {

    protected abstract void startRefresh(@RefreshType int refreshType);

    protected abstract void endRefresh(@RefreshType int refreshType);

}
