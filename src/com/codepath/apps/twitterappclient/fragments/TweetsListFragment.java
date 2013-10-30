package com.codepath.apps.twitterappclient.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.codepath.apps.twitterappclient.EndlessScrollListener;
import com.codepath.apps.twitterappclient.R;
import com.codepath.apps.twitterappclient.TweetsAdapter;
import com.codepath.apps.twitterappclient.activities.ProfileActivity;
import com.codepath.apps.twitterappclient.models.Tweet;
import com.codepath.apps.twitterappclient.models.User;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public abstract class TweetsListFragment extends Fragment {
	protected PullToRefreshListView lvTweets;
	private ProgressBar pbLoading;
	private TweetsAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragments_tweets_list, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setupViews();
	}

	private long getLastTweetId() {
		long ret = 0;
		TweetsAdapter adapter = getAdapter();
		if (adapter.getCount() > 0) {
			ret = (adapter.getItem(adapter.getCount() - 1)).getUniqueId();
		}
		return ret;
	}

	protected void showErrors(JSONObject json) {
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
	}

	private void setupViews() {
		pbLoading = (ProgressBar) getActivity().findViewById(R.id.pbLoading);
		ArrayList<Tweet> tweets = new ArrayList<Tweet>();
		adapter = new TweetsAdapter(getActivity(), tweets);
		lvTweets = (PullToRefreshListView) getActivity().findViewById(R.id.lvTweets);
		lvTweets.setAdapter(adapter);
		lvTweets.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				loadData(getLastTweetId());
			}
		});
		lvTweets.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				adapter.clear();
				loadData(getLastTweetId());
			}
		});
		if (getActivity().getClass() != ProfileActivity.class) {
			lvTweets.setItemsCanFocus(true);
			lvTweets.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Tweet tweet = adapter.getItem(position);
					User user = tweet.getUser();
					Intent i = new Intent(getActivity(), ProfileActivity.class);
					i.putExtra("idStr", user.getIdStr());
					i.putExtra("screenName", user.getScreenName());
					startActivity(i);
				}
			});
		}
	}

	public TweetsAdapter getAdapter() {
		return adapter;
	}

	protected void onStartLoading() {
		pbLoading.setVisibility(ProgressBar.VISIBLE);
	}

	protected void onEndLoading() {
		lvTweets.onRefreshComplete();
		pbLoading.setVisibility(ProgressBar.INVISIBLE);
	}

	public abstract void loadData(long lastTweetId);
}
