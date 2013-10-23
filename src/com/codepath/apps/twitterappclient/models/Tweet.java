package com.codepath.apps.twitterappclient.models;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;


@Table(name = "tweets")
public class Tweet extends Model {
	private static final String DATE_FORMAT = "EEE MMM dd HH:mm:ss Z yyyy";

	@Column(name = "tweetId")
	private long id;
	
	@Column(name = "user")
	private User user;

	@Column(name = "text")
	private String text;

	@Column(name = "timestamp")
	private String timestamp;

	public Tweet() {
		super();
	}

	public Tweet(JSONObject object) {
		super();
		
		try {
			this.id = Long.parseLong(object.getString("id"));
			this.user = new User(object.getJSONObject("user"));
			this.user.save();
			this.text = object.getString("text");
			this.timestamp = object.getString("created_at");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public User getUser() {
		return user;
	}

	public String getText() {
		return text;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public long getUniqueId() {
		return id;
	}

	public static Date parseDate(String createdAt) {
		Date out;
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.US);
		sdf.setLenient(true);
		try {
			out = sdf.parse(createdAt);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
			out = new Date();
		}
		return out;
	}

	public static ArrayList<Tweet> fromJson(JSONArray jsonArray) {
		ArrayList<Tweet> tweets = new ArrayList<Tweet>(jsonArray.length());

		// Process each result in json array, decode and convert to business object
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject tweetJson = null;
			try {
				tweetJson = jsonArray.getJSONObject(i);
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
			
			Tweet tweet = new Tweet(tweetJson);
			tweet.save();
			tweets.add(tweet);
		}

		return tweets;
	}

}
