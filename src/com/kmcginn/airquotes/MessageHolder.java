package com.kmcginn.airquotes;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import android.util.Log;
import android.widget.ArrayAdapter;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

public class MessageHolder {

	Set<ParseObject> messages = new LinkedHashSet<ParseObject>();
	Set<ParseObject> friends = new LinkedHashSet<ParseObject>();

	//constructor
	public MessageHolder() {
		refreshFriends();
	}
	
	public void refreshFriends() {
		//initialize friends here based on current parse user
				ParseUser currUser= ParseUser.getCurrentUser();
				// get relation and query
		        ParseRelation friendsRel= currUser.getRelation("friends");
		        ParseQuery friendsQuery= friendsRel.getQuery();
		        
		        friendsQuery.findInBackground(new FindCallback() {
					public void done(List<ParseObject> users, ParseException e) {
						if (e == null){
							for(ParseObject o: users){
								try {
								friends.add( o);
								}
								catch (Exception e1){
									Log.e("collection","Couldn't add to collection: "+o.getString("username")+e1);
								}
							}
						}
						else {
		    				Log.e("friends", "Unable to download friends: " + e);

						}
						
					}
		        	
		        });
		
	}
	
	public void refreshMap(double nearbyRadius, LatLng loc, final GoogleMap mMap, final HashMap<Marker, ParseObject> idMap) {
		
		Boolean friendsOnly;
		
		friendsOnly= (Boolean) ParseUser.getCurrentUser().get("viewFriends");
		
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
        			// add all objects from query to set
        			for(ParseObject o: objects){
        				
        				messages.add(o);
        			}
        			
        			
        			//TODO: move this into a function?
        			if(mMap != null) {
        				
        				mMap.clear();
        				Marker temp;
        				for(ParseObject o: messages) {
        					
        					// get the message text
            				String text = ((ParseObject) o).getString("text").toString();
            				//get the message's user
            				String user = ((ParseObject) o).getString("user").toString();
            				// get the location
            				ParseGeoPoint pt = ((ParseObject) o).getParseGeoPoint("location");
            				temp = mMap.addMarker(new MarkerOptions()
            					.position(new LatLng(pt.getLatitude(), pt.getLongitude()))
            					.title(text)
            					.snippet(user));
            				idMap.put(temp, o);
        				}
        				
        			}
        			
        		}
        		else {
        			//failure
        			Log.e("find","Unable to find posts: "+e);
       			
        		}
        	}
        });
		}
		catch (Exception e1){
			Log.e("find","Unable to enter findCallback: "+e1);
			
		}
		
		
	}
	
	public Set<ParseObject> getAll() {
		
		return messages;
	}
	
	public ParseObject get(int index) {
		return (ParseObject) messages.toArray()[index];
		
	}
	
	public void refreshList(double nearbyRadius, LatLng loc, final ArrayAdapter<String> listAdapter) {
		
		Boolean friendsOnly;
		
		friendsOnly= (Boolean) ParseUser.getCurrentUser().get("viewFriends");
		
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
        			// add all objects from query to set
        			for(ParseObject o: objects){
        				
        				messages.add(o);
        			}
        			
        			
        			//TODO: move this into a function?
        			if(listAdapter != null) {
        				
        				for(ParseObject o: messages) {
        					// get the message
    	        			String text = o.getString("text").toString();
    	        			// get the author
    	        			String author = o.getString("user").toString();
    	        			// add a string combining the message and location
    	        			listAdapter.add(text + "\n" + author);      					
        				}
        				
        				//indicate that the list adapter has changed its data, so the listview will update
        				listAdapter.notifyDataSetChanged();
        			}
        			
        		}
        		else {
        			//failure
        			Log.e("find","Unable to find posts: "+e);
       			
        		}
        	}
        });
		}
		catch (Exception e1){
			Log.e("find","Unable to enter findCallback: "+e1);
			
		}
		
	}
}
