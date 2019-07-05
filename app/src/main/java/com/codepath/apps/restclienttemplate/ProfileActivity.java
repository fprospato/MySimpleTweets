package com.codepath.apps.restclienttemplate;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.models.User;

import org.parceler.Parcels;

public class ProfileActivity extends AppCompatActivity {

    ImageView ivProfileImage;
    ImageView ivBackgroundImage;
    TextView tvName;
    TextView tvUsername;
    TextView tvBio;
    TextView tvJoined;
    TextView tvFriendCount;
    TextView tvFollowerCount;

    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        user = Parcels.unwrap(getIntent().getParcelableExtra("user"));

        ivProfileImage = findViewById(R.id.ivProfileImage);
        tvName = findViewById(R.id.tvName);
        tvUsername = findViewById(R.id.tvUsername);
        tvBio = findViewById(R.id.tvBio);
        tvJoined = findViewById(R.id.tvJoined);
        tvFriendCount = findViewById(R.id.tvFriendCount);
        tvFollowerCount = findViewById(R.id.tvFollowerCount);
        ivBackgroundImage = findViewById(R.id.ivBackgroundImage);

        populateView();
    }

    private void populateView() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.twitter_blue)));

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle("");
        actionBar.setLogo(R.drawable.ic_icon);
        actionBar.setDisplayUseLogoEnabled(true);

        tvName.setText(user.name);
        tvUsername.setText("@" + user.screenName);
        tvBio.setText(user.description);
        tvJoined.setText("Joined " + user.created_at);
        tvFriendCount.setText(String.valueOf(user.friendCount));
        tvFollowerCount.setText(String.valueOf(user.followerCount));

        Glide.with(getApplicationContext())
                .load(user.profileImageUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(ivProfileImage);

        Glide.with(getApplicationContext())
                .load(user.backgroundImageUrl)
                .into(ivBackgroundImage);
    }
}
