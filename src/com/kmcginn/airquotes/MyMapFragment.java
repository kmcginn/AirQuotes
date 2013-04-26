package com.kmcginn.airquotes;

import java.util.HashMap;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.parse.ParseObject;

public class MyMapFragment extends SupportMapFragment {

	private GoogleMap mMap;
	private LatLng loc;
	private double nearbyRadius = 0.5;
	MessageHolder allMessages;
	HashMap<Marker,ParseObject> idMap;
	HashMap<Marker, JSONObject> coupMap;
	
	//TODO: read this in from settings instead of downloading?
	//private Boolean friendsOnly = false;

	
	
	
	public MyMapFragment() {
		super();
		idMap = new HashMap<Marker, ParseObject>();
		coupMap = new HashMap<Marker, JSONObject>();
	}
	
	public static MyMapFragment newInstance(LatLng newLoc, MessageHolder holder) {
		MyMapFragment frag = new MyMapFragment();
		frag.loc = newLoc;
		frag.allMessages = holder;
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
		
		mMap.setMyLocationEnabled(true);
		mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 14));
		
		//add message pins
		allMessages.refreshMap(nearbyRadius, loc, mMap, idMap, coupMap);
        
        mMap.setOnCameraChangeListener(new OnCameraChangeListener() {
        	private float currentZoom = -1;

            @Override
            public void onCameraChange(CameraPosition pos) {
                if (pos.zoom != currentZoom){
                	Toast.makeText(getActivity(), "Zoom changed!", Toast.LENGTH_SHORT).show();
                    currentZoom = pos.zoom;
                }
            }
        	
        });

        //TODO: make clicking on the info window launch comment view for appropriate message
        mMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

			@Override
			public void onInfoWindowClick(Marker mark) {
				
				if(idMap.containsKey(mark)) {
					Log.d("window", "normal pin id: " + idMap.get(mark));
					Intent intent = new Intent(getActivity(), CommentViewActivity.class);
					intent.putExtra("objId", idMap.get(mark).getObjectId());
					MyMapFragment.this.startActivity(intent);
				}
				else if(coupMap.containsKey(mark)) {
					Log.d("window", coupMap.get(mark).toString());
					
				}
				
			}        	
        	
        });
	}

}
