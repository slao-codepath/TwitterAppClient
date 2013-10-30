package com.codepath.apps.twitterappclient.fragments;

import org.json.JSONArray;
import org.json.JSONObject;

import com.codepath.apps.twitterappclient.TweetsAdapter;
import com.codepath.apps.twitterappclient.TwitterClientApp;
import com.codepath.apps.twitterappclient.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

public class UserTimelineFragment extends TweetsListFragment {
	private String idStr;

	public void loadData(long lastTweetId) {
		if (idStr != null) {
			onStartLoading();
			TwitterClientApp.getRestClient().getUserTimeline(idStr, lastTweetId - 1, new JsonHttpResponseHandler() {
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

	public void onReceivedUserInfo(String idStr) {
		this.idStr = idStr;
		loadData(0);
	}
}
