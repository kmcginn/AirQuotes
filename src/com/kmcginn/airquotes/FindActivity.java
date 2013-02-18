package com.kmcginn.airquotes;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class FindActivity extends Activity{

	final Context context = this;
	
	private ArrayAdapter<String> listAdapter;
	
	// This is the Adapter being used to display the list's data
    SimpleCursorAdapter mAdapter;
	
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find);
		// Make sure we're running on Honeycomb or higher to use ActionBar APIs
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
        	// Show the Up button in the action bar.
        	getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // get ListView from activity's View
        ListView listView = (ListView) findViewById(R.id.list);
        // init arraylist adapter
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        // attach the adapter to the listview
        listView.setAdapter(listAdapter);
        
        // setup parse query (for all objects)
        ParseQuery query = new ParseQuery("Message");
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
        			Toast.makeText(context, "Unable to retrieve notes", Toast.LENGTH_LONG).show();
        			// go back to main screen
        			Intent intent = new Intent(context, MainActivity.class);
        			startActivity(intent);
        		}
        	}
        });
	
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_find, menu);
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

}
