package com.plumya.readma.articles;

import com.plumya.readma.BasePresenter;
import com.plumya.readma.BaseView;
import com.plumya.readma.data.model.Article;

import java.util.List;

/**
 * Created by miltomasz on 15/07/17.
 */

public interface ArticlesContract {
    interface View extends BaseView {
        void setLoadingIndicator(boolean active);
        void showError(String msg);
        void showArticles(List<Article> articles);
        void showArticlesEmpty();
        void showLoginScreen();
        void showArticleScreen();
    }
    interface Presenter extends BasePresenter {
        void loadArticles();
        void dispatchFlow();
    }
}
