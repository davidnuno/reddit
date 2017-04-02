package com.example.android.reddit;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * This class extends AsyncTaskLoader and is used to deal with loading an {@link Article} using JSON.
 */

public class ArticleLoader extends AsyncTaskLoader<List<Article>> {

    /**
     * Tag for log messages
     */
    private static final String LOG_TAG = ArticleLoader.class.getName() + "Steps => ";

    /**
     * Query URL
     */
    private String mUrl;

    public ArticleLoader(Context context, String url) {

        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {

        Log.v(LOG_TAG, "onStartLoading");
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<Article> loadInBackground() {

        Log.v(LOG_TAG, "loadInBackground");
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of Articles.
        List<Article> articles = QueryUtils.fetchArticleData(mUrl);

        return articles;
    }
}
