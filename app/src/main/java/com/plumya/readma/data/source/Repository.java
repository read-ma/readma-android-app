package com.plumya.readma.data.source;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.common.base.Optional;
import com.plumya.readma.data.source.local.PreferenceHelper;

/**
 * Created by miltomasz on 11/07/17.
 */

public class Repository {

    protected PreferenceHelper preferenceHelper;

    public Repository(PreferenceHelper preferenceHelper) {
        this.preferenceHelper = preferenceHelper;
    }

    public Optional<String> getAuthKey() {
        return getPreferenceHelper().getAuthToken();
    }

    public PreferenceHelper getPreferenceHelper() {
        return preferenceHelper;
    }

    public SharedPreferences sharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
