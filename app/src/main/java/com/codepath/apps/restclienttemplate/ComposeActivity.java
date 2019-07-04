package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
    TextView tvCharCount;
    TextView tvReply;
    ImageView ivBack;
    Button btnBack;


    //request code for startActivity
    static final int COMPOSE_ACTIVITY_OK= 1;


    //character counter
    private final TextWatcher charTextWatcher= new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            tvCharCount.setText(String.valueOf(280-s.length()));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        client = TwitterApp.getRestClient(this);

        etTweetText = findViewById(R.id.etTweetText);
        tvCharCount = findViewById(R.id.tvCharCount);
        ivBack = findViewById(R.id.ivBack);
        tvReply = findViewById(R.id.tvReply);
        btnBack = findViewById(R.id.btnBack);

        etTweetText.addTextChangedListener(charTextWatcher);

        setupDesign();
    }

    private void setupDesign() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.twitter_blue)));

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        String type = getIntent().getStringExtra("type");
        if (type.equals("reply")) {
            String username = getIntent().getStringExtra("username");

            etTweetText.setText(username);
            etTweetText.setHint("Tweet your reply");

            tvReply.setVisibility(View.VISIBLE);

            etTweetText.requestFocus();
        } else {
            tvReply.setVisibility(View.GONE);
        }
    }


    public void onSendTweet(View v) {
        String tweetText = etTweetText.getText().toString().trim();

        if (tweetText.equals("")) {
            Log.d("ComposeActivity", "No text in etTweetText");
            Toast.makeText(this, "No text.", Toast.LENGTH_SHORT).show();
            return;
        }

        client.sendTweet(tweetText, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("TwitterClient", response.toString());

                Tweet tweet = null;
                try {
                    tweet = Tweet.fromJSON(response);

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
