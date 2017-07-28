package com.plumya.readma.articledetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.plumya.readma.R;
import com.plumya.readma.articles.ArticlesRepository;
import com.plumya.readma.data.source.ServiceFactory;
import com.plumya.readma.data.source.local.PreferenceHelper;
import com.plumya.readma.data.source.remote.ArticlesService;
import com.plumya.readma.util.SchedulerProvider;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by miltomasz on 22/07/17.
 */

public class ArticleDetailActivity extends AppCompatActivity implements ArticleDetailContract.View {
    public static final String ARTICLE_ID = "articleId";

    private ArticleDetailContract.Presenter mArticleDetailPresenter;

    @BindView(R.id.article_detail_progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.content)
    TextView mContent;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_details);
        ButterKnife.bind(this);

        ArticlesService articlesService = ServiceFactory.createService(ArticlesService.class);
        mArticleDetailPresenter = new ArticleDetailPresenter(
                new ArticlesRepository(articlesService, new PreferenceHelper(this)),
                SchedulerProvider.getInstance()
        );
        mArticleDetailPresenter.attachView(this);

        long articleId = getIntent().getLongExtra(ARTICLE_ID, -1);
        mArticleDetailPresenter.loadArticle(articleId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mArticleDetailPresenter != null) {
            mArticleDetailPresenter.detachView();
        }
    }

    @Override
    public void showArticle(String title, String content) {
        mTitle.setText(title);
        mContent.setText(content);
    }

    @Override
    public void showErrorMessage(String message) {
        mContent.setVisibility(View.GONE);
        Snackbar.make(getCurrentFocus(), "Example Error!", Snackbar.LENGTH_LONG);
    }

    @Override
    public void showLoadingIndicator(boolean show) {
        mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }
}
