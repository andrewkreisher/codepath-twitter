package com.codepath.apps.restclienttemplate;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeContainer;
    private FloatingActionButton fab;



    RestClient client;
    TweetAdapter tweetAdapter;
    ArrayList<Tweet> tweets;
    RecyclerView rvTweets;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        //actionbar color
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#1da1f2")));

        fab = (FloatingActionButton) findViewById(R.id.fab);


        //on click listener for floating action button that takes you to compose tweet
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent composeTweet = new Intent(TimelineActivity.this, ComposeActivity.class);
                startActivityForResult(composeTweet, 100);
            }
        });





        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchTimelineAsync(0);
            }

//            swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
//            android.R.color.holo_green_light,
//            android.R.color.holo_orange_light,
//            android.R.color.holo_red_light);

        });
        client = RestApplication.getRestClient(this);
        //find recyclerview
        rvTweets = (RecyclerView) findViewById(R.id.rvTweet);
        client = RestApplication.getRestClient(this);
        //init arraylist / data source
        tweets = new ArrayList<>();
        //construct adapter from datasource
        tweetAdapter = new TweetAdapter(tweets);
        //recyclerview setup (layoutmanager, use adapter)
        rvTweets.setLayoutManager(new LinearLayoutManager(this));
        rvTweets.setAdapter(tweetAdapter);


    }


    MenuItem miActionProgressItem;
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Store instance of the menu item containing progress
        miActionProgressItem = menu.findItem(R.id.miActionProgress);
        populateTimeline();
        // Return to finish
        return super.onPrepareOptionsMenu(menu);
    }

    public void showProgressBar() {
        // Show progress item
        miActionProgressItem.setVisible(true);
    }

    public void hideProgressBar() {
        // Hide progress item
        miActionProgressItem.setVisible(false);
    }



    public void fetchTimelineAsync(int page) {
            // Send the network request to fetch the updated data
            // `client` here is an instance of Android Async HTTP
            // getHomeTimeline is an example endpoint.


//            tweetAdapter.clear();
//            populateTimeline();
//            swipeContainer.setRefreshing(false);

        client.getHomeTimeline(new JsonHttpResponseHandler() {
            public void onSuccess(int i, Header[] header, JSONArray json) {
                Log.i("Andrew2", "Success");
                        // Remember to CLEAR OUT old items before appending in the new ones
                tweetAdapter.clear();
                        // ...the data has come back, add new items to your adapter...
                //weetAdapter.addAll(tweets);
                populateTimeline();
                        // Now we call setRefreshing(false) to signal refresh has finished
                swipeContainer.setRefreshing(false);
            }

            public void onFailure(Throwable e) {
                Log.d("Andrew", "Fetch timeline error: " + e.toString());
            }
        });}




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle presses on the action bar items
//        switch (item.getItemId()) {
//            case R.id.miCompose:
//                composeMessage();
//                return true;
////            case R.id.miProfile:
////                showProfileView();
////                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 100 && resultCode == RESULT_OK){

            Tweet resultTweet = data.getParcelableExtra("result_tweet");
            tweets.add(0,resultTweet);
            tweetAdapter.notifyItemInserted(0);
            rvTweets.scrollToPosition(0);
            Toast.makeText(this, "Tweeted", Toast.LENGTH_LONG);

        }
    }

    private void composeMessage() {
        //open composeActivity to make new tweet
        Intent composeTweet = new Intent(TimelineActivity.this, ComposeActivity.class);
        startActivityForResult(composeTweet, 100);
        
    }


    private void populateTimeline(){
        showProgressBar();
        client.getHomeTimeline(new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("TwitterClient", response.toString());
                //iterate through json array
                //for each entry deseralize json object

                for (int i =0 ; i < response.length(); i++){
                    //convert obj to tweet model
                    //add tweet model to data source
                    //notify adapter of added item
                    try {
                        Tweet tweet = Tweet.fromJSON(response.getJSONObject(i));
                        tweets.add(tweet);
                        tweetAdapter.notifyItemInserted(tweets.size() - 1);
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                }
                hideProgressBar();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("TwitterClient", response.toString());
                hideProgressBar();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.d("TwitterClient", responseString);
                throwable.printStackTrace();
                hideProgressBar();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
                hideProgressBar();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("TwitterClient", errorResponse.toString());
                throwable.printStackTrace();
                hideProgressBar();
            }
        });

    }
}
