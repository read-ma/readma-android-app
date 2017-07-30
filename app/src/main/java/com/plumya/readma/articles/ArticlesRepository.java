package com.plumya.readma.articles;

import android.support.annotation.NonNull;

import com.google.common.base.Optional;
import com.plumya.readma.data.model.ArticleDetailWrapper;
import com.plumya.readma.data.model.ArticlesWrapper;
import com.plumya.readma.data.source.local.PreferenceHelper;
import com.plumya.readma.data.source.remote.ArticlesService;

import rx.Observable;

/**
 * Created by miltomasz on 15/07/17.
 */

public class ArticlesRepository {

    @NonNull
    private ArticlesService mArticlesService;
    private PreferenceHelper mPreferenceHelper;

    public ArticlesRepository(@NonNull ArticlesService articlesService,
                            @NonNull PreferenceHelper preferenceHelper) {
        mArticlesService = articlesService;
        mPreferenceHelper = preferenceHelper;
    }

    public Observable<ArticlesWrapper> getArticles(String authKey) {
        return mArticlesService.getArticles(authKey);
    }

    public Observable<ArticleDetailWrapper> getArticle(long articleId, String authKey) {
        return mArticlesService.getArticle(articleId, authKey);
    }

    public Optional<String> getAuthKey() {
        return getPreferenceHelper().getAuthToken();
    }

    public PreferenceHelper getPreferenceHelper() {
        return mPreferenceHelper;
    }
}
