package com.kmcginn.airquotes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.parse.Parse;

public class MainActivity extends Activity {	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// set up Parse
		Parse.initialize(this, "AyI8XoEioHWsBFl7PmQWMLbiJ6woG6O7uJFfqAYK", "nzKyPzUYD5UUJIJSxZ8jUqCp9rWQxGXc7XnmdA85");
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	// called when Post button is clicked
	public void goToMessage(View view) {
		// go to activity for posting messages
		Intent intent = new Intent(this, PostActivity.class);
		startActivity(intent);
	}

	//called when View button is clicked
	public void goToList(View view) {
		// go to activity that lists all messages
		Intent intent = new Intent(this, FindActivity.class);
		startActivity(intent);
	}
}
