package com.plumya.readma.articledetail;

import com.plumya.readma.BasePresenter;
import com.plumya.readma.BaseView;

/**
 * Created by miltomasz on 24/07/17.
 */

public class ArticleDetailContract {
    interface View extends BaseView {
        void showArticle(String title, String content);
        void showErrorMessage(String msg);
        void showLoadingIndicator(boolean b);
    }

    interface Presenter extends BasePresenter {
        void loadArticle(long articleId);
    }
}
