package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

    //instances
    private TwitterClient client;
    TweetAdapter tweetAdapter;
    ArrayList<Tweet> tweets;
    RecyclerView rvTweets;

    //request code for startActivity
    static final int COMPOSE_ACTIVITY_REQUEST= 1;  // The request code


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        //get the client
        client = TwitterApp.getRestClient(this);

        //find RecyclerView
        rvTweets = findViewById(R.id.rvTweet);

        //init the arraylist
        tweets = new ArrayList<>();

        //constructor the adapter from this datasource
        tweetAdapter = new TweetAdapter(tweets);

        //RecyclerView setup (layout manager, use adapter)
        rvTweets.setLayoutManager(new LinearLayoutManager(this));

        //set the adapter
        rvTweets.setAdapter(tweetAdapter);

        //populate timeline
        populateTimeline();
    }

    //get timeline results and show it on the table
    private void populateTimeline() {

        //make network request to get Twitter data
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("TwitterClient", response.toString());
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
//                Log.d("TwitterClient", response.toString());
                //iterate through the JSON array

                //for each entry, deserialize the JSON object
                for (int i = 0; i < response.length(); i++) {
                    try {
                        //convert each object to a Tweet model
                        Tweet tweet = Tweet.fromJSON(response.getJSONObject(i));

                        //add that Tweet model to our data source
                        tweets.add(tweet);

                        //notify the adapter that we've added and item
                        tweetAdapter.notifyItemInserted(tweets.size()-1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TwitterClient", responseString);
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
            }
        });
    }


    //action item in menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //get timeline menu
        getMenuInflater().inflate(R.menu.timeline, menu);

        //get tweet button
        MenuItem tweetItem = menu.findItem(R.id.miTweet);


        return true;
    }


    //set actions for menu taps

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.miTweet) {
            Intent i = new Intent(this, ComposeActivity.class);
            startActivityForResult(i, COMPOSE_ACTIVITY_REQUEST);
        }
        return super.onOptionsItemSelected(item);


    }
}
