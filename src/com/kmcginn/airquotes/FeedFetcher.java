package com.kmcginn.airquotes;

import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

public class FeedFetcher {

	static JSONArray jObj = new JSONArray();
	//String url = null;
	
	//constructor
	public FeedFetcher() {		
	}
	
	public JSONArray makeHTTPRequest(String url) {
		
		String result = null;
		
		Log.e("feedf", "URL grabbing from: " + url);
		
		PageDownloadTask pDown = new PageDownloadTask();
		
		try {
			result = pDown.execute(url).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			jObj = new JSONArray(result);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception err) {
			Log.e("feedf", err.toString());
		}
		
		return jObj;
	}
	
}