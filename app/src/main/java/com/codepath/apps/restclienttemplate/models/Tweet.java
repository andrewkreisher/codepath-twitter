package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

@Parcel
public class Tweet {

    //attributes
    public String body;
    public long uid;
    public String createdAt;
    public User user;
    public String mediaUrl;

    //deserialize data (Json)
    public static Tweet fromJSON (JSONObject jsonObject) throws JSONException {
        Tweet tweet = new Tweet();

        //extract info from json
        tweet.body = jsonObject.getString("text");
        tweet.uid = jsonObject.getLong("id");
        tweet.createdAt = jsonObject.getString("created_at");
        try {
            tweet.mediaUrl = jsonObject.getJSONObject("entities").getJSONArray("media").getJSONObject(0).getString("media_url");
        } catch (JSONException e) {
            tweet.mediaUrl = null;
        }
        tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        return tweet;


    }

    public Tweet(){}


}
