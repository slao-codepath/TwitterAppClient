package com.codepath.apps.twitterappclient.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

import com.codepath.apps.twitterappclient.TweetsAdapter;
import com.codepath.apps.twitterappclient.TwitterClientApp;
import com.codepath.apps.twitterappclient.models.Tweet;
import com.codepath.apps.twitterappclient.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

public class HomeTimelineFragment extends TweetsListFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	private void loadPersistedData() {
		ArrayList<Tweet> tweets = Tweet.all(Tweet.class);
		getAdapter().addAll(tweets);
		lvTweets.onRefreshComplete();
	}

	public void loadData(final int offset, long lastTweetId) {
		if (offset == 0) {
			getAdapter().clear();
			Tweet.delete(Tweet.class);
			User.delete(User.class);
		}

		ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo == null || !netInfo.isConnectedOrConnecting()) {
			loadPersistedData();
			return;
		}

		TwitterClientApp.getRestClient().getHomeTimeline(lastTweetId - 1, offset, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				TweetsAdapter adapter = getAdapter();
				adapter.addAll(Tweet.fromJson(jsonTweets));
				lvTweets.onRefreshComplete();
			}

			@Override
			public void onFailure(Throwable e, JSONObject json) {
				if (json.has("errors")) {
					try {
						JSONArray errorsArray = json.getJSONArray("errors");
						if (errorsArray.length() > 0) {
							JSONObject err = errorsArray.getJSONObject(0);
							String errMessage = err.getString("message");
							Toast.makeText(getActivity(), errMessage, Toast.LENGTH_SHORT).show();
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
}
