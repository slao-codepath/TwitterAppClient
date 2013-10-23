package com.codepath.apps.twitterappclient.models;

import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "users")
public class User extends Model {
	
	@Column(name = "profileImageUrl")
	private String profileImageUrl;

	@Column(name = "name")
	private String name;

	@Column(name = "screenName")
	private String screenName;

	public User() {
		super();
	}

	public User(JSONObject object) {
		super();
		
		try {
			this.name = object.getString("name");
			this.screenName = object.getString("screen_name");
			this.profileImageUrl = object.getString("profile_image_url");
		} catch (JSONException e) {
			e.printStackTrace();
		}		
	}

	public String getProfileImageUrl() {
		return profileImageUrl;	
	}

	public String getName() {
		return name;
	}

	public String getScreenName() {
		return screenName;
	}
}
