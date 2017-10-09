package com.plumya.readma.articledetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.plumya.readma.R;
import com.plumya.readma.articles.ArticlesRepository;
import com.plumya.readma.data.model.Translation;
import com.plumya.readma.data.source.ServiceFactory;
import com.plumya.readma.data.source.local.PreferenceHelper;
import com.plumya.readma.data.source.remote.ArticlesService;
import com.plumya.readma.data.source.remote.TranslationService;
import com.plumya.readma.translation.TranslationContract;
import com.plumya.readma.translation.TranslationPresenter;
import com.plumya.readma.translation.TranslationRepository;
import com.plumya.readma.util.SchedulerProvider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by miltomasz on 22/07/17.
 */

public class ArticleDetailActivity extends AppCompatActivity
        implements ArticleDetailContract.View, TranslationContract.View {
    public static final String ARTICLE_ID = "articleId";
    public static final String ARTICLE_IMAGE = "articleImage";
    private static final String TAG = ArticleDetailActivity.class.getSimpleName();

    private ArticleDetailContract.Presenter mArticleDetailPresenter;
    private TranslationContract.Presenter translationPresenter;

    @BindView(R.id.article_detail_progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.content)
    TextView mContent;
    @BindView(R.id.article_image)
    ImageView mArticleImage;
    @BindView(R.id.main_collapsing)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R.id.main_appbar)
    AppBarLayout mAppBarLayout;
    @BindView(R.id.bottom_sheet)
    View bottomSheet;
    @BindView(R.id.translations_rv)
    RecyclerView translationsRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_details);
        ButterKnife.bind(this);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final PreferenceHelper preferenceHelper = new PreferenceHelper(this);

        ArticlesService articlesService = ServiceFactory.createService(ArticlesService.class);
        mArticleDetailPresenter = new ArticleDetailPresenter(
                new ArticlesRepository(articlesService, preferenceHelper),
                SchedulerProvider.getInstance()
        );
        mArticleDetailPresenter.attachView(this);

        long articleId = getIntent().getLongExtra(ARTICLE_ID, -1);
        String image = getIntent().getStringExtra(ARTICLE_IMAGE);
        mArticleDetailPresenter.loadArticle(articleId, image);

        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setPeekHeight(120);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        TranslationService translationService = ServiceFactory.createService(TranslationService.class);
        translationPresenter = new TranslationPresenter(
                new TranslationRepository(translationService, preferenceHelper)
        );
        translationPresenter.attachView(this);

        SelectTextActionModeCallback selectTextActionModeCallback =
                new SelectTextActionModeCallback(translationPresenter, bottomSheetBehavior, mContent);

        mContent.setCustomSelectionActionModeCallback(selectTextActionModeCallback);
        mContent.setFocusable(false);
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
        mCollapsingToolbarLayout.setTitle(title);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                String collapsedTitle = getString(R.string.app_name);
                if ((Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange()) == 0) {
                    // Collapsed
                    mCollapsingToolbarLayout.setTitle(collapsedTitle);
                } else {
                    // Expanded
                    mCollapsingToolbarLayout.setTitle(title);
                }
            }
        });
    }

    @Override
    public void showContent(SpannableString content) {
        mContent.setText(content);
        mContent.setFocusable(true);
        mContent.setFocusableInTouchMode(true);
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

    @Override
    public void showTranslation(List<Translation> translations) {
        for (Translation t: translations) {
            Log.d(TAG, "Translations: " + t.text);
        }
        // specify an adapter (see also next example)
//        mTranslationsAdapter = new TranslationItemAdapter(getApplicationContext(), translations);
        SimpleAdapter translationsAdapter = new SimpleAdapter(this, translations);
        int translationSize = translations.size() - 1;

        //This is the code to provide a sectioned list
        List<SimpleSectionedRecyclerViewAdapter.Section> sections =
                new ArrayList<SimpleSectionedRecyclerViewAdapter.Section>();

        //Sections
        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(0,"Section 1"));
        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(translationSize,"Section 2"));
//        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(12,"Section 3"));
//        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(14,"Section 4"));
//        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(20,"Section 5"));

        //Add your adapter to the sectionAdapter
        SimpleSectionedRecyclerViewAdapter.Section[] dummy =
                new SimpleSectionedRecyclerViewAdapter.Section[sections.size()];
        SimpleSectionedRecyclerViewAdapter mSectionedAdapter =
                new SimpleSectionedRecyclerViewAdapter(
                        this, R.layout.section, R.id.section_text, translationsAdapter);
        mSectionedAdapter.setSections(sections.toArray(dummy));

        //Apply this adapter to the RecyclerView
        translationsRecyclerView.setAdapter(mSectionedAdapter);
    }

    @Override
    public void showErrorMessage() {

    }
}
