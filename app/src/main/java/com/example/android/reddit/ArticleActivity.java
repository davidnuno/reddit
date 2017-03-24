package com.example.android.reddit;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ArticleActivity extends AppCompatActivity implements LoaderCallbacks<List<Article>> {

    public static final String LOG_TAG = ArticleActivity.class.getName() + "Steps => ";
    /**
     * Constant value for the article loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int ARTICLE_LOADER_ID = 1;
    private static String REDDIT_FRONT_PAGE
            = "https://www.reddit.com/.json";

    /**
     * The {@link ArticleAdapter} is used to view the various articles through a ListView.
     */
    private ArticleAdapter mAdapter;

    /**
     * The {@link ProgressBar} while app is loading.
     */
    private ProgressBar mProgressBar;

    /**
     * The {@link TextView} that will be visible if no data is found.
     */
    private TextView mEmptyStateTextView;

    private SwipeRefreshLayout swipeContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.article_activity);

        mProgressBar = (ProgressBar) findViewById(R.id.loading_spinner);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);

        ListView articleListView = (ListView) findViewById(R.id.article_list);

        articleListView.setEmptyView(mEmptyStateTextView);

        mAdapter = new ArticleAdapter(ArticleActivity.this, new ArrayList<Article>());

        if (articleListView != null)
            articleListView.setAdapter(mAdapter);

        //When the user clicks on a specific article, this will link them to the site.
        articleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Article currentArticle = mAdapter.getItem(position);

                //Create the URI from the article URL
                Uri articleUri = Uri.parse(currentArticle.getUrl());

                //Create the intent
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, articleUri);

                //Send user to article site
                startActivity(websiteIntent);
            }
        });

        //Start Connectivity Manager
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        //Get the network activity
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        //If connected, launch LoaderManager
        if (isConnected) {
            LoaderManager loaderManager = getLoaderManager();

            loaderManager.initLoader(ARTICLE_LOADER_ID, null, this);
        } else {
            mProgressBar.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                LoaderManager loaderManager = getLoaderManager();

                loaderManager.restartLoader(ARTICLE_LOADER_ID, null, ArticleActivity.this);

                //Set Progress Bar to visible while loader refreshes
                mProgressBar.setVisibility(View.VISIBLE);

                swipeContainer.setRefreshing(false);
            }
        });
    }//END OF onCreate METHOD

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_refresh: {

                LoaderManager loaderManager = getLoaderManager();

                //Restart the loader
                loaderManager.restartLoader(ARTICLE_LOADER_ID, null, this);

                //Set the Progress Bar to visible
                mProgressBar.setVisibility(View.VISIBLE);

                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Loader<List<Article>> onCreateLoader(int i, Bundle bundle) {

        return new ArticleLoader(this, REDDIT_FRONT_PAGE);
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> earthquakes) {

        // Set empty state text to display "No articles found."
        mEmptyStateTextView.setText(R.string.no_articles_found);
        mProgressBar.setVisibility(View.GONE);

        Log.v(LOG_TAG, "onLoadFinished");
        // Clear the adapter of previous earthquake data
        mAdapter.clear();

        // If there is a valid list of {@link Article}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (earthquakes != null && !earthquakes.isEmpty()) {
            mAdapter.addAll(earthquakes);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {

        Log.v(LOG_TAG, "onLoaderReset");
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }
}
