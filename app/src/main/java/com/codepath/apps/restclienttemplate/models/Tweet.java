package com.codepath.apps.restclienttemplate.models;

import android.text.format.DateUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

@Parcel
public class Tweet {

    //list of attributes
    public String body;
    public long uid; //database ID for the tweet
    public User user;
    public String createdAt;
    public String favoriteCount;
    public String retweetCount;


    //constructor for Parcel
    public Tweet() {}


    /*
     * deserialize the JSON
     */
    public static Tweet fromJSON(JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();

        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        tweet.favoriteCount = jsonObject.getString("favorite_count");
        tweet.retweetCount = jsonObject.getString("retweet_count");

        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));

        //return tweet
        return tweet;
    }


    public String getRelativeTimeAgo(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            long dateMillis = sf.parse(rawJsonDate).getTime();
            relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                    System.currentTimeMillis(), DateUtils.FORMAT_ABBREV_RELATIVE).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //set the correct format
        String[] timeContents = relativeDate.split(" ");
        if (timeContents.length == 3) {
            relativeDate = timeContents[0] + ((timeContents[1].equals("minutes") || timeContents[1].equals("minute")) ? "m" : ((timeContents[1].equals("seconds") || timeContents[1].equals("second")) ? "s" : "h"));
        }

        return relativeDate;
    }


}
