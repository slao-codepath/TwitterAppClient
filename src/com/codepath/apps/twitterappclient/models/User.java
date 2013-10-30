package com.codepath.apps.twitterappclient.models;

import org.json.JSONException;
import org.json.JSONObject;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "users")
public class User extends Model {
	
	@Column(name = "idStr")
	private String idStr;

	@Column(name = "profileImageUrl")
	private String profileImageUrl;

	@Column(name = "name")
	private String name;

	@Column(name = "screenName")
	private String screenName;

	@Column(name = "tagLine")
	private String tagLine;

	@Column(name = "followersCount")
	private int followersCount;

	@Column(name = "friendsCount")
	private int friendsCount;

	public User() {
		super();
	}

	public User(JSONObject object) {
		super();
		
		try {
			this.idStr = object.getString("id_str");
			this.name = object.getString("name");
			this.screenName = object.getString("screen_name");
			this.profileImageUrl = object.getString("profile_image_url");
			this.tagLine = object.getString("description");
			this.followersCount = object.getInt("followers_count");
			this.friendsCount = object.getInt("friends_count");
		} catch (JSONException e) {
			e.printStackTrace();
		}		
	}

	public String getIdStr() {
		return idStr;
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
	
	public String getTagline() {
		return tagLine;
	}

	public int getFollowersCount() {
		return followersCount;
	}

	public int getFriendsCount() {
		return friendsCount;
	}
}
