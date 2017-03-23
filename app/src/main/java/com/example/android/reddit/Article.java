package com.example.android.reddit;

import org.apache.commons.lang3.StringUtils;

/**
 * The Article containing its information.
 */

public class Article {

    /**
     * Date article was created in UTC format.
     */
    private long mDate;

    /**
     * The article URL.
     */
    private String mUrl;

    /**
     * The title of the article.
     */
    private String mTitle;

    /**
     * The URL of article thumbnail.
     */
    private String mThumbnailUrl;

    /**
     * The article subreddit.
     */
    private String mSubreddit;

    /**
     * The number of comments in article.
     */
    private int mNumOfComments;

    /**
     * The article domain.
     */
    private String mDomain = StringUtils.EMPTY;

    /**
     * Creates a new {@link Article} object.
     *
     * @param title        of post to article.
     * @param url          link of the article.
     * @param subreddit    of the article.
     * @param thumbnailUrl of the article.
     * @param date         of when article was posted.
     */
    public Article(String title, String url, String subreddit, String thumbnailUrl, long date, int numOfComments, String domain) {

        mUrl = url;
        mTitle = title;
        mThumbnailUrl = thumbnailUrl;
        mSubreddit = subreddit;
        mDate = date;
        mNumOfComments = numOfComments;

        if (!domain.contains("reddit"))
            mDomain = domain;
    }

    /**
     * Returns the {@link Article} URL.
     *
     * @return the URL, of type {@link String}.
     */
    public String getUrl() {

        return mUrl;
    }

    /**
     * Returns the {@link Article} title.
     *
     * @return the title, of type {@link String}.
     */
    public String getTitle() {

        return mTitle;
    }

    /**
     * Returns the {@link Article} subreddit.
     *
     * @return the subreddit, of type {@link String}.
     */
    public String getSubreddit() {

        return mSubreddit;
    }

    /**
     * Returns the {@link Article} thumbnail URL.
     *
     * @return the thumbnail URL, of type {@link String}.
     */
    public String getThumbnailUrl() {

        return mThumbnailUrl;
    }

    public long getTimeInMilliseconds() {

        return mDate;
    }

    /**
     * Returns the number of comments from the {@link Article}.
     *
     * @return the number of comments.
     */
    public int getNumOfComments() {

        return mNumOfComments;
    }

    /**
     * Returns the domain of the {@link Article}.
     *
     * @return the domain, of type {@link String}.
     */
    public String getDomain() {

        return mDomain;
    }
}
