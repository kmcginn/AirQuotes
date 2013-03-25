package com.kmcginn.airquotes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MyMapFragment extends SupportMapFragment {

	private GoogleMap mMap;
	private LatLng loc;
	
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
	}

}
