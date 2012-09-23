package com.phd.photowalk.model;

import org.json.JSONException;
import org.json.JSONObject;


public class Album {
	
	public String id;
	public int index;
	public String name="";
	public String thumbUrl="";
	public String type;
	public int totalPhotos, totalLikers, totalContributors;
	public String venueType="";
	public int category=-1;
	public Point point;
	
	public JSONObject toJSONObject() throws JSONException
	{
		JSONObject object = new JSONObject();
		object.put("id", this.id);
		object.put("index", this.index);
		object.put("name", this.name);
		object.put("type", this.type);
		object.put("thumbUrl", this.thumbUrl);
		object.put("totalPhotos", this.totalPhotos);
		object.put("totalLikers", this.totalLikers);
		object.put("totalContributors", this.totalContributors);
		object.put("category", this.category);
		object.put("Point", this.point.toJSONString());
		
		return object;
	}
	
	
	public static Album parseAlbum(JSONObject json) throws Exception{
		Album album = new Album();
		album.id = json.optString("id");
		album.name = json.optString("name");
		album.thumbUrl = json.optString("thumbUrl");
		album.type = json.optString("type");
		album.totalPhotos = json.optInt("totalPhotos");
		album.totalLikers = json.optInt("totalLikers");
		album.totalContributors = json.optInt("totalContributors");
		
		JSONObject loc = json.optJSONObject("location");
		if( loc != null){
			album.point = new Point(Float.valueOf(loc.getString("latitude")), Float.valueOf(loc.getString("longitude")), 0);
			
			JSONObject venueService = loc.optJSONObject("venueService");
			if(venueService != null && venueService.has("categoryName"))
					album.venueType = venueService.getString("categoryName");
		}
		
		return album; 
	}

}
