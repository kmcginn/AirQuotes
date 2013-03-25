package com.kmcginn.airquotes;

import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
import com.parse.SaveCallback;

public class FindActivity extends Fragment{

	private ArrayAdapter<String> listAdapter;
	ParseObject messageHolder;
	private ParseUser user;
	private LatLng loc;
	private double nearbyRadius = 0.5;
	
	// This is the Adapter being used to display the list's data
    SimpleCursorAdapter mAdapter;
	
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);      
	
	}
	
	public static FindActivity newInstance(LatLng newLoc) {
		FindActivity frag = new FindActivity();
		frag.loc = newLoc;
		return frag;
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
        
        // setup parse query (for all objects)
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
        				// get the message
        				String text = ((ParseObject) o).getString("text").toString();
        				// get the location
        				ParseGeoPoint pt = ((ParseObject) o).getParseGeoPoint("location");
        				// add a string combining the message and location
        				listAdapter.add(text + " (" + pt.getLatitude() + "," + pt.getLongitude() + ")");
        			}
        			
        			//indicate that the list adapter has changed its data, so the listview will update
        			listAdapter.notifyDataSetChanged();
        		}
        		else {
        			//failure
        			Toast.makeText(getActivity(), "Unable to retrieve notes", Toast.LENGTH_LONG).show();
        		}
        	}
        });
		
		//return view to be displayed
		return findView;
	}	
	

	
	/* Run when post button is clicked */
	public void postMessage(View view) {
		user= ParseUser.getCurrentUser();
		// get text from user's EditText box
		EditText editText = (EditText) getView().findViewById(R.id.messageBox);
		String message = editText.getText().toString();		
		// gather message info for Parse
		messageHolder = new ParseObject("Message");
		// gather location info for Parse
		ParseGeoPoint point = new ParseGeoPoint(loc.latitude, loc.longitude);
		messageHolder.put("text", message);
		messageHolder.put("location", point);
		messageHolder.put("user", user);

		// add to user relation
		ParseRelation relation = user.getRelation("messages");
		relation.add(messageHolder);
		user.saveInBackground();
		
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
	}
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
