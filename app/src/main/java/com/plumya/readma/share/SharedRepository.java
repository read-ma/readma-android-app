package com.plumya.readma.share;

import android.support.annotation.NonNull;

import com.plumya.readma.data.model.Article;
import com.plumya.readma.data.model.SaveArticleWrapper;
import com.plumya.readma.data.source.local.PreferenceHelper;
import com.plumya.readma.data.source.remote.ArticlesService;

import retrofit2.Call;

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

    public Call<SaveArticleWrapper> saveArticle(Article article) {
        SaveArticleWrapper articleWrapper = new SaveArticleWrapper(article);
        String authKey = mPreferenceHelper.getAuthToken().get();
        return mArticlesService.saveArticle(articleWrapper, authKey);
    }
}
