package com.plumya.readma.login;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.plumya.readma.BaseView;
import com.plumya.readma.R;
import com.plumya.readma.data.model.User;
import com.plumya.readma.data.source.LoginRepository;
import com.plumya.readma.util.SchedulerProvider;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by miltomasz on 25/06/17.
 */

public class LoginPresenter implements LoginContract.Presenter {

    private static final String TAG = LoginPresenter.class.getSimpleName();

    // model
    @NonNull
    private LoginRepository mLoginRepository;
    // view
    @NonNull
    private LoginContract.View mLoginView;

    @NonNull
    private CompositeSubscription mSubscriptions;

    @NonNull
    private final SchedulerProvider mSchedulerProvider;

    public LoginPresenter(@NonNull LoginRepository repository,
                            @NonNull SchedulerProvider schedulerProvider) {
        mLoginRepository = repository;
        mSubscriptions = new CompositeSubscription();
        mSchedulerProvider = checkNotNull(schedulerProvider, "schedulerProvider cannot be null");
    }

    @Override
    public void attachView(BaseView view) {
        mLoginView = (LoginContract.View) view;
    }

    @Override
    public void detachView() {
        mLoginView = null;
        if (mSubscriptions != null) {
            mSubscriptions.clear();
        }
    }

    @Override
    public void login(String email, String password) {
        mLoginView.clearErrors();
        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mLoginView.showPasswordErrorMessage(R.string.error_invalid_password);
        }
        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mLoginView.showEmailErrorMessage(R.string.error_field_required);
        } else if (!isEmailValid(email)) {
            mLoginView.showEmailErrorMessage(R.string.error_invalid_email);
        } else {
            mSubscriptions.clear();
            Subscription subscription = mLoginRepository.login(email, password)
                    .subscribeOn(mSchedulerProvider.computation())
                    .observeOn(mSchedulerProvider.ui())
                    .subscribe(
                            // onNext
                            this::goToArticles,
                            // onError
                            error -> mLoginView
                                    .showEmailErrorMessage(R.string.error_network_problems),
                            // onCompleted
                            () -> mLoginView.setLoadingIndicator(false)

                    );
            mSubscriptions.add(subscription);
        }
    }

    private void goToArticles(User user) {
        if (user != null) {
            Log.d(TAG, "Login successful, going to main activity");
            mLoginRepository.getPreferenceHelper()
                    .putAuthToken(user.authenticationToken);
            mLoginView.showArticles();
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }
}
