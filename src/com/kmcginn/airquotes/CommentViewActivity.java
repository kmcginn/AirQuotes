package com.kmcginn.airquotes;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;


public class CommentViewActivity extends Activity {
	Context context= this;
	ParseObject post;
	String postID = "0";
	String postText = "";
	String comment = "";
	String username = "";
	String date = "";
	int altitude = 0;
	private ArrayAdapter<String> listAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment_view);
		
		// load in the post's id

		Intent intent = getIntent();
		try{
			postID = intent.getStringExtra("objId");
			Log.e("post", "Post id: " + postID);
		} catch(Exception e1) {
			Log.e("newAct", "Unable to get objectId from previous activity: " + e1);			
		}
		// query to get full post information
        ParseQuery query = new ParseQuery("Message");
        query.getInBackground(postID, new GetCallback() {
        	public void done(ParseObject o, ParseException e) {
        		if(e == null) {
        			//success
        			
        			// add from query to list adapter
        			try {
	        			
	        				// get the message text
	        				postText = o.getString("text").toString();
	        				Log.e("post", "postText: " + postText);
	        				//get the message's user
	        				username = o.getString("user").toString();
	        				//get the message's date
	        				date = o.getCreatedAt().toString();
	        				//get the message's altitude
	        				altitude = o.getInt("altitude");
	        				// save parseObject
	        				post= o;
	        				
	        				//setup the gui
	        				 try{
	        			        	// put new info in text boxes
	        			        	TextView postTextBox = (TextView) findViewById(R.id.postText);
	        			        	Log.e("gui", "Text for box: " + postText);
	        			        	postTextBox.setText(postText);
	        			        	TextView dateTextBox = (TextView) findViewById(R.id.dateText);
	        				        dateTextBox.setText(date);
	        				        TextView userTextBox = (TextView) findViewById(R.id.usernameText);
	        				        userTextBox.setText(username);
	        				        TextView altTextBox = (TextView) findViewById(R.id.altText);
	        				        altTextBox.setText(Integer.toString(altitude));
	        			        } catch(Exception e2) {
	        			        	Log.e("gui", "Unable to intialize text boxes: " + e2);
	        			        	
	        			        }
	        			
        			}
        			catch (Exception e1){
            			Log.e("query", "Exception while retrieving post: " + e1.getMessage());
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
        TextView postTextBox = (TextView) findViewById(R.id.postText);
        postTextBox.setText(postText);
        TextView dateTextBox = (TextView) findViewById(R.id.dateText);
        dateTextBox.setText(date);
        TextView userTextBox = (TextView) findViewById(R.id.usernameText);
        userTextBox.setText(username);
        TextView altTextBox = (TextView) findViewById(R.id.altText);
        altTextBox.setText(altitude);
        
        //
        //Update Comments
        //

  		 // get ListView from activity's View
          ListView listView = (ListView) findViewById(R.id.list);
          // init arraylist adapter
          listAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1);
          // attach the adapter to the listview
          listView.setAdapter(listAdapter);
          
          // setup parse query (for comments)
          ParseRelation relation = post.getRelation("Comments");
          ParseQuery commentQuery = relation.getQuery();
          
          // find them in the background
          commentQuery.findInBackground(new FindCallback() {
          	public void done(List<ParseObject> objects, ParseException e) {
          		if(e == null) {
          			//success
          			
          			// add all objects from query to list adapter
          			for(Object o: objects){
          				// get the message
          				String text = ((ParseObject) o).getString("text").toString();
          				// get the user
          				String user = ((ParseObject) o).getString("user").toString();
          				// add a string combining the message and user
          				listAdapter.add(text + "\n" + user);
          			}
          			
          			//indicate that the list adapter has changed its data, so the listview will update
          			listAdapter.notifyDataSetChanged();
          		}
          		else {
          			//failure
          			Toast.makeText(context, "Unable to retrieve comments", Toast.LENGTH_LONG).show();
          		}
          	}
          });
    
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
		TextView altTextBox = (TextView) findViewById(R.id.altText);
        altTextBox.setText(altitude);
		
		
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
		TextView altTextBox = (TextView) findViewById(R.id.altText);
        altTextBox.setText(altitude);
		
    }
	
	public void onAddCommentClicked(View view){
		// get comment from EditText
		EditText commentTextBox= (EditText) findViewById(R.id.CommentEdit);
		String comment= commentTextBox.getText().toString();
		
		// get current user
		ParseUser currUser= ParseUser.getCurrentUser();

		// upload the comment
		ParseObject commentHolder= new ParseObject("Comment");
		commentHolder.put("text", comment);
		commentHolder.put("user", currUser.getUsername());
		post.saveInBackground(new SaveCallback() {
			public void done(ParseException e) {
				if(e == null) {
					// saved successfully
					Toast.makeText(context, "Comment saved", Toast.LENGTH_SHORT).show();
				}
				else {
				
					// did not save successfully
					Toast.makeText(context, "Unable to save comment: " + e.getMessage(), Toast.LENGTH_LONG).show();
				}
			}
		});
		
		//	update the relationship
		ParseRelation relation = post.getRelation("Comments");
		relation.add(commentHolder);
		
	}
	
	
}
