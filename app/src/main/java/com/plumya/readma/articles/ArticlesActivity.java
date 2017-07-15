package com.plumya.readma.articles;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.plumya.readma.R;
import com.plumya.readma.data.model.Article;
import com.plumya.readma.data.source.ServiceFactory;
import com.plumya.readma.data.source.local.PreferenceHelper;
import com.plumya.readma.data.source.remote.ArticlesService;
import com.plumya.readma.login.LoginActivity;
import com.plumya.readma.util.SchedulerProvider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by miltomasz on 15/07/17.
 */

public class ArticlesActivity extends AppCompatActivity
        implements ArticlesContract.View, ArticlesAdapter.ClickListener {

    private View mProgressView;

    public static final String SELECTED_ARTICLE = "selectedArticle";
    private static final String TAG = ArticlesActivity.class.getSimpleName();

    @BindView(R.id.articles_recycler_view)
    RecyclerView mRecyclerView;
    private List<Article> mArticles = new ArrayList<>();
    private ArticlesAdapter mArticlesAdapter;
    private ArticlesContract.Presenter mArticlesPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles);
        ButterKnife.bind(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.articles_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);
        mRecyclerView.setNestedScrollingEnabled(false);

        mArticlesAdapter = new ArticlesAdapter(mArticles);
        mArticlesAdapter.setClickListener(this);
        mRecyclerView.setAdapter(mArticlesAdapter);

        ArticlesService articlesService = ServiceFactory.createService(ArticlesService.class);
        mArticlesPresenter = new ArticlesPresenter(
                new ArticlesRepository(articlesService, new PreferenceHelper(this)),
                SchedulerProvider.getInstance()
        );
        mArticlesPresenter.attachView(this);
        mArticlesPresenter.dispatchFlow();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mArticlesPresenter.detachView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LoginActivity.SIGN_IN_REQUEST_CODE : {
                mArticlesPresenter.loadArticles();
                break;
            }
        }
    }

    @Override
    public void setLoadingIndicator(boolean show) {
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void showArticles(List<Article> articles) {
        mArticlesAdapter.addItems(articles);
    }

    @Override
    public void showArticlesEmpty() {

    }

    @Override
    public void showLoginScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, LoginActivity.SIGN_IN_REQUEST_CODE);
    }

    @Override
    public void showArticleScreen() {
        mArticlesPresenter.loadArticles();
    }

    @Override
    public void onArticleClick(Article article) {

    }
}
