package com.codepath.apps.twitterappclient.activities;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.apps.twitterappclient.R;
import com.codepath.apps.twitterappclient.TwitterClientApp;
import com.codepath.apps.twitterappclient.fragments.HomeTimelineFragment;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimeLineActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_time_line);
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
						HomeTimelineFragment fragment = (HomeTimelineFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentTweets);
						fragment.loadData(0, 0);
					}
				});
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
