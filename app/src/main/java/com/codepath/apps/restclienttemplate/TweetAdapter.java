package com.codepath.apps.restclienttemplate;

import android.content.Context;
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

import java.util.List;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    //instances
    private List<Tweet> mTweets;
    Context context;

    public TweetAdapter(List<Tweet> tweets) {
        mTweets = tweets;
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView ivProfileImage;
        public TextView tvUsername;
        public TextView tvBody;
        public TextView tvRetweet;
        public TextView tvFavorite;

        public  ViewHolder(View itemView) {
            super(itemView);

            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvUsername = itemView.findViewById(R.id.tvUserName);
            tvBody = itemView.findViewById(R.id.tvBody);
            tvRetweet = itemView.findViewById(R.id.tvRetweet);
            tvFavorite = itemView.findViewById(R.id.tvFavorite);
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
