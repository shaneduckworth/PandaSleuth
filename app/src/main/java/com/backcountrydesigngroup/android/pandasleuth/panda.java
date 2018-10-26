package com.backcountrydesigngroup.android.pandasleuth;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class panda extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<article>> {

    public static final String queryURL = "https://content.guardianapis.com/search?order-by=newest&show-fields=byline&page-size=10&q=solar&api-key=test";
    private articleAdapter mAdapter;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panda);

        // Set up the loadermanager
        getLoaderManager().initLoader(1, null, this).forceLoad();
        LoaderManager loaderManager = getLoaderManager();
        Loader<Object> loader = loaderManager.getLoader(1);


        mAdapter = new articleAdapter(this, new ArrayList<article>());
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
        return new articleLoader(panda.this, queryURL);
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
        empty.setText("The internet seems to be a bit elusive.  There are no news feeds.  Please check your internet connection and try again.");
        if (articleLoader.mUrl != "" && articles.isEmpty()) {
            empty.setText("There are no articles with the specified search term.  Contact the app developer to add an option to change the search term.  Sorry, life sucks for you now!");
        }
    }

    @Override
    public void onLoaderReset(Loader<List<article>> loader) {
        mAdapter.clear();
    }
}