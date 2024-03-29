package com.codepath.apps.restclienttemplate;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.github.scribejava.apis.TwitterApi;
import com.github.scribejava.core.builder.api.BaseApi;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/scribejava/scribejava/tree/master/scribejava-apis/src/main/java/com/github/scribejava/apis
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final BaseApi REST_API_INSTANCE = TwitterApi.instance();
	public static final String REST_URL = "https://api.twitter.com/1.1";
	public static final String REST_CONSUMER_KEY = "4KxocRp2Wh8RZ9cy1KJEjxGVy";
	public static final String REST_CONSUMER_SECRET = "EeyJ4vEZN3al7c0C13bMwAY3pGc2RASrampYtvJvnX1kLDHKJf";

	// Landing page to indicate the OAuth flow worked in case Chrome for Android 25+ blocks navigation back to the app.
	public static final String FALLBACK_URL = "https://codepath.github.io/android-rest-client-template/success.html";

	// See https://developer.chrome.com/multidevice/android/intents
	public static final String REST_CALLBACK_URL_TEMPLATE = "intent://%s#Intent;action=android.intent.action.VIEW;scheme=%s;package=%s;S.browser_fallback_url=%s;end";

	public TwitterClient(Context context) {
		super(context, REST_API_INSTANCE,
				REST_URL,
				REST_CONSUMER_KEY,
				REST_CONSUMER_SECRET,
				String.format(REST_CALLBACK_URL_TEMPLATE, context.getString(R.string.intent_host),
						context.getString(R.string.intent_scheme), context.getPackageName(), FALLBACK_URL));
	}


	public void getCurrentUser(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("account/verify_credentials.json");

		RequestParams params = new RequestParams();

		client.get(apiUrl, params, handler);
	}


	public void getHomeTimeline(AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");

		RequestParams params = new RequestParams();
		params.put("count", 25); //get 25 tweets
		params.put("since_id", 1); //mostly used for the pull and refresh feature

		client.get(apiUrl, params, handler);
	}

	public void getMoreHomeTimeline(long max_id, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/home_timeline.json");

		RequestParams params = new RequestParams();
		params.put("count", 25);
		params.put("since_id", 1);
		params.put("max_id", max_id);

		client.get(apiUrl, params, handler);
	}

	public void sendTweet(String message, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("statuses/update.json");

		RequestParams params = new RequestParams();
		params.put("status", message);

		client.post(apiUrl, params, handler);
	}

	public void sendFavorite(String id, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("favorites/create.json");

		RequestParams params = new RequestParams();
		params.put("id", id);

		client.post(apiUrl, params, handler);
	}

	public void deleteFavorite(String id, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("favorites/destroy.json");

		RequestParams params = new RequestParams();
		params.put("id", id);

		client.post(apiUrl, params, handler);
	}

	public void sendFollow(long user_id, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("friendships/create.json");

		RequestParams params = new RequestParams();
		params.put("user_id", user_id);
		params.put("follow", true);

		client.post(apiUrl, params, handler);
	}

	public void deleteFollow(long user_id, AsyncHttpResponseHandler handler) {
		String apiUrl = getApiUrl("friendships/destroy.json");

		RequestParams params = new RequestParams();
		params.put("user_id", user_id);

		client.post(apiUrl, params, handler);
	}

	public void getFollows(long user_id, Boolean isFriendsList, AsyncHttpResponseHandler handler) {
		String apiUrl;
		if (isFriendsList) {
			apiUrl = getApiUrl("friends/list.json");
		} else {
			apiUrl = getApiUrl("followers/list.json");
		}

		RequestParams params = new RequestParams();
		params.put("user_id", user_id);
		params.put("count", 25);

		client.get(apiUrl, params, handler);
	}
}
