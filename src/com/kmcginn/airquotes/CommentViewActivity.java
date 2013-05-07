package com.kmcginn.airquotes;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
	ParseObject mainPost=null;
	ParseObject updatedCommentHolder;
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
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
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
        try {
			query.getInBackground(postID, new GetCallback() {
				
	        	public void done(ParseObject post, ParseException e) {

				if (e == null){
        			try {
        					mainPost= post;

	        				// get the message text
	        				postText = post.getString("text").toString();
	        				Log.e("post", "postText: " + postText);
	        				//get the message's user
	        				username = post.getString("user").toString();
	        				//get the message's date
	        				date = post.getCreatedAt().toString();
	        				//get the message's altitude
	        				altitude = post.getInt("altitude");
	        				
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
            			Intent intent2= new Intent(context, MainActivity.class);
            	    	startActivity(intent2);
        			}
        			loadComments();
				}
				else	{
					Toast.makeText(context, "Query failed", Toast.LENGTH_SHORT).show();
				}
			}});
			} catch (Exception e3) {
				Log.e("post","Post query raised an exception: "+e3);
			}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.comment_view, menu);
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
			// TODO: If Settings has multiple levels, Up should navigate up
			// that hierarchy.
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void onClimbClicked(View view){
		
		altitude+=1;
		
		mainPost.increment("altitude");
		mainPost.saveInBackground(new SaveCallback() {
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
        altTextBox.setText(Integer.toString(altitude));
		
		
		
    }
	
	public void onDiveClicked(View view){
				
		altitude -= 1;
		mainPost.increment("altitude", -1);
		mainPost.saveInBackground(new SaveCallback() {
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
        altTextBox.setText(Integer.toString(altitude));
		
    }
	
	public void onAddCommentClicked(View view){
		// get comment from EditText
		EditText commentTextBox= (EditText) findViewById(R.id.CommentEdit);
		String comment= commentTextBox.getText().toString();
		commentTextBox.setText("");

		// get current user
		ParseUser currUser= ParseUser.getCurrentUser();

		// upload the comment
		ParseObject commentHolder= new ParseObject("Comment");
		commentHolder.put("text", comment);
		commentHolder.put("user", currUser.getUsername());
		final ParseObject fullComment = commentHolder;
		try {
			commentHolder.saveInBackground(new SaveCallback() {

				@Override
				public void done(ParseException e) {
					try {
						//	update the relationship
						ParseRelation relation = mainPost.getRelation("Comments");
						relation.add(fullComment);
			      	} catch (Exception e1){
				    	Log.e("relation", "Exception while adding a relation: "+e1);
				    }

					mainPost.saveInBackground(new SaveCallback() {
						public void done(ParseException e) {
							if(e == null) {
								// saved successfully
								//Toast.makeText(context, "Relation saved", Toast.LENGTH_SHORT).show();
							}
							else {
							
								// did not save successfully
								//Toast.makeText(context, "Unable to save relation: " + e.getMessage(), Toast.LENGTH_LONG).show();
							}
						}
					});
					
					loadComments();
					
				}
				
				
			});
			// saved successfully
			Toast.makeText(context, "Comment saved", Toast.LENGTH_SHORT).show();
		} catch(Exception e1){
			// did not save successfully
			Toast.makeText(context, "Unable to save comment: " + e1.toString(), Toast.LENGTH_LONG).show();
		}
      	
      	
	}
	public void loadComments(){
		// get ListView from activity's View
        ListView listView = (ListView) findViewById(R.id.commentList);
        // init arraylist adapter
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        // attach the adapter to the listview
        try {
        	listView.setAdapter(listAdapter);
        } catch (Exception e1) {
        	Log.e("adapter", "Exception while creating listAdaptor: "+e1);
        }
  	  
        try {
        	// setup parse query (for comments)
        	ParseRelation relation = mainPost.getRelation("Comments");
        	ParseQuery commentQuery = relation.getQuery();
        	commentQuery.orderByAscending("createdAt");
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
        } catch(Exception e1){
		  Log.e("query", "Exception in querying relation: "+e1);
		}
 	}
}