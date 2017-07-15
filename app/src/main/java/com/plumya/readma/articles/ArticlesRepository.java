package com.plumya.readma.articles;

import android.support.annotation.NonNull;

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

    public PreferenceHelper getPreferenceHelper() {
        return mPreferenceHelper;
    }
}
