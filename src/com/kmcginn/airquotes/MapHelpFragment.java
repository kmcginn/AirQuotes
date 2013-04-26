package com.kmcginn.airquotes;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MapHelpFragment extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public static MapHelpFragment newInstance() {
		MapHelpFragment frag = new MapHelpFragment();
		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//inflate the UI view for the fragment from XML file
		View mapHelpView = inflater.inflate(R.layout.fragment_map_help, container, false);
		
		return mapHelpView;
	}
}
