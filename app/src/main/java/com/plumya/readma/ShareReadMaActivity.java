package com.plumya.readma;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.plumya.readma.data.model.Article;
import com.plumya.readma.data.model.ArticleDetailWrapper;
import com.plumya.readma.data.source.ServiceFactory;
import com.plumya.readma.data.source.local.PreferenceHelper;
import com.plumya.readma.data.source.remote.ArticlesService;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by miltomasz on 10/08/16.
 */

public class ShareReadMaActivity extends Activity {

    private static final String TAG = ShareReadMaActivity.class.getSimpleName();
    private SharedRepository mSharedRepository;
    private Subscription mSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArticlesService articlesService = ServiceFactory.createService(ArticlesService.class);
        mSharedRepository = new SharedRepository(articlesService, new PreferenceHelper(this));

        Intent intent = getIntent();
        String url = intent.getStringExtra(Intent.EXTRA_TEXT);

        String extractedUrl = url.substring(url.lastIndexOf("http"));

        Article article = new Article();
        article.sourceUrl = extractedUrl;
        article.contentType = "article";
        saveArticle(article);
        finish();
    }

    private void saveArticle(Article article) {
        mSubscription = mSharedRepository.saveArticle(article)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Observer<ArticleDetailWrapper>() {
                    @Override
                    public void onCompleted() {
                        Toast.makeText(getApplicationContext(),
                                "Article added successfully!", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, e.toString());
                        Toast.makeText(getApplicationContext(),
                                "Adding article failed!", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(ArticleDetailWrapper articleWrapper) {
                        // no action
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
    }
}
