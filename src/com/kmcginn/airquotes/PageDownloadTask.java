package com.kmcginn.airquotes;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.util.Log;

public class PageDownloadTask extends AsyncTask<String, Integer, String> {

	String page;
	
	public PageDownloadTask() {
		
	}
	
	@Override
	protected String doInBackground(String... url) {
		
		Log.e("pdtask", "URL in task: " + url[0]);
		
		//TODO: make use of the fact that url is an ARRAY of strings?
		
		try {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url[0]);
		
		HttpResponse httpResponse = httpClient.execute(httpGet);
		HttpEntity httpEntity = httpResponse.getEntity();
		
		page = EntityUtils.toString(httpEntity);
		
		} catch(Exception err) {
			Log.e("page_dl", err.toString());			
		}
		
		return page;
		
	}	
	
}