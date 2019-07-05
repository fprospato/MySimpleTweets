package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

@Parcel
public class User {

    public String name;
    public long uid;
    public String screenName;
    public String profileImageUrl;
    public String backgroundImageUrl;

    public String description;
    public String created_at;
    public int friendCount;
    public int followerCount;


    //constructor for Parcel
    public User() {}


    /*
     * deserialize the JSON
     */
    public static User fromJSON(JSONObject json) throws JSONException {
        User user = new User();

        user.name = json.getString("name");
        user.uid = json.getLong("id");
        user.screenName = json.getString("screen_name");
        user.profileImageUrl = json.getString("profile_image_url_https");
        user.backgroundImageUrl = json.getString("profile_banner_url");

        user.description = json.getString("description");
        user.friendCount = json.getInt("friends_count");
        user.followerCount = json.getInt("followers_count");

        String createdAt = json.getString("created_at");
        user.created_at = getJoinedDate(createdAt);

        return user;
    }

    private static String getJoinedDate(String created_at) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);

        String dateString = "";
        try {
            long dateMillis =  sf.parse(created_at).getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("MMMM yyyy", Locale.US);
            dateString = formatter.format(dateMillis);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateString;
    }
}
