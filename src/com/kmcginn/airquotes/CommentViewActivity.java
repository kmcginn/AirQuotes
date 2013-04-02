package com.kmcginn.airquotes;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;


public class CommentViewActivity extends Activity {
	Context context= this;
	ParseObject post;
	String postID;
	String postText;
	String comment;
	String username;
	String date;
	int altitude;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment_view);
		
		// load in the post's id
		Bundle extras = getIntent().getExtras();
		if (extras!=null){
		    postID = extras.getString("user");			
		}
		
		// query to get full post information
        ParseQuery query = new ParseQuery("Message");
        query.whereEqualTo("objectId", postID);
     // find in the background
        query.findInBackground(new FindCallback() {
        	public void done(List<ParseObject> objects, ParseException e) {
        		if(e == null) {
        			//success
        			
        			// add from query to list adapter
        			try {
	        			for(Object o: objects){
	        				// get the message text
	        				postText = ((ParseObject) o).getString("text").toString();
	        				//get the message's user
	        				username = ((ParseObject) o).getString("user").toString();
	        				//get the message's date
	        				date = ((ParseObject) o).getString("createdOn").toString();
	        				//get the message's altitude
	        				altitude = ((ParseObject) o).getInt("altitude");
	        				// save parseObject
	        				post= (ParseObject) o;
	        			}
        			}
        			catch (Exception e1){
            			Toast.makeText(context, "Exception while retrieving post: " + e1.getMessage(), Toast.LENGTH_LONG).show();
            			Intent intent= new Intent(context, MainActivity.class);
            	    	startActivity(intent);
        			}
      
        		}
        		else {
        			//failure
        			Toast.makeText(context, "Unable to retrieve post", Toast.LENGTH_LONG).show();
        			Intent intent= new Intent(context, MainActivity.class);
        	    	startActivity(intent);
        		}
        	}
        });

        // put new info in text boxes
        EditText postTextBox = (EditText) findViewById(R.id.postText);
        postTextBox.setText(postText);
        EditText dateTextBox = (EditText) findViewById(R.id.dateText);
        dateTextBox.setText(date);
        EditText userTextBox = (EditText) findViewById(R.id.usernameText);
        userTextBox.setText(username);
        EditText altTextBox = (EditText) findViewById(R.id.altText);
        altTextBox.setText(altitude);
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.comment_view, menu);
		return true;
	}

	public void onDoneClicked(View view){
		Intent intent= new Intent(context, MainActivity.class);
    	startActivity(intent);	
    }
	
	public void onClimbClicked(View view){
		altitude+=1;
		
		post.put("altitude", altitude);
		post.saveInBackground(new SaveCallback() {
			public void done(ParseException e) {
				if(e == null) {
					// saved successfully
					Toast.makeText(context, "Altitude updated", Toast.LENGTH_SHORT).show();
				}
				else {
				
					// did not save successfully
					Toast.makeText(context, "Unable to update altitude: " + e.getMessage(), Toast.LENGTH_LONG).show();
				}
			}
		});
		
		
    }
	
	public void onDiveClicked(View view){
		altitude-=1;
		
		post.put("altitude", altitude);
		post.saveInBackground(new SaveCallback() {
			public void done(ParseException e) {
				if(e == null) {
					// saved successfully
					Toast.makeText(context, "Altitude updated", Toast.LENGTH_SHORT).show();
				}
				else {
				
					// did not save successfully
					Toast.makeText(context, "Unable to update altitude: " + e.getMessage(), Toast.LENGTH_LONG).show();
				}
			}
		});
		
		
    }
	

	
}
