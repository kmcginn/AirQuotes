package com.kmcginn.airquotes;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MessageHelpFragment extends Fragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public static MessageHelpFragment newInstance() {
		MessageHelpFragment frag = new MessageHelpFragment();
		return frag;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//inflate the UI view for the fragment from XML file
		View messageHelpView = inflater.inflate(R.layout.fragment_message_help, container, false);
		
		return messageHelpView;
	}
}
