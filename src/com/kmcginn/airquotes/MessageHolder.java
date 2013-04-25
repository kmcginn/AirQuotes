package com.kmcginn.airquotes;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

public class MessageHolder {

	Set<ParseObject> messages = new TreeSet<ParseObject>();
	Set<ParseObject> friends = new TreeSet<ParseObject>();

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
	
	public void refreshList(Boolean friendsOnly, double nearbyRadius, LatLng loc) {
		
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
        			Log.e("loc","After for loop");

        			
        			
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
