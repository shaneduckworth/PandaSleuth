package com.backcountrydesigngroup.android.pandasleuth;
/**     Copyright (C) 2016 The Android Open Source Project

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 **/
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.util.List;

public class ArticleLoader extends AsyncTaskLoader<List<article>> {

    public static String mUrl;
    static final String LOG_TAG = PandaQuery.class.getSimpleName();

    public ArticleLoader(Context context, String url) {
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
        return PandaQuery.getArticles(mUrl);
    }
}
