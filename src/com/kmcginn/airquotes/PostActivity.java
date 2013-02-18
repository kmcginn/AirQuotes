package com.kmcginn.airquotes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.SaveCallback;

public class PostActivity extends Activity {

	ParseObject messageHolder;
	final Context context = this;
	private LocationManager locationManager;
	private double lat;
	private double lng;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post);
		// Make sure we're running on Honeycomb or higher to use ActionBar APIs
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
        	// Show the Up button in the action bar.
        	getActionBar().setDisplayHomeAsUpEnabled(true);
        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_post, menu);
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

	@Override
	protected void onStart() {
	    super.onStart();

	    // initialize location manager
	    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	    // check if GPS is enabled
	    final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

	    // GPS not enabled!
	    if (!gpsEnabled) {
	        
	    	//initialize alert dialog builder
	    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
	    	alertDialogBuilder.setTitle("GPS Not Enabled");
	    	alertDialogBuilder.setMessage("GPS must be enabled to post a note in AirQuotes.");
	    	alertDialogBuilder.setCancelable(true);
	    	
	    	//set behavior of enable button
	    	alertDialogBuilder.setPositiveButton("Enable", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					enableLocationSettings();
					dialog.dismiss();
				}
			});
	    	
	    	// set behavior of cancel button
	    	alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
					// go back to main activity
					Intent intent = new Intent(context, MainActivity.class);
					startActivity(intent);
					
				}
			});
	    	
	    	//create the alert dialog
	    	AlertDialog alertDialog = alertDialogBuilder.create();
	    	
	    	//show it
	    	alertDialog.show();
	    }
	    
	    // get updates from the listener
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,10000,10,listener);

	}

	@Override
	protected void onStop() {
		super.onStop();
		// end GPS searching, etc.
		locationManager.removeUpdates(listener);		
	}
	
	// method for allowing the user to turn the GPS on
	private void enableLocationSettings() {
		// go to the location settings menu
	    Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
	    startActivity(settingsIntent);
	}
	
	// set up a location listener
	private final LocationListener listener = new LocationListener() {

	    @Override
	    public void onLocationChanged(Location location) {
	    	// update stored values of latitude and longitude
	        lat = location.getLatitude();
	        lng = location.getLongitude();
	    }
	    
	    public void onProviderDisabled(String provider) {
	        // required for interface, not used
	    }
	         
	    public void onProviderEnabled(String provider) {
	    	// required for interface, not used
	    }
	         
	    public void onStatusChanged(String provider, int status,Bundle extras) {
	        // required for interface, not used
	    }
	    
	};	
	
	/* Run when post button is clicked */
	public void postMessage(View view) {
		
		// get text from user's EditText box
		EditText editText = (EditText) findViewById(R.id.messageBox);
		String message = editText.getText().toString();		
		// gather message info for Parse
		messageHolder = new ParseObject("Message");
		// gather location info for Parse
		ParseGeoPoint point = new ParseGeoPoint(lat, lng);
		messageHolder.put("text", message);
		messageHolder.put("location", point);
		// save it!
		messageHolder.saveInBackground(new SaveCallback() {
			public void done(ParseException e) {
				if(e == null) {
					// saved successfully
					Toast.makeText(context, "Note posted", Toast.LENGTH_SHORT).show();
				}
				else {
					// did not save successfully
					Toast.makeText(context, "Unable to post note", Toast.LENGTH_LONG).show();
				}
			}
		});
		// go back to main page
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}
	
}
