package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    //instances
    private ArrayList<Tweet> mTweets;
    static Context context;

    public TweetAdapter(ArrayList<Tweet> tweets) {
        this.mTweets = tweets;
    }


    /*
     * for each row, inflate the layout and cache references into ViewHolder
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);


        View tweetView = inflater.inflate(R.layout.item_tweet, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(tweetView);

        return viewHolder;
    }


    /*
     * bind the values based on the position of the element
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Tweet tweet = mTweets.get(i);

        String sourceString = "<b>" + tweet.user.name + "</b>" +  " <font color=#808080>" + "@" +  tweet.user.screenName + " • " + tweet.getRelativeTimeAgo(tweet.createdAt) + "</font>";
        viewHolder.tvUsername.setText(Html.fromHtml(sourceString, Html.FROM_HTML_MODE_LEGACY));
        viewHolder.username = tweet.user.screenName;

        viewHolder.tvBody.setText(tweet.body);
        viewHolder.tvRetweet.setText(tweet.retweetCount);
        viewHolder.tvFavorite.setText(tweet.favoriteCount);

        Glide.with(context).load(tweet.user.profileImageUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(viewHolder.ivProfileImage);
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView ivProfileImage;
        public TextView tvUsername;
        public TextView tvBody;
        public TextView tvRetweet;
        public TextView tvFavorite;
        public ImageView ivComment;
        String username;

        public  ViewHolder(View itemView) {
            super(itemView);

            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvUsername = itemView.findViewById(R.id.tvUserName);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvRetweet = itemView.findViewById(R.id.tvRetweet);
            tvFavorite = itemView.findViewById(R.id.tvFavorite);
            ivComment = itemView.findViewById(R.id.ivComment);

            ivComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (username.length() > 0) {
                        Intent i = new Intent(context, ComposeActivity.class);
                        i.putExtra("type", "reply");
                        i.putExtra("username", "@" + username);
                        context.startActivity(i);
                    }
                }
            });

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            // get item position
            int position = getAdapterPosition();

            // check for valid position (actually is in the view)
            if (position != RecyclerView.NO_POSITION) {

                // get movie from the position (won't work if the class is static)
                Tweet tweet = mTweets.get(position);

                // create intent for the new activity
                Intent intent = new Intent(context, DetailActivity.class);

                // serialize the movie using parceler, use its short name as a key
                intent.putExtra("tweet", Parcels.wrap(tweet));

                // show the activity
                context.startActivity(intent);
            }
        }

    }


    public void clear() {
        mTweets.clear();
        notifyDataSetChanged();
    }


    public void addAll(List<Tweet> list) {
        mTweets.addAll(list);
        notifyDataSetChanged();
    }
}
