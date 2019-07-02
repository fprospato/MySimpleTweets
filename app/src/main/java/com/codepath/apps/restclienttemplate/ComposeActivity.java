package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {

    //instances
    private TwitterClient client;
    EditText etTweetText;

    //request code for startActivity
    static final int COMPOSE_ACTIVITY_OK= 1;  // The request code


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        //get the client
        client = TwitterApp.getRestClient(this);

        //get edit text
        etTweetText = findViewById(R.id.etTweetText);
    }


    //send tweet button
    public void onSendTweet(View v) {
        //get text
        String tweetText = etTweetText.getText().toString().trim();

        //check if there's text and not just spaces in the tweet
        if (tweetText.equals("")) {
            Log.d("ComposeActivity", "No text in etTweetText");
            Toast.makeText(this, "No text.", Toast.LENGTH_SHORT).show();
            return;
        }


        //send tweet post
        client.sendTweet(tweetText, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("TwitterClient", response.toString());

                //make new tweet
                Tweet tweet = null;
                try {
                    tweet = Tweet.fromJSON(response);

                    //Make and send intent
                    Intent intent = new Intent();
                    intent.putExtra("tweet", Parcels.wrap(tweet));
                    setResult(COMPOSE_ACTIVITY_OK, intent);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("TwitterClient", response.toString());
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
}
