package com.example.android.reddit;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 1/13/2017.
 */

public final class QueryUtils {

    private final static String LOG_TAG = QueryUtils.class.getName();

    /** Declare the constructor as private to avoid being called. */
    private QueryUtils() {
    }

    public static List<Article> extractDataFromJson(String articleJSON) {

        if (TextUtils.isEmpty(articleJSON)) {
            return null;
        }

        List<Article> articleList = new ArrayList<>();

        try {

            JSONObject data = new JSONObject(articleJSON).getJSONObject("data");

            JSONArray articleArray = data.getJSONArray("children");

            //Loop through all of the articles.
            for (int i = 0; i < articleArray.length(); i++) {

                JSONObject currentArticle = articleArray.getJSONObject(i).getJSONObject("data");

                //Begin extracting article data from JSON data.

                Log.v(LOG_TAG, "Extracting the title...");
                //Extract the article title
                String title = currentArticle.getString("title");

                Log.v(LOG_TAG, "Extracting the subreddit...");
                //Extract the article subreddit
                String subreddit = currentArticle.getString("subreddit_name_prefixed");

                Log.v(LOG_TAG, "Extracting the url...");
                //Extract the article url
                String url = currentArticle.getString("url");

                Log.v(LOG_TAG, "Extracting the thumbnail url...");
                //Extract the image url
                String thumbnailUrl = currentArticle.getString("thumbnail");

                Log.v(LOG_TAG, "Extracting the time of post in epoch...");
                //Extract the article post time
                long dateUTC = currentArticle.getLong("created_utc");

                Log.v(LOG_TAG, "Extracting the number of comments...");
                //Extract the number of comments for article
                int numComments = currentArticle.getInt("num_comments");

                Log.v(LOG_TAG, "Extracting the domain...");
                //Extract the article domain
                String domain = currentArticle.getString("domain");

                Log.v(LOG_TAG, "Creating new Article object");

                //If a title exists then add to article list.
                if (title != null) {
                    Log.v(LOG_TAG, "Adding new article to list.");
                    articleList.add(new Article(title, url, subreddit, thumbnailUrl, dateUTC, numComments, domain));
                }
            }//END for LOOP
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the article JSON results", e);
        }
        return articleList;
    }//END OF extractDataFromJson METHOD

    private static URL createURL(String stringURL) {
        URL url = null;

        try {
            url = new URL(stringURL);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL", e);
        }
        return url;
    }//END OF createURL METHOD

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        //If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error response code: ", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return jsonResponse;
    }//END OF makeHttpRequest METHOD

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {

        StringBuilder output = new StringBuilder();

        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);

            String line = reader.readLine();

            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }//END OF readFromStream METHOD

    public static List<Article> fetchArticleData(String requestURL) {

        URL url = createURL(requestURL);

        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request", e);
        }

        List<Article> articles = extractDataFromJson(jsonResponse);

        return articles;
    }//END OF fetchArticleData METHOD
}//END OF QueryUtils CLASS
