package com.kmcginn.airquotes;

import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class MyMapFragment extends SupportMapFragment {

	private GoogleMap mMap;
	private LatLng loc;
	private double nearbyRadius = 0.5;
	
	public MyMapFragment() {
		super();
	}
	
	public static MyMapFragment newInstance(LatLng newLoc) {
		MyMapFragment frag = new MyMapFragment();
		frag.loc = newLoc;
		return frag;
		
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = super.onCreateView(inflater, container, savedInstanceState);
		setUpMapIfNeeded();
		return rootView;
	}
	
	
	private void setUpMapIfNeeded() {
		
		if(mMap == null) {
			//try to get the map
			mMap = getMap();
			// Check if we were successful in obtaining the map.
			if (mMap != null) {
				setUpMap();
			}		
			else {
				//unable to find map, do something
			}
		}
	}
	
	private void setUpMap() {
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 14));
		//TODO: add pins
		// setup parse query (for messages)
        ParseQuery query = new ParseQuery("Message");
        //set distance away
        query.whereWithinMiles("location", new ParseGeoPoint(loc.latitude,loc.longitude), nearbyRadius);
        // find them in the background
        query.findInBackground(new FindCallback() {
        	public void done(List<ParseObject> objects, ParseException e) {
        		if(e == null) {
        			//success
        			
        			// add all objects from query to list adapter
        			for(Object o: objects){
        				// get the message text
        				String text = ((ParseObject) o).getString("text").toString();
        				//get the message's user
        				//String user = ((ParseObject) o).getString("user").toString();
        				String user = "test";
        				// get the location
        				ParseGeoPoint pt = ((ParseObject) o).getParseGeoPoint("location");
        				mMap.addMarker(new MarkerOptions()
        					.position(new LatLng(pt.getLatitude(), pt.getLongitude()))
        					.title(text)
        					.snippet(user));
        			}
      
        		}
        		else {
        			//failure
        			Toast.makeText(getActivity(), "Unable to retrieve notes", Toast.LENGTH_LONG).show();
        		}
        	}
        });
	}

}
