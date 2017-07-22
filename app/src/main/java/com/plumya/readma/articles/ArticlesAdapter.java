package com.plumya.readma.articles;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.plumya.readma.R;
import com.plumya.readma.data.model.Article;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by miltomasz on 12/06/16.
 */

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ArticleViewHolder> {

    private Context mContext;
    private List<Article> mArticles;
    private OnClickListener mClickListener;

    public ArticlesAdapter(Context context, List<Article> articles) {
        mContext = context;
        mArticles = articles;
    }

    public void setOnClickListener(OnClickListener clickListener) {
        mClickListener = clickListener;
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.article_item, parent, false);
        return new ArticleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder holder, int position) {
        Article article = mArticles.get(position);
        holder.title.setText(article.title.trim());
        Glide.with(mContext)
                .load(article.image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.LOW)
                .error(R.mipmap.ic_launcher)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e,
                                               String model,
                                               Target<GlideDrawable> target,
                                               boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource,
                                                   String model,
                                                   Target<GlideDrawable> target,
                                                   boolean isFromMemoryCache,
                                                   boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.articleImageView);
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

    public class ArticleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.article_title_tv)
        TextView title;
        @BindView(R.id.article_image_view)
        ImageView articleImageView;
        @BindView(R.id.progress_bar)
        ProgressBar progressBar;

        Article mArticle;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setArticle(Article article) {
            mArticle = article;
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Article article = mArticles.get(position);
            mClickListener.onArticleClick(article);
        }


//        @OnClick(R.id.article_title_tv)
//        public void onTitleClick() {
//            if (mClickListener != null) {
//                mClickListener.onArticleClick(mArticle);
//            }
//        }
    }

    interface OnClickListener {
        void onArticleClick(Article article);
    }
}
