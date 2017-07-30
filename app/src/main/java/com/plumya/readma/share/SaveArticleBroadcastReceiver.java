package com.plumya.readma.share;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.plumya.readma.R;
import com.plumya.readma.service.SaveArticleService;

/**
 * Created by miltomasz on 30/07/17.
 */

public class SaveArticleBroadcastReceiver extends BroadcastReceiver {

    public static final String ACTION_SAVE_ARTICLE_RESPONSE = "com.plumya.readma.SAVE_ARTICLE";

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean resultOk = intent.getBooleanExtra(SaveArticleService.RESULT, false);
        if (resultOk) {
            String toastMsg = context.getResources().getString(R.string.article_saved_successfully);
            Toast
                    .makeText(context, toastMsg, Toast.LENGTH_LONG)
                    .show();
        } else {
            String toastMsg = context.getResources().getString(R.string.article_saved_failed);
            Toast
                    .makeText(context, toastMsg, Toast.LENGTH_LONG)
                    .show();
        }
    }
}
