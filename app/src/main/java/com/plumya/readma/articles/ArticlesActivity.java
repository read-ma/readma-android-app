package com.plumya.readma.articles;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.crashlytics.android.Crashlytics;
import com.plumya.readma.R;
import com.plumya.readma.articledetail.ArticleDetailActivity;
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
import io.fabric.sdk.android.Fabric;

/**
 * Created by miltomasz on 15/07/17.
 */

public class ArticlesActivity extends AppCompatActivity
        implements ArticlesContract.View, ArticlesAdapter.OnClickListener {

    private static final String TAG = ArticlesActivity.class.getSimpleName();
    public static final String SELECTED_ARTICLE = "selectedArticle";

    @BindView(R.id.articles_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private List<Article> mArticles = new ArrayList<>();
    private ArticlesAdapter mArticlesAdapter;
    private ArticlesContract.Presenter mArticlesPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_articles);
        ButterKnife.bind(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.articles_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);
        mRecyclerView.setNestedScrollingEnabled(false);

        mArticlesAdapter = new ArticlesAdapter(this, mArticles);
        mArticlesAdapter.setOnClickListener(this);
        mRecyclerView.setAdapter(mArticlesAdapter);
        mRecyclerView.addOnScrollListener(mScrollListener);

        ArticlesService articlesService = ServiceFactory.createService(ArticlesService.class);
        mArticlesPresenter = new ArticlesPresenter(
                new ArticlesRepository(articlesService, new PreferenceHelper(this)),
                SchedulerProvider.getInstance()
        );
        mArticlesPresenter.attachView(this);
        mArticlesPresenter.dispatchFlow();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mArticlesPresenter.loadArticles();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mArticlesPresenter != null) {
            mArticlesPresenter.detachView();
        }
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
        mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showArticles(List<Article> articles) {
        mArticlesAdapter.setArticles(articles);
        mArticlesAdapter.notifyDataSetChanged();
        mRecyclerView.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showArticlesEmpty() {

    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void showLoginScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, LoginActivity.SIGN_IN_REQUEST_CODE);
    }

    @Override
    public void showArticleDetailsScreen(long articleId) {
        Intent intent = new Intent(this, ArticleDetailActivity.class);
        intent.putExtra(ArticleDetailActivity.ARTICLE_ID, articleId);
        startActivity(intent);
    }

    @Override
    public void showArticlesScreen() {
        mArticlesPresenter.loadArticles();
    }

    @Override
    public void onArticleClick(Article article) {
        mArticlesPresenter.openArticleDetails(article);
    }

    private RecyclerView.OnScrollListener mScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            LinearLayoutManager manager = ((LinearLayoutManager) recyclerView.getLayoutManager());
            boolean enabled =manager.findFirstCompletelyVisibleItemPosition() == 0;
            if (mSwipeRefreshLayout != null) {
                mSwipeRefreshLayout.setEnabled(enabled);
            }
        }
    };
}
