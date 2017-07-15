package com.plumya.readma.articles;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.plumya.readma.R;
import com.plumya.readma.data.model.Article;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by miltomasz on 12/06/16.
 */

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ArticleViewHodler> {

    private List<Article> mArticles;
    private ClickListener mClickListener;

    public ArticlesAdapter(List<Article> mArticles) {
        this.mArticles = mArticles;
    }

    @Override
    public ArticleViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.article_item, parent, false);
        return new ArticleViewHodler(itemView);
    }

    @Override
    public void onBindViewHolder(ArticleViewHodler holder, int position) {
        Article article = mArticles.get(position);
        holder.title.setText(article.title.trim());
        holder.difficulty.setText(article.difficulty);
        holder.setArticle(article);
    }

    @Override
    public int getItemCount() {
        return mArticles.size();
    }

    public void addItems(List<Article> articles) {
        synchronized (mArticles) {
            mArticles.addAll(articles);
        }
        notifyDataSetChanged();
    }

    public void setClickListener(ClickListener clickListener) {
        mClickListener = clickListener;
    }

    public class ArticleViewHodler extends RecyclerView.ViewHolder {

        @BindView(R.id.title)
        public TextView title;
        @BindView(R.id.difficulty)
        public TextView difficulty;
        public Article mArticle;

        public ArticleViewHodler(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            difficulty = (TextView) itemView.findViewById(R.id.difficulty);
        }

        public void setArticle(Article article) {
            mArticle = article;
        }

        @OnClick(R.id.title)
        public void onTitleClick() {
            if (mClickListener != null) {
                mClickListener.onArticleClick(mArticle);
            }
        }
    }

    interface ClickListener {
        void onArticleClick(Article article);
    }
}
