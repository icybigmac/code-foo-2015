package com.samirrajani.ignappq5;

import java.util.List;

// import javax.json.JsonObject;



import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class myArrayAdapter extends ArrayAdapter<JSONObject>{

	private Context context;
	private List<JSONObject> objects;

	public myArrayAdapter(Context context, List<JSONObject> objects) {
		super(context, R.layout.articleandvideorow, objects);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.objects = objects;
	}
	
	public View getView(int position, View convertView, ViewGroup parent){
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = inflater.inflate(R.layout.articleandvideorow, parent, false);
	    TextView number = (TextView) rowView.findViewById(R.id.leftNumber);
	    TextView name = (TextView) rowView.findViewById(R.id.firstLine);
	    TextView subName = (TextView) rowView.findViewById(R.id.secondLine);
	    if (position < 9) {
	    	number.setText("0" + Integer.toString(position + 1));
	    } else {
	    	number.setText(Integer.toString(position + 1));
	    }
	    try {
		    if (objects.get(position).has("headline")) {
		    	name.setText(objects.get(position).getString("headline"));
		    	subName.setText(objects.get(position).getString("subHeadline"));
		    } else if (objects.get(position).has("longTitle")){
		    	name.setText(objects.get(position).getString("longTitle"));
		    	subName.setText(objects.get(position).getString("title"));
		    }
	    } catch (JSONException e) {
	    	e.printStackTrace();
	    }
	    return rowView;
	  }

}
