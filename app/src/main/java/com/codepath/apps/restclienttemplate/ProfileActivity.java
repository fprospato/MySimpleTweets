package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.graphics.Color;
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
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.text.NumberFormat;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class ProfileActivity extends AppCompatActivity {

    ImageView ivProfileImage;
    ImageView ivBackgroundImage;
    TextView tvName;
    TextView tvUsername;
    TextView tvBio;
    TextView tvJoined;
    TextView tvFriendCount;
    TextView tvFollowerCount;
    Button btnFollow;
    Button btnFollowing;

    private TwitterClient client;
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        client = TwitterApp.getRestClient(this);
        user = Parcels.unwrap(getIntent().getParcelableExtra("user"));

        ivProfileImage = findViewById(R.id.ivProfileImage);
        tvName = findViewById(R.id.tvName);
        tvUsername = findViewById(R.id.tvUsername);
        tvBio = findViewById(R.id.tvBio);
        tvJoined = findViewById(R.id.tvJoined);
        tvFriendCount = findViewById(R.id.tvFriendCount);
        tvFollowerCount = findViewById(R.id.tvFollowerCount);
        ivBackgroundImage = findViewById(R.id.ivBackgroundImage);
        btnFollow = findViewById(R.id.btnFollow);
        btnFollowing = findViewById(R.id.btnFollowing);

        setupActionBar();

        populateView();

        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!user.following) {
                    sendFollow();
                } else {
                    deleteFollow();
                }
            }
        });

        btnFollowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToFollowView();
            }
        });
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.twitter_blue)));

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("");
        actionBar.setLogo(R.drawable.ic_icon);
        actionBar.setDisplayUseLogoEnabled(true);
    }

    private void populateView() {
        tvName.setText(user.name);
        tvUsername.setText("@" + user.screenName);
        tvBio.setText(user.description);
        tvJoined.setText("Joined " + user.created_at);

        NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
        tvFriendCount.setText(String.valueOf(numberFormat.format(user.friendCount)));
        tvFollowerCount.setText(String.valueOf(numberFormat.format(user.followerCount)));

        Glide.with(getApplicationContext())
                .load(user.profileImageUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(ivProfileImage);

        Glide.with(getApplicationContext())
                .load(user.backgroundImageUrl)
                .into(ivBackgroundImage);

        changeFollowBtnDesign();
    }

    private void sendFollow() {
        client.sendFollow(user.uid,  new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("TwitterClient", response.toString());

                user.following = true;

                changeFollowBtnDesign();
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

    private void deleteFollow() {
        client.deleteFollow(user.uid,  new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("TwitterClient", response.toString());

                user.following = false;

                changeFollowBtnDesign();
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

    private void goToFollowView() {
        Intent i = new Intent(getApplicationContext(), FollowActivity.class);
        i.putExtra("user", Parcels.wrap(user));
        startActivity(i);
    }


    private void changeFollowBtnDesign() {
        if (user.following) {
            btnFollow.setText("Following");
            btnFollow.setTextColor(Color.parseColor("#FFFFFF"));
            btnFollow.setBackground(new ColorDrawable(getResources().getColor(R.color.twitter_blue)));
        } else {
            btnFollow.setText("Follow");
            btnFollow.setTextColor(Color.parseColor("#1DA1F2"));
            btnFollow.setBackground(new ColorDrawable(getResources().getColor(R.color.white)));
        }
    }
}
