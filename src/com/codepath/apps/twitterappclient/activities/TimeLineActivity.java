package com.codepath.apps.twitterappclient.activities;

import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.codepath.apps.twitterappclient.R;
import com.codepath.apps.twitterappclient.TwitterClientApp;
import com.codepath.apps.twitterappclient.fragments.HomeTimelineFragment;
import com.codepath.apps.twitterappclient.fragments.MentionsFragment;
import com.codepath.apps.twitterappclient.fragments.TweetsListFragment;
import com.loopj.android.http.JsonHttpResponseHandler;

public class TimeLineActivity extends FragmentActivity implements TabListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS); 
		setContentView(R.layout.activity_time_line);
		setupNatigationTabs();
	}

	private void setupNatigationTabs() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);

		Tab tabHome = actionBar.newTab().setText("Home")
				.setTag("HomeTimelineFragment").setIcon(R.drawable.ic_action_home)
				.setTabListener(this);

		Tab tabMentions = actionBar.newTab().setText("Mentions")
				.setTag("MentionsTimelineFragment").setIcon(R.drawable.ic_action_mentions)
				.setTabListener(this);

		actionBar.addTab(tabHome);
		actionBar.addTab(tabMentions);
		actionBar.selectTab(tabHome);
	}

	public void onProfileView(MenuItem mi) {
		Intent i = new Intent(this, ProfileActivity.class);
		startActivity(i);
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
						TweetsListFragment fragment = (TweetsListFragment) getSupportFragmentManager().findFragmentById(R.id.frame_container);
						fragment.getAdapter().clear();
						fragment.loadData(0);
					}
				});
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction fc) {
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction fc) {
		FragmentManager manager = getSupportFragmentManager();
		android.support.v4.app.FragmentTransaction fts = manager.beginTransaction();
		if (tab.getTag() == "HomeTimelineFragment") {
			fts.replace(R.id.frame_container, new HomeTimelineFragment());
		} else {
			fts.replace(R.id.frame_container, new MentionsFragment());
		}
		fts.commit();
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction fc) {
	}

}
