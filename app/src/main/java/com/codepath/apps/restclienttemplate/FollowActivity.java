package com.codepath.apps.restclienttemplate;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class FollowActivity extends AppCompatActivity {

    public final static String TAG = "FollowActivity";

    public static TwitterClient client;
    //TweetAdapter tweetAdapter;
    User user;
    ArrayList<User> users;
    RecyclerView rvFollow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow);

        client = TwitterApp.getRestClient(this);
        user = Parcels.unwrap(getIntent().getParcelableExtra("user"));

        rvFollow = findViewById(R.id.rvTweet);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        rvFollow.setLayoutManager(linearLayoutManager);

        users = new ArrayList<>();
//        tweetAdapter = new TweetAdapter(users);
//        rvFollow.setAdapter(tweetAdapter);

        setupActionBar();

        getFollows();
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.twitter_blue)));

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("");
        actionBar.setLogo(R.drawable.ic_icon);
        actionBar.setDisplayUseLogoEnabled(true);
    }

    private void getFollows() {
        client.getFollows(user.uid, true, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d(TAG, response.toString());

                try {
                    JSONArray usersJSONArray = response.getJSONArray("users");
                    for (int i = 0; i < usersJSONArray.length(); i++) {
                        User user = User.fromJSON(usersJSONArray.getJSONObject(i));

                        users.add(user);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d(TAG, response.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d(TAG, responseString);
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d(TAG, errorResponse.toString());
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d(TAG, errorResponse.toString());
                throwable.printStackTrace();
            }
        });
    }
}
