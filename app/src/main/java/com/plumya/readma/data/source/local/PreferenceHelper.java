package com.plumya.readma.data.source.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.common.base.Optional;

/**
 * Created by miltomasz on 14/07/17.
 */

public class PreferenceHelper {

    private static final String PREF_KEY_AUTH_TOKEN = "authToken";
    private final SharedPreferences mSharedPreferences;

    public PreferenceHelper(Context context) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void putAuthToken(String authToken) {
        mSharedPreferences.edit().putString(PREF_KEY_AUTH_TOKEN, authToken).commit();
    }

    public Optional<String> getAuthToken() {
        String authKey = mSharedPreferences.getString(PREF_KEY_AUTH_TOKEN, null);
        return Optional.fromNullable(authKey);
    }
}
