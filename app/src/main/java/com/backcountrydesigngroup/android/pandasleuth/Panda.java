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

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class Panda extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<article>> {

    public static final String queryURL = "https://content.guardianapis.com/search?api-key=test&show-fields=byline&page-size=10"; // "https://content.guardianapis.com/search?section=environment&from-date=2000-01-01&order-by=relevance&show-fields=byline&page-size=10&q=solar&api-key=test";
    private ArticleAdapter mAdapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panda);

        // Set up the loadermanager
        getLoaderManager().initLoader(1, null, this).forceLoad();
        LoaderManager loaderManager = getLoaderManager();
        Loader<Object> loader = loaderManager.getLoader(1);


        mAdapter = new ArticleAdapter(this, new ArrayList<article>());
        // Find a reference to the {@link ListView} in the layout
        ListView articleListView = (ListView) findViewById(R.id.list);

        // set the adapter
        articleListView.setAdapter(mAdapter);

        // Set the view to show if the ListView is empty
        articleListView.setEmptyView(findViewById(R.id.emptyElement));

        // add the click listener
        articleListView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                article currentArticle = mAdapter.getItem(position);
                Uri url = Uri.parse(currentArticle.getArticleURL());
                Intent intent = new Intent(Intent.ACTION_VIEW, url);
                startActivity(intent);
            }
        });
    }

    @Override
    public Loader<List<article>> onCreateLoader(int i, Bundle bundle) {
        progressBar = (ProgressBar) findViewById(R.id.progressIndicator);
        progressBar.setVisibility(View.VISIBLE);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        String topic = sharedPrefs.getString(
                getString(R.string.settings_topic_key),
                getString(R.string.settings_topic_default)
        );

        String section = sharedPrefs.getString(
                getString(R.string.settings_section_key),
                getString(R.string.settings_section_default)
        );

        Uri baseUri = Uri.parse(queryURL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("section", section);
        uriBuilder.appendQueryParameter("order-by", orderBy);
        uriBuilder.appendQueryParameter("q", topic);

        return new ArticleLoader(Panda.this, uriBuilder.toString());
    }

    // Set the adapter on the {@link ListView}
    // so the list can be populated in the user interface
    @Override
    public void onLoadFinished(Loader<List<article>> loader, List<article> articles) {
        mAdapter.clear();
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressIndicator);
        progressBar.setVisibility(View.INVISIBLE);
        if (articles != null && !articles.isEmpty()) {
            mAdapter.addAll(articles);
        }
        TextView empty = (TextView) findViewById(R.id.emptyElement);
        empty.setText(R.string.internet_error_message);
        if (ArticleLoader.mUrl != "" && articles.isEmpty()) {
            empty.setText(R.string.no_article_error_message);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<article>> loader) {
        mAdapter.clear();
    }

    @Override
    // Inflate main menu
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    // Respond to menu items selected.
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}