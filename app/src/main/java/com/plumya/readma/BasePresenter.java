package com.plumya.readma;

/**
 * Created by miltomasz on 25/06/17.
 */

public interface BasePresenter<V extends BaseView> {
    void attachView(V view);
    void detachView();
}
