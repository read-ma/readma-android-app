package com.plumya.readma.data.source.remote;

import com.plumya.readma.data.model.ArticleDetailWrapper;
import com.plumya.readma.data.model.ArticlesWrapper;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by miltomasz on 15/07/17.
 */

public interface ArticlesService {
    @Headers("Accept:application/json")
    @GET("/api/articles")
    Observable<ArticlesWrapper> getArticles(@Query("auth_token") String authToken);

    @Headers("Accept:application/json")
    @GET("/api/articles/{id}")
    Observable<ArticleDetailWrapper> getArticle(@Path("id") long id,
                                                @Query("auth_token") String authenticationToken);

    @Headers("Accept:application/json")
    @POST("/api/articles")
    Observable<ArticleDetailWrapper> saveArticle(@Body ArticleDetailWrapper articleWrapper,
                                                 @Query("auth_token") String authenticationToken);
}
