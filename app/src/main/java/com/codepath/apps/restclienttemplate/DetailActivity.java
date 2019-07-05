package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcels;

import cz.msebera.android.httpclient.Header;

public class DetailActivity extends AppCompatActivity {

    //instances
    private TwitterClient client;
    ImageView ivProfileImage;
    TextView tvName;
    TextView tvUsername;
    TextView tvBody;
    TextView tvRetweetCount;
    TextView tvFavCount;
    ImageView ivFav;

    Button btnReply;
    Button btnFav;
    Button btnProfile;

    Tweet tweet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.twitter_blue)));

        client = TwitterApp.getRestClient(this);

        ivProfileImage = findViewById(R.id.ivProfileImage);
        tvName = findViewById(R.id.tvName);
        tvUsername = findViewById(R.id.tvBio);
        tvBody = findViewById(R.id.tvBody);
        tvRetweetCount = findViewById(R.id.tvRetweetCount);
        tvFavCount = findViewById(R.id.tvFavCount);
        btnReply = findViewById(R.id.btnReply);
        btnFav = findViewById(R.id.btnFav);
        ivFav = findViewById(R.id.ivFav);
        btnProfile = findViewById(R.id.btnProfile);

        tweet = Parcels.unwrap(getIntent().getParcelableExtra("tweet"));

        populateDetails();

        setupButtons();
    }

    private void populateDetails() {
        tvName.setText(tweet.user.name);
        tvUsername.setText("@" + tweet.user.screenName);
        tvBody.setText(tweet.body);
        tvRetweetCount.setText(tweet.retweetCount);
        tvFavCount.setText(tweet.favoriteCount);

        if (tweet.favorited.equals("true")) {
            ivFav.setImageResource(R.drawable.ic_vector_heart);

            //Twitter API does not give the like count for the first hour of the tweet post. This is just so the user can visually see that they have liked it
            if (tvFavCount.getText().toString().equals("0")) {
                tvFavCount.setText("1");
            }

        } else {
            ivFav.setImageResource(R.drawable.ic_vector_heart_stroke);
        }

        Glide.with(this).load(tweet.user.profileImageUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(ivProfileImage);
    }

    private void setupButtons() {
        btnReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), ComposeActivity.class);
                i.putExtra("type", "reply");
                i.putExtra("username", "@" + tweet.user.screenName);
                startActivity(i);
            }
        });

        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tweet.favorited.equals("true")) {
                    sendFav();
                } else {
                    deleteFav();
                }
            }
        });

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToProfileView();
            }
        });

        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToProfileView();
            }
        });

        tvUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToProfileView();
            }
        });
    }

    private void sendFav() {
        client.sendFavorite(String.valueOf(tweet.uid), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("TwitterClient", response.toString());

                Integer currentCount = Integer.valueOf(tvFavCount.getText().toString());
                currentCount++;
                tvFavCount.setText(String.valueOf(currentCount));

                ivFav.setImageResource(R.drawable.ic_vector_heart);

                tweet.favorited = "true";
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TwitterClient", responseString);
            }
        });
    }


    private void deleteFav() {
        client.deleteFavorite(String.valueOf(tweet.uid), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("TwitterClient", response.toString());

                Integer currentCount = Integer.valueOf(tvFavCount.getText().toString());
                currentCount--;
                tvFavCount.setText(String.valueOf(currentCount));

                ivFav.setImageResource(R.drawable.ic_vector_heart_stroke);

                tweet.favorited = "false";
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TwitterClient", responseString);
            }
        });
    }

    private void goToProfileView() {
        Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
        i.putExtra("user", Parcels.wrap(tweet.user));
        startActivity(i);
    }
}
