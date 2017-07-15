package com.plumya.readma.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.plumya.readma.data.model.User;
import com.plumya.readma.data.source.local.PreferenceHelper;
import com.plumya.readma.data.source.remote.LoginService;
import com.plumya.readma.login.LoginRequest;

import rx.Observable;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by miltomasz on 25/06/17.
 */

public class LoginRepository {

    @Nullable
    private static LoginRepository INSTANCE = null;

    // local data source
    @Nullable
    private final PreferenceHelper mPreferenceHelper;

    // remote data source
    @NonNull
    private LoginService mLoginService;

    private LoginRepository(@NonNull LoginService loginService,
                            @NonNull PreferenceHelper preferenceHelper) {
        mLoginService = checkNotNull(loginService);
        mPreferenceHelper = preferenceHelper;
    }

    public static LoginRepository getInstance(@NonNull LoginService loginService,
                                              @NonNull PreferenceHelper preferenceHelper) {
        if (INSTANCE == null) {
            INSTANCE = new LoginRepository(loginService, preferenceHelper);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    public Observable<User> login(String email, String password) {
        User user = new User(email, password);
        return mLoginService.login(new LoginRequest(user));
    }

    public PreferenceHelper getPreferenceHelper() {
        return mPreferenceHelper;
    }
}
