package com.phd.photowalk.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * thx to ligi 
 * https://github.com/eyeem/EyeEmTV/blob/master/src/com/eyeem/tv/NetHelper.java
 * 
 * @author tobi
 *
 */
public class NetHelper {

	
	public static String downloadURL2String(URL url) {

		URLConnection con;
		InputStream in = null;
		try {
			con = url.openConnection();
			in = con.getInputStream();
		} catch (IOException e2) {
			e2.printStackTrace();
		}

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
		} catch (Exception e3) {
			e3.printStackTrace();
			return null;
		}
		StringBuilder sb = new StringBuilder();
		String line = null;

		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e3) {
		}

		return sb.toString();

	}


	public static JSONObject URL2JSONObject(URL url) {
		String json_string=NetHelper.downloadURL2String(url);

		if (json_string==null)
			return null;

		try {
			return new JSONObject(json_string);
		} catch (JSONException e) {
			return null;
		}
	}
	public static JSONObject URLString2JSONObject(String url_s) {
		URL url=null;
		try {
			url = new URL(url_s);
		} catch (MalformedURLException e2) { 
			return null;
		}
		return URL2JSONObject(url);
	}
}