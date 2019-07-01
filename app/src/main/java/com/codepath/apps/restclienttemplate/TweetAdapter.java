package com.codepath.apps.restclienttemplate;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.List;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder>{

    private List<Tweet> mTweets;
    Context context;
    //pass in tweet array into constructor
     public TweetAdapter(List<Tweet> tweets){
         mTweets = tweets;
     }

    //inflate layout for each row and cache references into viewholder

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View tweetView = inflater.inflate(R.layout.item_tweet, viewGroup, false );
        ViewHolder viewHolder = new ViewHolder(tweetView);
        return viewHolder;

    }

    //bind values based on position of element


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        //get data
        Tweet tweet = mTweets.get(i);
        //populate view w data
        viewHolder.tvUsername.setText(tweet.user.name);
        viewHolder.tvBody.setText(tweet.body);
        Glide.with(context)
                .load(tweet.user.profileImageUrl)
                .into(viewHolder.ivProfileImage);



    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    //create viewholder class
    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView ivProfileImage;
        public TextView tvUsername;
        public TextView tvBody;

        public ViewHolder(View itemView){
            super(itemView);
            //perform findviewbyid lookups
            ivProfileImage = (ImageView) itemView.findViewById(R.id.ivProfileImage);
            tvBody = (TextView) itemView.findViewById(R.id.tvBody);
            tvUsername = (TextView) itemView.findViewById(R.id.tvUsername);
        }

    }



}
