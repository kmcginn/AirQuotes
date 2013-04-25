package com.kmcginn.airquotes;

import java.util.List;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class FriendsFragment extends Fragment{

	private ParseUser user;
	CheckBox friendsCheck;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

		StrictMode.setThreadPolicy(policy); 
	}
	
	public FriendsFragment(){
		super();
	}
	
	public static FriendsFragment newInstance() {
		FriendsFragment frag = new FriendsFragment();
		return frag;
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View settingsView = inflater.inflate(R.layout.settings_fragment, container, false);
		
		try{
			ParseUser currUser= ParseUser.getCurrentUser();
	        Boolean friendsOnly= (Boolean) currUser.get("viewFriends");
	        friendsCheck= (CheckBox) settingsView.findViewById(R.id.viewFriendsCheck);
			friendsCheck.setChecked(friendsOnly);
		}
		catch (Exception e1){
			Log.e("check","Unable to check the box: "+e1);
		}
		
		return settingsView;
	}
	
	public void addFriendClicked(View view){
		user= ParseUser.getCurrentUser();
		
			
		// setup parse query (for all objects)
        ParseQuery query = new ParseQuery("User");
        // find them in the background
        query.findInBackground(new FindCallback() {
        	public void done(List<ParseObject> objects, ParseException e) {
        		if(e == null) {
        			//success
        			ParseRelation relation = user.getRelation("friends");
        			EditText editText = (EditText) getView().findViewById(R.id.friendText);
        			String friend = editText.getText().toString();
        			boolean found= false;
        			// add all objects from query to list adapter
        			for(Object o: objects){
        				// get the username
        				String text = ((ParseObject) o).getString("username").toString();
        				// check if correct user
        				if (text==friend){
        					relation.add((ParseUser) o);
        					user.saveInBackground();
        					found=true;
                			Toast.makeText(getActivity(), "Friend Added!", Toast.LENGTH_LONG).show();

        				}
        				// add a string combining the message and location
        			}
        			if (!found){
            			Toast.makeText(getActivity(), "Unable to find user", Toast.LENGTH_LONG).show();

        			}
        			//indicate that the list adapter has changed its data, so the listview will update
        		}
        		else {
        			//failure
        			Toast.makeText(getActivity(), "Unable to find user", Toast.LENGTH_LONG).show();
        		}
        	}
        });
		
	}
	
	public void onPause(){
		super.onPause();
		ParseUser currUser= ParseUser.getCurrentUser();
		currUser.put("viewFriends", ((CheckBox) getView().findViewById(R.id.viewFriendsCheck)).isChecked());
		try {
			currUser.save();
		} catch (ParseException e) {
			Log.e("check","Unable to saved friend preference: "+e);
		}
	}
	/*
	 * Set up the {@link android.app.ActionBar}.
	 */
	/*
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}
	
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
