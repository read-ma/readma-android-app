package com.plumya.readma.data.source.remote;

import com.plumya.readma.data.model.User;
import com.plumya.readma.login.LoginRequest;

import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by miltomasz on 25/06/17.
 */

public interface LoginService {

    @Headers("Accept:application/json")
    @POST("/api/login")
    Observable<User> login(@Body LoginRequest request);
}
