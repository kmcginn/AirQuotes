package com.kmcginn.airquotes;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.google.android.gms.maps.model.LatLng;
import com.parse.ParseObject;

public class MessagesFragment extends Fragment{
	private ArrayAdapter<String> listAdapter;
	ParseObject messageHolder;
	private LatLng loc;
	private double nearbyRadius = 0.5;
	//private ArrayList<String> objectIdList = new ArrayList<String>();
	boolean friendsOnly;
	ArrayList <String> friends= new ArrayList<String>();
	MessageHolder allMessages;
	
	// This is the Adapter being used to display the list's data
    SimpleCursorAdapter mAdapter;
	
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);  

	}
	
	public static MessagesFragment newInstance(LatLng newLoc, MessageHolder holder) {
		MessagesFragment frag = new MessagesFragment();
		frag.loc = newLoc;
		frag.allMessages = holder;
		return frag;
	}
	
	public void refresh() {
		listAdapter.clear();
		allMessages.refreshList(nearbyRadius, loc, listAdapter);		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		//inflate the UI view for the fragment from XML file
		View findView = inflater.inflate(R.layout.activity_find, container, false);
		
		 // get ListView from activity's View
        ListView listView = (ListView) findView.findViewById(R.id.list);
        // init arraylist adapter
        listAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
        
    
        // attach the adapter to the listview
        listView.setAdapter(listAdapter);
        
        listView.setOnItemClickListener(mMessageClickedHandler);

        allMessages.refreshFriends(null);
		allMessages.refreshList(nearbyRadius, loc, listAdapter);

		//return view to be displayed
		return findView;
	}	
	

	private OnItemClickListener mMessageClickedHandler = new OnItemClickListener() {
	    @SuppressWarnings("rawtypes")
		public void onItemClick(AdapterView parent, View v, int position, long id) {
	            	
	    	Intent intent = new Intent(getActivity(), CommentViewActivity.class);
	    	intent.putExtra("objId", allMessages.get(position).getObjectId());
	    	MessagesFragment.this.startActivity(intent);
	    }
	};

}
