package com.codepath.apps.twitterappclient.fragments;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;

import com.codepath.apps.twitterappclient.TweetsAdapter;
import com.codepath.apps.twitterappclient.TwitterClientApp;
import com.codepath.apps.twitterappclient.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class MentionsFragment extends TweetsListFragment {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public void loadData(long lastTweetId) {
		onStartLoading();
		TwitterClientApp.getRestClient().getMentions(lastTweetId - 1, new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONArray jsonTweets) {
				TweetsAdapter adapter = getAdapter();
				adapter.addAll(Tweet.fromJson(jsonTweets));
				onEndLoading();
				super.onSuccess(jsonTweets);
			}

			@Override
			public void onFailure(Throwable e, JSONObject json) {
				showErrors(json);
				onEndLoading();
				super.onFailure(e, json);
			}

			@Override
			public void onFailure(Throwable e, JSONArray jsonArray) {
				onEndLoading();
				super.onFailure(e, jsonArray);
			}

		});
	}
}
