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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class ArticlesActivity extends Activity {
	final List<JSONObject> list = new ArrayList<JSONObject>();
	myArrayAdapter adapter;
	ListView listView;
	protected int count;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_articles);
				
		Button videoButton = (Button) findViewById(R.id.videoButton);
		Button load = (Button) findViewById(R.id.loadArticlesButton);
		listView = (ListView) findViewById(R.id.articleList);
		
		count = 0;
		
		videoButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ArticlesActivity.this, VideoActivity.class);
				startActivity(intent);
			}
			
		});
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				JSONObject json = (JSONObject) parent.getItemAtPosition(position);
				try {
					String url = buildURL(json);
					Intent i = new Intent(Intent.ACTION_VIEW);
					i.setData(Uri.parse(url));
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
				        	JSONObject json = getJson("http://ign-apis.herokuapp.com/articles?startIndex=0\u0026count=10");
				        	count += json.getInt("count");
							return getJson("http://ign-apis.herokuapp.com/articles?startIndex=" + count + "\u0026count=10");
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
		        	JSONObject json = getJson("http://ign-apis.herokuapp.com/articles?startIndex=0\u0026count=10");
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
					adapter = new myArrayAdapter(ArticlesActivity.this, list);
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
	
	private String buildURL(JSONObject json) throws JSONException {
		StringBuilder sb = new StringBuilder();
		sb.append("http://ign.com");
		sb.append("/articles");
		String s = json.getString("publishDate");//getJsonArray("data").getJsonObject(0).getJsonObject("metadata").getString("publishDate");
		s = s.split("T")[0];
		String[] date = s.split("-");
		sb.append("/");
		sb.append(date[0]);
		sb.append("/");
		sb.append(date[1]);
		sb.append("/");
		sb.append(date[2]);
		sb.append("/");
		String slug = json.getString("slug");//getJsonArray("data").getJsonObject(0).getJsonObject("metadata").getString("slug");
		sb.append(slug);
		return sb.toString();
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
