package com.phd.photowalk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.phd.photowalk.api.SimpleEyeEmAPI;
import com.phd.photowalk.model.Album;

public class MainActivity extends Activity implements IFLocationUpdate/*, ArchitectUrlListener*/{
	private PHDApplication app;
	private LoadAlbumsAroundYouTask aroundTask;
	
	public static final String FOOD = "Restaurants, Italien Restaurant, Burger Joint, Fast Food Restaurant, Pizza, Noodle Haus, Ice Cream";
	public static final String DRINK = "Bars, Pub, Cocktail Bar";
	public static final String CHILL = "Parks, Garden";
	public static final String PARTY = "Rock Club, Concert Hall";
	public static final String CAFE = "Caf�, Bagel Shop, Cupcake";
	public static final String WORK = "Office, Tech Startup";
	public static final String SHOP = "Boutique, Clothing Store, Shoe Store";
	
	public static final int CATEGORY_FOOD=0;
	public static final int CATEGORY_DRINK=1;
	public static final int CATEGORY_CHILL=2;
	public static final int CATEGORY_PARTY=3;
	public static final int CATEGORY_CAFE=4;
	public static final int CATEGORY_WORK=5;
	public static final int CATEGORY_SHOP=6;
	
	private List<String> categories;
	private List<Integer> availableCategories;
	
	private HashMap<Integer, String> categoryMap;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (PHDApplication) getApplication();
        setContentView(R.layout.main);
        aroundTask = new LoadAlbumsAroundYouTask();
        
        categories = new ArrayList<String>();
        categories.add(CAFE);
        categories.add(CHILL);
        categories.add(PARTY);
        categories.add(DRINK);
        categories.add(SHOP);
        categories.add(WORK);
        categories.add(FOOD);
        
        categoryMap = new HashMap<Integer, String>();
        categoryMap.put(CATEGORY_FOOD, FOOD);
        categoryMap.put(CATEGORY_DRINK, DRINK);
        categoryMap.put(CATEGORY_CHILL, CHILL);
        categoryMap.put(CATEGORY_PARTY, PARTY);
        categoryMap.put(CATEGORY_CAFE, CAFE);
        categoryMap.put(CATEGORY_WORK, WORK);
        categoryMap.put(CATEGORY_SHOP, SHOP);
        
        availableCategories = new ArrayList<Integer>();
        
        app.setLocationUpdater(this);
     }
    
	/**
	 * listener method called when the location of the user has changed
	 * used for informing the ArchitectView about a new location of the user
	 */
	@Override
	public void sendLocation(float lat, float lon, float accuracy) {
		if(aroundTask.getStatus() == Status.PENDING)
			aroundTask.execute(lat,lon);
	}
	
	private void setCategories(){
		for(Album album : app.albumList){
			Iterator it = categoryMap.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry<Integer, String> pairs = (Map.Entry<Integer, String>)(it.next());
				if(pairs.getValue().contains(album.venueType) && album.venueType.length() > 0)
					album.category = pairs.getKey();
				
				if(!availableCategories.contains(album.category) && album.category != -1)
					availableCategories.add(album.category);
				
			}
			
		}
	}
	
	private class LoadAlbumsAroundYouTask extends AsyncTask<Float, Void, Void> {
		@Override
		protected Void doInBackground(Float... params) {
			app.albumList = new ArrayList<Album>();
			JSONObject jsonAlbumsObject = SimpleEyeEmAPI.getAlbumsAroundYou(""+params[0], ""+params[1]);
			
			try{
				jsonAlbumsObject = jsonAlbumsObject.getJSONObject("albums");
				
				JSONArray albums = null; 
				albums = jsonAlbumsObject.getJSONArray("items");
				
				for (int i = 0; i < albums.length(); i++) {
					JSONObject album = albums.getJSONObject(i);
					app.albumList.add(Album.parseAlbum(album));
				}
				
			}catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			setCategories();
			
			((ListView)findViewById(R.id.tagList)).setAdapter(new TagAdapter(MainActivity.this, android.R.id.title));
		}
	}
	
	private class TagAdapter extends ArrayAdapter<Integer>{
		

		public TagAdapter(Context context, int textViewResourceId) {
			super(context, textViewResourceId);
			// TODO Auto-generated constructor stub
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final Integer category = availableCategories.get(position);
			
			TextView title = new TextView(MainActivity.this);
			title.setText(getTextforAdapter(category));
			title.setPadding(20, 20, 20, 20);
			title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
			
			title.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent openAR = new Intent(MainActivity.this,ARActivity.class);
					openAR.putExtra(ARActivity.EXTRA_CATEGORY, category);
					startActivity(openAR);
				}
			});
			
			return title;
		}
		
		@Override
		public int getCount() {
			return availableCategories.size();
		}
		
	}
	
	private String getTextforAdapter(Integer category){
		switch (category) {
			case CATEGORY_CAFE:
				return "Looking for a nice coffee?";
			
			case CATEGORY_CHILL:
				return "Looking for a place to relaxe?";
				
			case CATEGORY_DRINK:
				return "Wanna get hammered?";
			
			
			case CATEGORY_FOOD:
				return "Need fooood!!!!";
			
			
			case CATEGORY_PARTY:
				return "Gimme some music";
				
				
			case CATEGORY_SHOP:
				return "Lets waste money";
			
			case CATEGORY_WORK:
				return "Gimme some work, I need money!";

		default:
			break;
		}
		
		return null;
	}
}