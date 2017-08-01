package com.plumya.readma.articles;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.common.base.Optional;
import com.plumya.readma.BaseView;
import com.plumya.readma.data.model.Article;
import com.plumya.readma.data.model.ArticlesWrapper;
import com.plumya.readma.util.SchedulerProvider;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by miltomasz on 15/07/17.
 */

public class ArticlesPresenter implements ArticlesContract.Presenter {

    private static final String TAG = ArticlesPresenter.class.getSimpleName();
    private ArticlesRepository mArticlesRepository;
    private ArticlesContract.View mArticlesView;
    private final SchedulerProvider mSchedulerProvider;

    @NonNull
    private Subscription mSubscription;

    public ArticlesPresenter(ArticlesRepository repository, SchedulerProvider schedulerProvider) {
        mArticlesRepository = repository;
        mSchedulerProvider = schedulerProvider;
        mSubscription = new CompositeSubscription();
    }

    @Override
    public void attachView(BaseView view) {
        mArticlesView = (ArticlesContract.View) view;
    }

    @Override
    public void detachView() {
        mArticlesView = null;
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
    }

    @Override
    public void dispatchFlow() {
        Optional<String> authKey = mArticlesRepository.getPreferenceHelper().getAuthToken();
        if (authKey.isPresent()) {
            mArticlesView.showArticlesScreen();
        } else {
            mArticlesView.showLoginScreen();
        }
    }

    @Override
    public void openArticleDetails(Article article) {
        mArticlesView.showArticleDetailsScreen(article.id);
    }

    @Override
    public void loadArticles() {
        mArticlesView.setLoadingIndicator(true);
        Optional<String> authKey = mArticlesRepository.getPreferenceHelper().getAuthToken();
        mSubscription = mArticlesRepository.getArticles(authKey.get())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Observer<ArticlesWrapper>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "Articles request completed.");
                        mArticlesView.setLoadingIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "Articles request error!" + e.getMessage());
                        mArticlesView.showError("Error while loading articles");
                    }

                    @Override
                    public void onNext(ArticlesWrapper articleWrapper) {
                        if (articleWrapper != null && !articleWrapper.articles.isEmpty()) {
                            Observable.from(articleWrapper.articles)
                                    .filter(new Func1<Article, Boolean>() {
                                        @Override
                                        public Boolean call(Article article) {
                                            return article.adminUserId != null;
                                        }
                                    })
                                    .toList()
                                    .subscribe(new Action1<List<Article>>() {
                                        @Override
                                        public void call(List<Article> articles) {
                                            mArticlesView.showArticles(articleWrapper.articles);
                                        }
                                    });
                        } else {
                            mArticlesView.showArticlesEmpty();
                        }
                    }
                });
    }
}
