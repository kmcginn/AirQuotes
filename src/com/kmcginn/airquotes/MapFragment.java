package com.kmcginn.airquotes;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;

public class MapFragment extends Fragment {

	private GoogleMap mMap;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setUpMapIfNeeded();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_map, container, false);
		
		return rootView;
	}
	
	private void setUpMapIfNeeded() {
		
		if(mMap == null) {
			 // Try to obtain the map from the SupportMapFragment in the layout
           mMap = ((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map))
                   .getMap();
           // Check if we were successful in obtaining the map.
           if (mMap != null) {
               setUpMap();
           }	
		
		}
	}
	
	private void setUpMap() {
		
	
	}

}
