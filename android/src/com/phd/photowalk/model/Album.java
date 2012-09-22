package com.phd.photowalk.model;

import org.json.JSONException;
import org.json.JSONObject;


public class Album {
	
	public String id;
	public String name="";
	public String thumbUrl="";
	public int totalPhotos, totalLikers, totalContributors;
	public String lat=null;
	public String lng=null;
	
	private Point point;
	
	public JSONObject toJSONObject() throws JSONException
	{
		JSONObject object = new JSONObject();
		object.put("id", this.id);
		object.put("name", this.name);
		object.put("thumbUrl", this.thumbUrl);
		object.put("totalPhotos", this.totalPhotos);
		object.put("totalLikers", this.totalLikers);
		object.put("totalContributors", this.totalContributors);
		object.put("Point", this.point.toJSONString());
		
		return object;
	}
	
	
	public static Album parseAlbum(JSONObject json) throws Exception{
		Album album = new Album();
		album.id = json.optString("id");
		album.name = json.optString("name");
		album.thumbUrl = json.optString("thumbUrl");
		album.totalPhotos = json.optInt("totalPhotos");
		album.totalLikers = json.optInt("totalLikers");
		album.totalContributors = json.optInt("totalContributors");
		
		JSONObject loc = json.optJSONObject("location");
		if( loc != null){
			album.point = new Point(Long.valueOf(loc.getString("latitude")), Long.valueOf(loc.getString("longitude")), 150);
		}
		
		return album; 
	}

}
