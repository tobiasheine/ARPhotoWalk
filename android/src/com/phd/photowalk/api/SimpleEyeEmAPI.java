package com.phd.photowalk.api;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * thx to ligi 
 * https://github.com/eyeem/EyeEmTV/blob/master/src/com/eyeem/tv/SimpleEyeEmAPI.java
 * @author tobi
 *
 */
public class SimpleEyeEmAPI {
	private static String client_id="client_id=QATAfrOjakwFGyoHPTLSmoG8KJAWj6fS";


	public static JSONObject getObjectForURLFrag(String frag) {
		return NetHelper.URLString2JSONObject("https://www.eyeem.com/api/v2/" + frag +"?"+client_id);
	}

	public static JSONObject getPhotoObject(String photo_id) {
		return getObjectForURLFrag("photos/"+photo_id);
	}
	
}