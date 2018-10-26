package com.backcountrydesigngroup.android.pandasleuth;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.util.List;

public class articleLoader extends AsyncTaskLoader<List<article>> {

    public static String mUrl;
    static final String LOG_TAG = pandaQuery.class.getSimpleName();

    public articleLoader(Context context, String url) {
        super(context);

        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        Log.e(LOG_TAG,"The network status is: " + isConnected);
        if(isConnected) {
            mUrl = url;
        } else {
            mUrl = "";
        }
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<article> loadInBackground() {
        return pandaQuery.getArticles(mUrl);
    }
}
