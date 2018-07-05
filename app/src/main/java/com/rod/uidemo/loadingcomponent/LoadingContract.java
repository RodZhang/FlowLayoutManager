package com.rod.uidemo.loadingcomponent;

import com.rod.uidemo.loadingcomponent.annotations.RefreshType;
import com.rod.uidemo.loadingcomponent.annotations.RequestResult;

/**
 * No pains, no gains.
 * <p>
 * Created by Rod on 2018/7/6.
 */
public class LoadingContract {

    public interface View {

        void showLoading();

        void showContent();

        void showEmpty();

        void showError(@RequestResult int requestResult);

    }

    public interface Presenter {

        void loadData(@RefreshType int refreshType);
    }
}
