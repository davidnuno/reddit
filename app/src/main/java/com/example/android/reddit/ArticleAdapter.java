package com.example.android.reddit;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.MutableDateTime;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * This class is used to set the ListView items.
 */

public class ArticleAdapter extends ArrayAdapter<Article> {

    private final static String LOG_TAG = ArticleAdapter.class.getName() + "Steps => ";

    /** This is a divider used to separate the article subreddit and time posted. */
    private final static String TEXT_DIVIDER = "* ";

    /** These are default thumbnails when an articles contains none. */
    private final static String THUMBNAIL_DEFAULT = "default";
    private final static String THUMBNAIL_SELF = "self";

    /**
     * Constructs a new {@link ArticleAdapter}.
     *
     * @param context  of the app.
     * @param articles is the list of articles, which is the data source of the adapter.
     */
    public ArticleAdapter(Context context, List<Article> articles) {

        super(context, 0, articles);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.article_list_item, parent, false);
        }

        //Get the current Article from the array
        Article currentArticle = getItem(position);

        //Find the titleView TextView ID
        TextView titleView = (TextView) listItemView.findViewById(R.id.article_title);

        //Set the title
        titleView.setText(currentArticle.getTitle());

        //Find the subreddit TextView ID
        TextView subredditView = (TextView) listItemView.findViewById(R.id.article_subreddit);

        //Set the subreddit
        subredditView.setText(currentArticle.getSubreddit());

        TextView time = (TextView) listItemView.findViewById(R.id.article_time);

        Log.v(LOG_TAG, "Setting date to --- " + timeAgo(currentArticle.getTimeInMilliseconds()));
        time.setText(TEXT_DIVIDER + timeAgo(currentArticle.getTimeInMilliseconds()));

        //Find the thumbnail ImageView ID
        ImageView thumbnail = (ImageView) listItemView.findViewById(R.id.article_thumbnail);

        //Find the number of comments TextView
        TextView numberComments = (TextView) listItemView.findViewById(R.id.number_of_comments);

        //Convert number of comments to String
        String comments = Integer.toString(currentArticle.getNumOfComments());

        //Set the number of comments
        numberComments.setText(comments);

        //Find the domain TextView ID
        TextView domain = (TextView) listItemView.findViewById(R.id.article_domain);

        if (currentArticle.getDomain() != StringUtils.EMPTY) {
            //Set the domain
            domain.setText(TEXT_DIVIDER + currentArticle.getDomain());
        } else {
            domain.setVisibility(View.GONE);
        }


        Log.v(LOG_TAG, "thumbnail: " + currentArticle.getThumbnailUrl());

        //If article uses a default thumbnail, set the image to GONE
        if (currentArticle.getThumbnailUrl().equals(THUMBNAIL_DEFAULT) ||
                currentArticle.getThumbnailUrl().equals(THUMBNAIL_SELF)) {

            thumbnail.setVisibility(View.GONE);
        } else {
            //Display thumbnail using the Picasso library
            Picasso.with(getContext()).load(currentArticle.getThumbnailUrl())
                    .placeholder(R.drawable.ic_reddit)
                    .transform(new RoundedCornersTransformation(10, 5))
                    .into(thumbnail);
        }

        return listItemView;
    }//END OF getView METHOD

    /**
     * Method used to format on how long ago the article was posted.
     *
     * @param epoch time the article was posted.
     * @return the time ago the article was posted, of type {@link String}.
     */
    private String timeAgo(long epoch) {

        MutableDateTime time = new MutableDateTime();

        //Set the epoch time of the article.
        time.setTime(epoch);

        //The current time
        DateTime now = new DateTime();

        Hours hoursDifference = Hours.hoursBetween(now, time);

        String text = hoursDifference.getHours() + "h";
        return text;
    }
}
