package com.plumya.readma.articledetail;

import android.util.Log;

import com.google.common.base.Optional;
import com.plumya.readma.BaseView;
import com.plumya.readma.articles.ArticlesRepository;
import com.plumya.readma.data.model.ArticleDetailWrapper;
import com.plumya.readma.util.SchedulerProvider;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by miltomasz on 24/07/17.
 */

public class ArticleDetailPresenter implements ArticleDetailContract.Presenter {

    private static final String TAG = ArticleDetailPresenter.class.getSimpleName();

    private ArticleDetailContract.View mArticleDetailView;
    private ArticlesRepository mArticlesRepository;
    private CompositeSubscription mSubscriptions;
    private SchedulerProvider mSchedulerProvider;

    public ArticleDetailPresenter(ArticlesRepository repository, SchedulerProvider schedulerProvider) {
        mArticlesRepository = repository;
        mSchedulerProvider = schedulerProvider;
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void attachView(BaseView view) {
        mArticleDetailView = (ArticleDetailContract.View) view;
    }

    @Override
    public void detachView() {
        mArticleDetailView = null;
        if (mSubscriptions != null) {
            mSubscriptions.unsubscribe();
        }
    }

    @Override
    public void loadArticle(long articleId, String image) {
        Optional<String> authKey = mArticlesRepository.getAuthKey();
        if (!authKey.isPresent() || articleId == -1) {
            mArticleDetailView.showLoadingIndicator(false);
            mArticleDetailView.showErrorMessage("Could not load article!");
            return;
        }
        mArticleDetailView.showLoadingIndicator(true);
        mArticleDetailView.showArticleImage(image);
        mSubscriptions.add(
                mArticlesRepository.getArticle(articleId, authKey.get())
                        .subscribeOn(mSchedulerProvider.computation())
                        .observeOn(mSchedulerProvider.ui())
                        .subscribe(
                                // onNext
                                this::displayArticle,
                                // onError
                                throwable -> {},
                                // onCompleted
                                () -> mArticleDetailView.showLoadingIndicator(false)
                        )
        );
    }

    private void displayArticle(ArticleDetailWrapper articleDetailWrapper) {
        Log.d(TAG, "Preparing article to show");
        String[][] titleArray = articleDetailWrapper.article.title;
        StringBuilder titleBuilder = new StringBuilder();
        for (int i = 0; i < titleArray[0].length; i++) {
            titleBuilder.append(titleArray[0][i]).append(" ");
        }
        String title = titleBuilder.toString();
        String content = articleDetailWrapper.article.content;

        mArticleDetailView.showTitle(title);
        mArticleDetailView.showContent(content);
    }
}
