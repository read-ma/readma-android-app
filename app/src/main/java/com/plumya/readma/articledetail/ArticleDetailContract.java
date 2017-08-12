package com.plumya.readma.articledetail;

import com.plumya.readma.BasePresenter;
import com.plumya.readma.BaseView;

/**
 * Created by miltomasz on 24/07/17.
 */

public class ArticleDetailContract {
    interface View extends BaseView {
        void showTitle(String title);
        void showContent(String content);
        void showErrorMessage(String msg);
        void showLoadingIndicator(boolean b);
        void showArticleImage(String image);
    }

    interface Presenter extends BasePresenter {
        void loadArticle(long articleId, String image);
    }
}
