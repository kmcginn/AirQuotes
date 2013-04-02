package com.kmcginn.airquotes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.parse.ParseQuery;


public class CommentViewActivity extends Activity {
	Context context= this;
	String postID;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment_view);
		Bundle extras = getIntent().getExtras();
		if (extras!=null){
		    postID = extras.getString("user");			
		}
        ParseQuery query = new ParseQuery("Message");
        query.whereEqualTo("objectId", postID);

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
}
