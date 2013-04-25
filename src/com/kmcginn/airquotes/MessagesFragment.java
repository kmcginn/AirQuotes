package com.kmcginn.airquotes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

public class MessagesFragment extends Fragment{
	private ArrayAdapter<String> listAdapter;
	ParseObject messageHolder;
	private LatLng loc;
	private double nearbyRadius = 0.5;
	private ArrayList<String> objectIdList = new ArrayList<String>();
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
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.e("loc","at beginning of onCreateView");
		//inflate the UI view for the fragment from XML file
		View findView = inflater.inflate(R.layout.activity_find, container, false);
		
		 // get ListView from activity's View
        ListView listView = (ListView) findView.findViewById(R.id.list);
        // init arraylist adapter
        listAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
        // attach the adapter to the listview
        listView.setAdapter(listAdapter);
        
        listView.setOnItemClickListener(mMessageClickedHandler);
		Log.e("loc","before friends query");

		allMessages.refreshList(friendsOnly, nearbyRadius, loc, listAdapter);
		/*
        // get friends list
        
        // get current user
		//TODO: MAKE THIS NOT HAPPEN IN THE GUI THREAD!!!
		// if friends only, does second query in its callback
		// otherwise just regular background query?
		ParseUser currUser= ParseUser.getCurrentUser();
	    friendsOnly= (Boolean) currUser.get("viewFriends");
	    try {
	        if (friendsOnly){
		        // get relation and query
		        ParseRelation friendsRel= currUser.getRelation("friends");
		        ParseQuery friendsQuery= friendsRel.getQuery();
		        
		        friendsQuery.findInBackground(new FindCallback() {
					public void done(List<ParseObject> users, ParseException e) {
						if (e == null){
							for(ParseObject o: users){
								try {
								friends.add( o.getString("username"));
								}
								catch (Exception e1){
									Log.e("collection","Couldn't add to collection: "+o.getString("username")+e1);
								}
							}
						}
						else {
	        				Log.e("friends", "Unable to upload friends: " + e);
	
						}
						
					}
		        	
		        });
	        }
        }
        catch (Exception e1){
        	Log.e("user","Exception thrown while getting user information"+e1);
        }
		Log.e("loc","after friends query");

        
        // setup parse query (for messages)
        ParseQuery query = new ParseQuery("Message");
        
        try {
	        //set distance away
	        query.whereWithinMiles("location", new ParseGeoPoint(loc.latitude,loc.longitude), nearbyRadius);
        } catch (Exception e1){
			Log.e("loc", "Unable to get location: " + e1);
        }
		Log.e("loc","before find query");
		if (friendsOnly){
        	query.whereContainedIn("user", friends);
        }
    	// find them in the background
		try {
        query.findInBackground(new FindCallback() {
        	public void done(List<ParseObject> objects, ParseException e) {
        		Log.e("loc","before if");

        		if(e == null) {
        			//success
        			Log.e("loc","In Callback");

        			// get user information
        			//clear old list of objectIds
        			objectIdList.clear();
        			
        			Log.e("loc","After loading user data");
        			
        			// add all objects from query to list adapter
        			for(ParseObject o: objects){
        				// get the message
	        			String text = o.getString("text").toString();
	        			// get the author
	        			String author = o.getString("user").toString();
	        			// get the location
	        			ParseGeoPoint pt = o.getParseGeoPoint("location");
	        			// add a string combining the message and location
	        			listAdapter.add(text + "\n" + author);
	        				
	        			try{
	        			// add message's parse id to the array
	        				String objid = o.getObjectId();
	        				objectIdList.add(objid);
	        			} catch(Exception e1) {
	        				Log.e("list", "Unable to add objectId to arraylist: " + e1);
	        				
	        			}
	        			
	        			//indicate that the list adapter has changed its data, so the listview will update
	        			listAdapter.notifyDataSetChanged();
        			}
        			Log.e("loc","After for loop");

        			
        			
        		}
        		else {
        			//failure
        			Log.e("find","Unable to find posts: "+e);

        			Toast.makeText(getActivity(), "Unable to retrieve notes", Toast.LENGTH_LONG).show();
        		}
        	}
        });
		}
		catch (Exception e1){
			Log.e("find","Unable to enter findCallback: "+e1);
			
		}
		*/
		//return view to be displayed
		return findView;
	}	
	

	private OnItemClickListener mMessageClickedHandler = new OnItemClickListener() {
	    public void onItemClick(AdapterView parent, View v, int position, long id) {
	        //TODO: make clicking on the list launch CommentViewActivity
	    	//need argument to be objectid for the message
	    	
	    	Intent intent = new Intent(getActivity(), CommentViewActivity.class);
	    	intent.putExtra("objId", allMessages.get(position).getObjectId());
	    	MessagesFragment.this.startActivity(intent);
	    }
	};
	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_find, menu);
		return true;
	}
*/
	/*
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
	*/

}
