package com.samirrajani.ignappq5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class VideoActivity extends Activity {

	final List<JSONObject> list = new ArrayList<JSONObject>();
	myArrayAdapter adapter;
	ListView listView;
	protected int count;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video);
				
		Button articleButton = (Button) findViewById(R.id.articleButton2);
		Button load = (Button) findViewById(R.id.loadVideosButton);
		listView = (ListView) findViewById(R.id.videoList);
		
		count = 0;
		
		articleButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(VideoActivity.this, ArticlesActivity.class);
				startActivity(intent);
			}
			
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				JSONObject json = (JSONObject) parent.getItemAtPosition(position);
				try {
					Intent i = new Intent(Intent.ACTION_VIEW);
					i.setData(Uri.parse(json.getString("url")));
					startActivity(i);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
		});
		load.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				new AsyncTask<Void, Void, JSONObject> () {
				    protected JSONObject doInBackground(Void... none) {
				        try {
				        	JSONObject json = getJson("http://ign-apis.herokuapp.com/videos?startIndex=0\u0026count=10");
				        	count += json.getInt("count");
							return getJson("http://ign-apis.herokuapp.com/videos?startIndex=" + count + "\u0026count=10");
						} catch (IOException e) {
							e.printStackTrace();
						} catch (JSONException e) {
							e.printStackTrace();
						};
						return null;
				    }
				    protected void onProgressUpdate(Void... none) { }
				    protected void onPostExecute(JSONObject json) {
				    	try {
					    	JSONArray elements = json.getJSONArray("data");
							
							
							for (int i = 0; i < elements.length(); i++) {
								list.add(elements.getJSONObject(i).getJSONObject("metadata"));						
							}
							adapter.notifyDataSetChanged();
				    	} catch (JSONException e) {
				    		e.printStackTrace();
				    	}
				    }
				}.execute();
			}
			
		});
		
		
	}
	
	public void onResume() {
		super.onResume();
				
		new AsyncTask<Void, Void, JSONObject> () {
		    protected JSONObject doInBackground(Void... none) {
		        try {
		        	JSONObject json = getJson("http://ign-apis.herokuapp.com/videos?startIndex=0\u0026count=10");
		        	count = json.getInt("startIndex");
					return json;
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				};
				return null;
		    }
		    protected void onProgressUpdate(Void... none) { }
		    protected void onPostExecute(JSONObject json) {
		    	try {
			    	JSONArray elements = json.getJSONArray("data");
					
					
					for (int i = 0; i < elements.length(); i++) {
						list.add(elements.getJSONObject(i).getJSONObject("metadata"));						
					}
					adapter = new myArrayAdapter(VideoActivity.this, list);
					listView.setAdapter(adapter);
		    	} catch (JSONException e) {
		    		e.printStackTrace();
		    	}
		    }
		}.execute();		
	}
	
	public void onPause() {
		super.onPause();
		count = 0;
		list.clear();
	}
	
	private static JSONObject getJson(String url) throws IOException, JSONException{
		InputStream is = new URL(url).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			return new JSONObject(readAll(rd));
		} finally {
			is.close();
		}
	}
	
	private static String readAll(Reader rd) throws IOException{
		StringBuilder sb = new StringBuilder();
		int current;
		while ((current = rd.read()) != -1) {
			sb.append((char) current);
		}
		return sb.toString();
	}
}
