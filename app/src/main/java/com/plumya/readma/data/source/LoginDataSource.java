package com.plumya.readma.data.source;

import com.plumya.readma.data.model.User;

import rx.Observable;

/**
 * Created by miltomasz on 13/07/17.
 */

public interface LoginDataSource {
    Observable<User> login(String email, String password);
}
