package com.phd.photowalk.model;

import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONObject;

public class Photo{
	
	public int photoId;
	public int width;
	public int height;
	public String thumbUrl;
	public String photoUrl;
	public String filename;
	public String caption;
	public String title;
	public Vector<Album> albums;
	
	public String updatedString;
	public long updated;
	
	public Photo(){
		albums = new Vector<Album>();
	}
	
	public Photo(int photoId, String thumbUrl, String photoUrl, int width, int height){
		this.photoId = photoId;
		this.thumbUrl = thumbUrl;
		this.photoUrl = photoUrl;
		this.width = width;
		this.height = height;
		
		albums = new Vector<Album>();
	}
	
	public static Photo parsePhoto(JSONObject json) throws Exception{
		Photo photo = new Photo();
		photo.photoId = json.optInt("id");
		photo.width = json.optInt("width");
		photo.height = json.optInt("height");
		photo.thumbUrl = json.optString("thumbUrl");
		photo.filename = json.optString("filename");
		photo.caption = json.optString("caption");
		photo.title = json.optString("title");
		
		JSONObject albumsObject = json.optJSONObject("albums");
		
		if(albumsObject != null){
			JSONArray albums = albumsObject.optJSONArray("items");
			if(albums != null){
				for (int i = 0; i < albums.length(); i++) {
					photo.albums.add(Album.parseAlbum(albums.getJSONObject(i)));
				}
			}
		}
		
		return photo; 
	}
	
}
