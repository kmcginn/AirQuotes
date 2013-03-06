package com.kmcginn.airquotes;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.SaveCallback;
//import com.example.newtester.R;

public class PostActivity extends Fragment {

	ParseObject messageHolder;
	private LocationManager locationManager;
	private double lat;
	private double lng;
	
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState ) {
		super.onCreate(savedInstanceState);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.activity_post, container, false);
        //TextView dummyTextView = (TextView) rootView.findViewById(R.id.section_label);
        //dummyTextView.setText(Integer.toString(getArguments().getInt(ARG_SECTION_NUMBER)));
        return rootView;
	}
	
	@Override
	public void onStart() {
	    super.onStart();
	    
	    

	    // initialize location manager
	    locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
	    // check if GPS is enabled
	    final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

	    // GPS not enabled!
	    if (!gpsEnabled) {
	        
	    	//initialize alert dialog builder
	    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
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
					Intent intent = new Intent(getActivity(), MainActivity.class);
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
	public void onStop() {
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
		EditText editText = (EditText) getView().findViewById(R.id.messageBox);
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
					Toast.makeText(getActivity(), "Note posted", Toast.LENGTH_SHORT).show();
				}
				else {
					// did not save successfully
					Toast.makeText(getActivity(), "Unable to post note", Toast.LENGTH_LONG).show();
				}
			}
		});
		// go back to main page
		Intent intent = new Intent(getActivity(), MainActivity.class);
		startActivity(intent);
	}
	
}
