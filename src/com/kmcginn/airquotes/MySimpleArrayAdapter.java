package com.kmcginn.airquotes;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MySimpleArrayAdapter extends ArrayAdapter<Map<String, String>> {
	  private final Context context;
	  private Map<String, String> data;

	  public MySimpleArrayAdapter(Context context) {
	    super(context, R.layout.list_item);
	    this.context = context;
	    data = new HashMap<String, String>();
	  }

	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = inflater.inflate(R.layout.list_item, parent, false);
	    TextView messageView = (TextView) rowView.findViewById(R.id.messageText);
	    TextView authorView = (TextView) rowView.findViewById(R.id.authorText);
	    TextView altView = (TextView) rowView.findViewById(R.id.altitudeText);
	    
	    messageView.setText(data.get("message"));
	    authorView.setText(data.get("author"));
	    altView.setText(data.get("altitude"));
	    
		return rowView;
	  }
	  
	  public void add(String tag, String data) {
		this.data.put(tag, data);  
	  }
} 