package com.plumya.readma.share;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.plumya.readma.data.model.Article;
import com.plumya.readma.service.SaveArticleService;

/**
 * Created by miltomasz on 10/08/16.
 */

public class ShareReadMaActivity extends Activity {

    private static final String TAG = ShareReadMaActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        String url = intent.getStringExtra(Intent.EXTRA_TEXT);

        String extractedUrl = url.substring(url.lastIndexOf("http"));

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
        startService(intent);
    }
}
