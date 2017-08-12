package com.plumya.readma.articledetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
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
    public static final String ARTICLE_IMAGE = "articleImage";

    private ArticleDetailContract.Presenter mArticleDetailPresenter;

    @BindView(R.id.article_detail_progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.content)
    TextView mContent;
    @BindView(R.id.article_image)
    ImageView mArticleImage;
    @BindView(R.id.main_collapsing)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.main_appbar)
    AppBarLayout mAppBarLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_details);
        ButterKnife.bind(this);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ArticlesService articlesService = ServiceFactory.createService(ArticlesService.class);
        mArticleDetailPresenter = new ArticleDetailPresenter(
                new ArticlesRepository(articlesService, new PreferenceHelper(this)),
                SchedulerProvider.getInstance()
        );
        mArticleDetailPresenter.attachView(this);

        long articleId = getIntent().getLongExtra(ARTICLE_ID, -1);
        String image = getIntent().getStringExtra(ARTICLE_IMAGE);
        mArticleDetailPresenter.loadArticle(articleId, image);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mArticleDetailPresenter != null) {
            mArticleDetailPresenter.detachView();
        }
    }

    @Override
    public void showTitle(String title) {
        mTitle.setText(title);
        mCollapsingToolbarLayout.setTitle(title);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                String collapsedTitle = getString(R.string.app_name);
                if ((Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange()) == 0) {
                    //  Collapsed
                    mCollapsingToolbarLayout.setTitle(collapsedTitle);
                } else {
                    //Expanded
                    mCollapsingToolbarLayout.setTitle(title);
                }
            }
        });
    }

    @Override
    public void showContent(String content) {
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

    @Override
    public void showArticleImage(String image) {
        Glide.with(this)
                .load(image)
                .priority(Priority.LOW)
                .into(mArticleImage);
    }
}
