package com.plumya.readma.login;

import com.plumya.readma.BasePresenter;
import com.plumya.readma.BaseView;

/**
 * Created by miltomasz on 25/06/17.
 */

public interface LoginContract {
    interface View extends BaseView {
        void setLoadingIndicator(boolean active);
        void clearErrors();
        void showEmailErrorMessage(int msgId);
        void showPasswordErrorMessage(int msgId);
        void showArticles();
    }

    interface Presenter extends BasePresenter {
        void login(String email, String password);
    }
}
