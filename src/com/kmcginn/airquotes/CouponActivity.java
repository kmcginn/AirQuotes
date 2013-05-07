package com.kmcginn.airquotes;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class CouponActivity extends Activity {

	ArrayAdapter<String> listAdapter;
	private JSONObject coupObj;
	ArrayList<String> coupDetails;
	Context context = this;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coupon);
		// Show the Up button in the action bar.
		setupActionBar();
		
		coupDetails = new ArrayList<String>();
		
		Intent intent = getIntent();
		
		try{
			coupObj = new JSONObject(intent.getStringExtra("coupon"));
		} catch(Exception e1) {
			Log.e("newCoupAct", "Unable to get JSONObj from previous activity: " + e1);			
		}
		
		TextView name = (TextView) findViewById(R.id.business_name);
		TextView addr = (TextView) findViewById(R.id.business_address);
		TextView phone = (TextView) findViewById(R.id.business_phone);
		TextView web = (TextView) findViewById(R.id.business_link);
		ListView listView = (ListView) findViewById(R.id.coupon_list);
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(mCouponClickedHandler);
		try {
			name.setText(coupObj.getString("name").toString());
			addr.setText(coupObj.getString("address").toString());
			phone.setText(coupObj.getString("phone").toString());
			web.setText(coupObj.getString("uri").toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JSONArray allCoups = new JSONArray();
		try {
			allCoups = coupObj.getJSONArray("coupons");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONObject oneCoup = new JSONObject();
		for(int i = 0; i < allCoups.length(); i++) {
			try{
				oneCoup = allCoups.getJSONObject(i);
				listAdapter.add(oneCoup.getString("name").toString());
				coupDetails.add(oneCoup.getJSONObject("details").getString("desc").toString());
			} catch (JSONException e) {
				
				e.printStackTrace();
			}
		}
		
		listAdapter.notifyDataSetChanged();
		


	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.coupon, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private OnItemClickListener mCouponClickedHandler = new OnItemClickListener() {
	    @SuppressWarnings("rawtypes")
		public void onItemClick(AdapterView parent, View v, int position, long id) {
	        AlertDialog.Builder builder = new AlertDialog.Builder(context);
	        
	        builder.setMessage(coupDetails.get(position));
	        builder.setCancelable(true);
	        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {

	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	            	//do nothing
	            	//could eventually mark coupon as claimed
	            }
	        });
	        // Create the AlertDialog object and return it
	        AlertDialog alertDialog = builder.create();
	        alertDialog.show();
	    	
	    }
	};
	
}
