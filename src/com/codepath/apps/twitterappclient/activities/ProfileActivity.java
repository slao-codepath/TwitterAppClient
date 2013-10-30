package com.codepath.apps.twitterappclient.activities;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.twitterappclient.R;
import com.codepath.apps.twitterappclient.TwitterClientApp;
import com.codepath.apps.twitterappclient.fragments.UserTimelineFragment;
import com.codepath.apps.twitterappclient.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ProfileActivity extends FragmentActivity {
	private String idStr;
	private String screenName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		Intent i = getIntent();
		if (i != null) {
			idStr = i.getStringExtra("idStr");
			screenName = i.getStringExtra("screenName");
		}
		loadProfileInfo();
	}

	private void loadProfileInfo() {
		JsonHttpResponseHandler handler = new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject json) {
				User u = new User(json);
				getActionBar().setTitle("@" + u.getScreenName());
				populateProfileHeader(u);
				UserTimelineFragment fragment = (UserTimelineFragment) getSupportFragmentManager().findFragmentById(
						R.id.fragmentUserTimeline);
				fragment.onReceivedUserInfo(u.getIdStr());
			}
		};
		if (idStr != null) {
			TwitterClientApp.getRestClient().getUserInfo(idStr, screenName, handler);
		} else {
			TwitterClientApp.getRestClient().getMyInfo(handler);
		}
	}

	private void populateProfileHeader(User u) {
		TextView tvName = (TextView) findViewById(R.id.tvName);
		TextView tvTagline = (TextView) findViewById(R.id.tvTagline);
		TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
		TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
		ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);

		tvName.setText(u.getName());
		tvTagline.setText(u.getTagline());
		tvFollowers.setText(u.getFollowersCount() + " Followers");
		tvFollowing.setText(u.getFriendsCount() + " Following");
		ImageLoader.getInstance().displayImage(u.getProfileImageUrl(), ivProfileImage);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile, menu);
		return true;
	}

}
