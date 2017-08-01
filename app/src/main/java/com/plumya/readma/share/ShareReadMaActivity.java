package com.plumya.readma.share;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.common.base.Optional;
import com.plumya.readma.data.model.Article;
import com.plumya.readma.data.source.local.PreferenceHelper;
import com.plumya.readma.service.SaveArticleService;

/**
 * Created by miltomasz on 10/08/16.
 */

public class ShareReadMaActivity extends Activity {

    private static final String TAG = ShareReadMaActivity.class.getSimpleName();
    private static final String HTTP_STRING = "http";
    private PreferenceHelper mPreferenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreferenceHelper = new PreferenceHelper(this);

        Intent intent = getIntent();
        String url = intent.getStringExtra(Intent.EXTRA_TEXT);

        String extractedUrl = url.substring(url.lastIndexOf(HTTP_STRING));

        Article article = new Article();
        article.sourceUrl = extractedUrl;
        article.contentType = "article";
        saveArticle(article);
        // finish activity
        finish();
    }

    private void saveArticle(Article article) {
        Log.d(TAG, "Saving article");
        Intent intent = new Intent(this, SaveArticleService.class);
        intent.putExtra(SaveArticleService.ARTICLE, article);
        Optional<String> authKey = mPreferenceHelper.getAuthToken();
        if (authKey.isPresent()) {
            intent.putExtra(SaveArticleService.AUTH_KEY, authKey.get());
        }
        startService(intent);
    }
}
