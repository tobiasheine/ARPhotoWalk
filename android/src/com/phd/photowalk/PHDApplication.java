package com.phd.photowalk;

import com.nostra13.universalimageloader.core.ImageLoader;

import android.app.Application;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class PHDApplication extends Application implements LocationListener{

	private LocationManager locManager;
	private Location loc;
	private IFLocationUpdate locUpdate=null;
	public float mScreenDensity;
	public int mDeviceWidth;
	private ImageLoader mLoader = null;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		mScreenDensity = getResources().getDisplayMetrics().density;
		mDeviceWidth = getResources().getDisplayMetrics().widthPixels;
		
		locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        locManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, this);
        locManager.requestLocationUpdates( LocationManager.NETWORK_PROVIDER, 0, 0, this);
	}
	
	public void setLocationUpdater(IFLocationUpdate updater){
		locUpdate = updater;
	}
	
	
	public Location getLastKnownLocation(){
		return loc;
	}
	
	@Override
	public void onLocationChanged(Location location) {
		loc = location;
		
		if(locUpdate != null){
			locUpdate.sendLocation((float)loc.getLatitude(), (float)loc.getLongitude(), (float)loc.getAccuracy());
		}
	}

	@Override
	public void onProviderDisabled(String provider) {
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		
	}
	
	public ImageLoader getImageLoader(){
		return mLoader;
	}
	public void setImageLoader(ImageLoader img_loader){
		this.mLoader = img_loader;
	}

}
