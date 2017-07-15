package com.plumya.readma.data.source;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by miltomasz on 11/07/17.
 */

public class Repository {
    public SharedPreferences sharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }
}
