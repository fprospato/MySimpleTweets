package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

public class DetailActivity extends AppCompatActivity {

    //instances
    ImageView ivProfileImage;
    TextView tvName;
    TextView tvUsername;
    TextView tvBody;
    TextView tvRetweetCount;
    TextView tvFavCount;
    Button btnReply;

    Tweet tweet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.twitter_blue)));

        ivProfileImage = findViewById(R.id.ivProfileImage);
        tvName = findViewById(R.id.tvName);
        tvUsername = findViewById(R.id.tvUsername);
        tvBody = findViewById(R.id.tvBody);
        tvRetweetCount = findViewById(R.id.tvRetweetCount);
        tvFavCount = findViewById(R.id.tvFavCount);
        btnReply = findViewById(R.id.btnReply);

        tweet = Parcels.unwrap(getIntent().getParcelableExtra("tweet"));

        tvName.setText(tweet.user.name);
        tvUsername.setText("@" + tweet.user.screenName);
        tvBody.setText(tweet.body);
        tvRetweetCount.setText(tweet.retweetCount);
        tvFavCount.setText(tweet.favoriteCount);

        Glide.with(this).load(tweet.user.profileImageUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(ivProfileImage);


    }


}
