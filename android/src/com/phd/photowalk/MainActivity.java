package com.phd.photowalk;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.phd.photowalk.api.SimpleEyeEmAPI;
import com.phd.photowalk.model.Album;
import com.phd.photowalk.widgets.ActionBar;

public class MainActivity extends Activity implements IFLocationUpdate{
	private PWalkApplication app;
	private LoadAlbumsAroundYouTask aroundTask;
	
	public static final String FOOD = "Restaurants, Italian Restaurant, Burger Joint, Fast Food Restaurant, Pizza, Noodle Haus, Ice Cream, Pizza Place, Korean Restaurant";
	public static final String DRINK = "Bars, Pub, Cocktail Bar, Beer Garden";
	public static final String CHILL = "Parks, Garden";
	public static final String PARTY = "Rock Club, Concert Hall";
	public static final String CAFE = "Café, Bagel Shop, Cupcake, Bakery";
	public static final String WORK = "Office, Tech Startup, Art Gallery, Event Space";
	public static final String SHOP = "Boutique, Clothing Store, Shoe Store";
	
	public static final int CATEGORY_FOOD	=	0;
	public static final int CATEGORY_DRINK	=	1;
	public static final int CATEGORY_CHILL	=	2;
	public static final int CATEGORY_PARTY	=	3;
	public static final int CATEGORY_CAFE	=	4;
	public static final int CATEGORY_WORK	=	5;
	public static final int CATEGORY_SHOP	=	6;
	
	private int[] numCategories = new int[7];
	
	private List<String> categories;
	private List<Integer> availableCategories;
	
	private ActionBar actionBar;
	
	private float mLat=0;
	private float mLon=0;
	
	private static final double MAX_DISTANCE = 1.0;
	
	private HashMap<Integer, String> categoryMap;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (PWalkApplication) getApplication();
        setContentView(R.layout.main);
        
        actionBar = (ActionBar) findViewById(R.id.actionbar);
        actionBar.setTitle("What we found around you: ");
        
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
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	return super.onOptionsItemSelected(item);
    }
    
    
    @Override
    protected void onResume() {
    	super.onResume();
    	
    	if(mLat > 0 && mLon >0){
    		double distance = distFrom(app.getLastKnownLocation().getLatitude(), app.getLastKnownLocation().getLongitude(), mLat, mLon);
    		
    		if(distance > MAX_DISTANCE){
    			aroundTask = new LoadAlbumsAroundYouTask();
    			sendLocation((float)app.getLastKnownLocation().getLatitude(), (float)app.getLastKnownLocation().getLongitude(),0);
    		}
    	}
    	
    }
    
	/**
	 * listener method called when the location of the user has changed
	 * used for informing the ArchitectView about a new location of the user
	 */
	@Override
	public void sendLocation(float lat, float lon, float accuracy) {
		if(aroundTask.getStatus() == Status.PENDING){
			mLat = lat;
			mLon = lon;
			aroundTask.execute(lat,lon);
		}
	}
	
	private void setCategories(){
		//clear
		for (int i = 0; i < numCategories.length; i++) {
			numCategories[i] = 0;
		}
		
		for(Album album : app.albumList){
			Iterator<Entry<Integer, String>> it = categoryMap.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry<Integer, String> pairs = (Map.Entry<Integer, String>)(it.next());
				if(pairs.getValue().contains(album.venueType) && album.venueType.length() > 0){
					album.category = pairs.getKey();
					numCategories[pairs.getKey()]++;
				}
				
				if(!availableCategories.contains(album.category) && album.category != -1)
					availableCategories.add(album.category);
				
			}
			
		}
	}
	
	private class LoadAlbumsAroundYouTask extends AsyncTask<Float, Void, Void> {
		
		@Override
		protected void onPreExecute() {
			actionBar.setBusy(true);
		}
		
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
			actionBar.setBusy(false);
		}
	}
	
	private class TagAdapter extends ArrayAdapter<Integer>{
		

		public TagAdapter(Context context, int textViewResourceId) {
			super(context, textViewResourceId);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final Integer category = availableCategories.get(position);
			
			convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.places_adapter, null);
			
			TextView title = (TextView) convertView.findViewById(R.id.title);
			title.setText(getTextforAdapter(category));
			
			ImageView imgView = (ImageView) convertView.findViewById(R.id.image);
			imgView.setBackgroundResource(getBackgroundResource(category));
			
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent openAR = new Intent(MainActivity.this,ARActivity.class);
					openAR.putExtra(ARActivity.EXTRA_CATEGORY, category);
					startActivity(openAR);
				}
			});
			
			return convertView;
		}
		
		@Override
		public int getCount() {
			return availableCategories.size();
		}
		
	}
	
	private int getBackgroundResource(int category) {
		switch (category) {
		case CATEGORY_CAFE:
			return R.drawable.coffee;
		
		case CATEGORY_CHILL:
			return R.drawable.chillout;
			
		case CATEGORY_DRINK:
			return R.drawable.drunk;
		
		
		case CATEGORY_FOOD:
			return R.drawable.food;
		
		
		case CATEGORY_PARTY:
			return R.drawable.party;
			
			
		case CATEGORY_SHOP:
			return R.drawable.buy;
		
		case CATEGORY_WORK:
			return R.drawable.money;

	default:
		break;
		}
		
		return 0;
	}
	
	private String getTextforAdapter(Integer category){
		switch (category) {
			case CATEGORY_CAFE:
				return numCategories[category]+(numCategories[category] > 1 ? " places" : " place")+" to sit down and and enjoy a coffee";
			
			case CATEGORY_CHILL:
				return numCategories[category]+(numCategories[category] > 1 ? " places" : " place")+" to catch some fresh air";
				
			case CATEGORY_DRINK:
				return numCategories[category]+(numCategories[category] > 1 ? " places" : " place")+" to get wasted";
			
			case CATEGORY_FOOD:
				return numCategories[category]+(numCategories[category] > 1 ? " places" : " place")+" to get some food";
			
			case CATEGORY_PARTY:
				return numCategories[category]+(numCategories[category] > 1 ? " places" : " place")+" to party until sunrise";
				
			case CATEGORY_SHOP:
				return numCategories[category]+(numCategories[category] > 1 ? " places" : " place")+" to spend some money";
			
			case CATEGORY_WORK:
				return numCategories[category]+(numCategories[category] > 1 ? " places" : " place")+" to earn some money!";

		default:
			break;
		}
		
		return null;
	}
	
	public static double distFrom(double lat1, double lng1, double lat2, double lng2) {
		 double earthRadius = 3958.75;
		 double dLat = Math.toRadians(lat2-lat1);
		 double dLng = Math.toRadians(lng2-lng1);
		 double sindLat = Math.sin(dLat / 2);
		 double sindLng = Math.sin(dLng / 2);
		 double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2) * Math.cos(lat1) * Math.cos(lat2);
		 double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
		 double dist = earthRadius * c;
		
		 return dist;
	}
	
	
}