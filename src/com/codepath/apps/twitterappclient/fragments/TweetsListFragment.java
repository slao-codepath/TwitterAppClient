package com.codepath.apps.twitterappclient.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.apps.twitterappclient.EndlessScrollListener;
import com.codepath.apps.twitterappclient.R;
import com.codepath.apps.twitterappclient.TweetsAdapter;
import com.codepath.apps.twitterappclient.models.Tweet;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public abstract class TweetsListFragment extends Fragment {
	protected PullToRefreshListView lvTweets;
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

	private void setupViews() {
		ArrayList<Tweet> tweets = new ArrayList<Tweet>();
		adapter = new TweetsAdapter(getActivity(), tweets);
		lvTweets = (PullToRefreshListView) getActivity().findViewById(R.id.lvTweets);
		lvTweets.setAdapter(adapter);
		lvTweets.setOnScrollListener(new EndlessScrollListener() {
			@Override
			public void onLoadMore(int page, int totalItemsCount) {
				loadData(totalItemsCount, getLastTweetId());
			}
		});
		lvTweets.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				loadData(0, getLastTweetId());
			}
		});
	}

	public TweetsAdapter getAdapter() {
		return adapter;
	}

	public abstract void loadData(final int offset, long lastTweetId);
}
