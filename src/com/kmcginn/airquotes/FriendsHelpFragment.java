package com.kmcginn.airquotes;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FriendsHelpFragment extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public static FriendsHelpFragment newInstance() {
		FriendsHelpFragment frag = new FriendsHelpFragment();
		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//inflate the UI view for the fragment from XML file
		View friendsHelpView = inflater.inflate(R.layout.fragment_friends_help, container, false);
		
		return friendsHelpView;
	}
}
