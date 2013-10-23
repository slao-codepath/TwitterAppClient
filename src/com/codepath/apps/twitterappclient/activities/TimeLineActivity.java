package com.codepath.apps.twitterappclient.activities;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.apps.twitterappclient.EndlessScrollListener;
import com.codepath.apps.twitterappclient.R;
import com.codepath.apps.twitterappclient.TweetsAdapter;
import com.codepath.apps.twitterappclient.TwitterClientApp;
import com.codepath.apps.twitterappclient.models.Tweet;
import com.codepath.apps.twitterappclient.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class TimeLineActivity extends Activity {
	private PullToRefreshListView lvTweets;
	private ArrayList<Tweet> tweets;
	private TweetsAdapter adapter;
	private long lastTweetId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time_line);
		setupViews();
		refreshData();
	}

	private void setupViews() {
		lvTweets = (PullToRefreshListView) findViewById(R.id.lvTweets);
		lvTweets.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				loadData(totalItemsCount);
			}
		});
		lvTweets.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				refreshData();
			}
		});		
	}

	private void refreshData() {
		lastTweetId = 0;
		if (adapter != null) {
			adapter.clear();
		}
		loadData(0);
	}

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	private void loadPersistedData() {
		ArrayList<Tweet> tweets = Tweet.all(Tweet.class);
		adapter = new TweetsAdapter(getBaseContext(), tweets);
		lvTweets.setAdapter(adapter);
	}

	private void loadData(final int offset) {
		if (!isOnline()) {
			loadPersistedData();
			lvTweets.onRefreshComplete();
			return;
		}
		TwitterClientApp.getRestClient().getHomeTimeline(lastTweetId - 1, offset, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				// clear persistent data, new data is being loaded
				Tweet.delete(Tweet.class);
				User.delete(User.class);

				tweets = Tweet.fromJson(jsonTweets);
				if (adapter == null) {
					adapter = new TweetsAdapter(getBaseContext(), tweets);
					lvTweets.setAdapter(adapter);
				} else {
					adapter.addAll(tweets);
				}
				Tweet lastTweet = adapter.getItem(adapter.getCount() - 1);
				lastTweetId = lastTweet.getUniqueId();
				lvTweets.onRefreshComplete();
				super.onSuccess(jsonTweets);
			}

			@Override
			public void onFailure(Throwable e, JSONObject json) {
				if (json.has("errors")) {
					try {
						JSONArray errorsArray = json.getJSONArray("errors");
						if (errorsArray.length() > 0) {
							JSONObject err = errorsArray.getJSONObject(0);
							String errMessage = err.getString("message");
							Toast.makeText(getApplicationContext(), errMessage, Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e1) {
						e1.printStackTrace();
					}
				}

				loadPersistedData();
				super.onFailure(e, json);
			}

			@Override
			public void onFailure(Throwable e, JSONArray jsonArray) {
				loadPersistedData();				
				super.onFailure(e, jsonArray);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.time_line, menu);
		return super.onCreateOptionsMenu(menu);
	}

	public void onCompose(MenuItem mi) {
		Intent i = new Intent(getApplicationContext(), ComposeActivity.class);
		startActivityForResult(i, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null && requestCode == 0) {
			String message = data.getExtras().getString("message");
			if (message != null) {
				TwitterClientApp.getRestClient().postTweet(message, new JsonHttpResponseHandler() {
					@Override
					public void onSuccess(JSONObject response) {
						refreshData();
					}
				});
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
