package com.phd.photowalk.api;

import org.json.JSONObject;

/**
 * thx to ligi 
 * https://github.com/eyeem/EyeEmTV/blob/master/src/com/eyeem/tv/SimpleEyeEmAPI.java
 * @author tobi
 *
 */
public class SimpleEyeEmAPI {
	private static String client_id="client_id=QATAfrOjakwFGyoHPTLSmoG8KJAWj6fS";
	private static String base_url="https://www.eyeem.com/api/v2/";

	public static JSONObject getPhotoObject(String photo_id) {
		return NetHelper.URLString2JSONObject(base_url+"photos/"+photo_id+"?"+client_id);
	}
	
	public static JSONObject getAlbumObject(String album_id) {
		return NetHelper.URLString2JSONObject(base_url+"albums/"+album_id+"?"+client_id);
	}
	
	public static JSONObject searchAlbums(String search) {
		return NetHelper.URLString2JSONObject(base_url+"search?="+search+"&"+client_id);
	}
	
	public static JSONObject getPhotosAroundYou(String lat, String lon) {
		return NetHelper.URLString2JSONObject(base_url+"albums/radius:"+lat+":"+lon+"/photos"+"?"+client_id+"&includeAlbums=1&limit=20");
	}
	
	public static JSONObject getAlbumsAroundYou(String lat, String lon) {
		return NetHelper.URLString2JSONObject(base_url+"albums?geoSearch=nearbyVenues&lat="+lat+"&lng="+lon+"&"+client_id+"&includeAlbums=1&limit=40&radius=0.2");
	}
	
	public static JSONObject getAlbumsPhotos(String albumId) {
		return NetHelper.URLString2JSONObject(base_url+"albums/"+albumId+"/photos?"+client_id+"&limit=100&detailed=1");
	}
	
}