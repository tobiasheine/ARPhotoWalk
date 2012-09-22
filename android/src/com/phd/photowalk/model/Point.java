package com.phd.photowalk.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Point {
	
	private double latitude;
	private double longitude;
	private double altitude;
	
	public Point(double lat, double lon, double alt)
	{
		this.latitude = lat;
		this.longitude = lon;
		this.altitude = alt;
	}
	
	public JSONObject toJSONString() throws JSONException
	{
		JSONObject object = new JSONObject();
		object.put("latitude", this.latitude);
		object.put("longitude", this.longitude);
		object.put("altitude", this.altitude);
		return object;
	}

}
