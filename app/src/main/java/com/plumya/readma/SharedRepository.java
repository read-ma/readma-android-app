package com.plumya.readma;

import android.support.annotation.NonNull;

import com.plumya.readma.data.model.Article;
import com.plumya.readma.data.model.ArticleDetailWrapper;
import com.plumya.readma.data.source.local.PreferenceHelper;
import com.plumya.readma.data.source.remote.ArticlesService;

import rx.Observable;

/**
 * Created by miltomasz on 15/07/17.
 */

public class SharedRepository {

    @NonNull
    private ArticlesService mArticlesService;
    private PreferenceHelper mPreferenceHelper;

    public SharedRepository(@NonNull ArticlesService articlesService,
                            @NonNull PreferenceHelper preferenceHelper) {
        mArticlesService = articlesService;
        mPreferenceHelper = preferenceHelper;
    }

    public Observable<ArticleDetailWrapper> saveArticle(Article article) {
        ArticleDetailWrapper articleWrapper = new ArticleDetailWrapper();
//        articleWrapper.article = article;
        String authKey = mPreferenceHelper.getAuthToken().get();

        return mArticlesService.saveArticle(articleWrapper, authKey);
    }
}
