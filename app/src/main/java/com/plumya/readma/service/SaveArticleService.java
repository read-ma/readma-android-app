package com.plumya.readma.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.plumya.readma.data.model.Article;
import com.plumya.readma.data.model.SaveArticleWrapper;
import com.plumya.readma.data.source.ServiceFactory;
import com.plumya.readma.data.source.remote.ArticlesService;
import com.plumya.readma.share.SaveArticleBroadcastReceiver;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by miltomasz on 30/07/17.
 */

public class SaveArticleService extends IntentService {

    public static final String ARTICLE = "article";
    public static final String RESULT = "result";

    private static final String TAG = SaveArticleService.class.getSimpleName();
    private static final String AUTH_KEY = "authKey";

    public SaveArticleService() {
        super("SaveArticleService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Article article = intent.getParcelableExtra(ARTICLE);
        SaveArticleWrapper saveArticleWrapper = new SaveArticleWrapper(article);
        String authKey = intent.getStringExtra(AUTH_KEY);

        ArticlesService articlesService = ServiceFactory.createService(ArticlesService.class);
        // perform call and save
        Call<SaveArticleWrapper> saveArticleCall =
                articlesService.saveArticle(saveArticleWrapper, authKey);
        try {
            Response<SaveArticleWrapper> response = saveArticleCall.execute();
            if (response.isSuccessful()) {
                broadcastResult(true);
            } else {
                broadcastResult(false);
            }
        } catch (IOException e) {
            Log.d(TAG, "Exception occurred while saving article: " + e.getMessage());
            broadcastResult(false);
        }
    }

    private void broadcastResult(boolean result) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(SaveArticleBroadcastReceiver.ACTION_SAVE_ARTICLE_RESPONSE);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        broadcastIntent.putExtra(RESULT, result);
        sendBroadcast(broadcastIntent);
    }
}
