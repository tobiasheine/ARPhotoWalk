package com.phd.photowalk;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.phd.photowalk.model.Album;
import com.wikitude.architect.ArchitectUrlListener;
import com.wikitude.architect.ArchitectView;

public class ARActivity extends Activity implements ArchitectUrlListener, IFLocationUpdate{

	private String apiKey = "nKc9NtnrpQJ/aUtTDoy50t5ndV9zjuslj3B8xcHgvKoBAl6n/e2UznDNPlyBCFxKr2ffCR01ucmHm+xLEAJj0FQdT0u8Jr5rKKobj7gBNl/xCrlBWRXUfdiCctXwQBWTKJuuUmErhvqCgp3QWqDUeDHnQKIpc+yDBNko6qp78A5TYWx0ZWRfX+ZSlAflWlVhPNJvdcOBPOJNtNC0TVO8WFAmrJsgkAdu4diK0QVd15faK/HrbJFtIXyrB3KFGc7sbBvfPwDtHVBkSfzCZSsY1Sdb3lZ02UphzYrFNiblTjsUz96Ss6IfxwgExhw7nyZ2MFJYWZ824STK3Tb2PxrhWsrRqq4fcCM7fjd6XRYMvTbOn6pCq/0aBh9009S9+76O1NIGAsq+kEUguMRM+TVpc+QZeTQS82OLxEJY7DrmQQ2OHEKuZVu7/xGix3V3VFeL4//zWC+WMXDTbRWoJyfwfn8xkU5p3CiMbxOdHIZZw5AWFmVeCu8SUN6DEnymMX+PlHxR2h229hT5Q/VtQTdN4kEEKE+d82I9bNUM1dFBy9uyNGXuaNjQfZaHonj5eVG53pvq0JJIlE9X2Rr/BEHglhmvpRINSZnCHYNyBxkCaVnI5uZ7K5mJOMhBKRkUDW72lNDhrLM6l2LdQyw0AN4wxfyLoXeRrRW5JWL7GYCciTgaDOOmVSVSgTKvQ//jPBh7";
	private ArchitectView architectView;
	private PHDApplication app;
	public static final String EXTRA_CATEGORY = "category";
	private int category;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		app = (PHDApplication) getApplication();
		
		this.getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );
        
        //check if the device fulfills the SDK'S minimum requirements
        if(!ArchitectView.isDeviceSupported(this))
        {
        	Toast.makeText(this, "minimum requirements not fulfilled", Toast.LENGTH_LONG).show();
        	this.finish();
        	return;
        }
        setContentView(R.layout.ar_activity);
        
        category = getIntent().getIntExtra(ARActivity.EXTRA_CATEGORY, -1);
        
        //set the devices' volume control to music to be able to change the volume of possible soundfiles to play
        this.setVolumeControlStream( AudioManager.STREAM_MUSIC );
        this.architectView = (ArchitectView) this.findViewById(R.id.architectView);
        //onCreate method for setting the license key for the SDK
        architectView.onCreate(apiKey);
        
        ((PHDApplication)getApplication()).setLocationUpdater(this);
	}

	@Override
    protected void onPostCreate(Bundle savedInstanceState) {
    	super.onPostCreate(savedInstanceState);
    	
    	//IMPORTANT: creates ARchitect core modules
    	if(this.architectView != null)
    		this.architectView.onPostCreate();
    	
    	//register this activity as handler of "architectsdk://" urls
    	this.architectView.registerUrlListener(this);
    	
    	if(app.albumList != null){
    		try {
				loadWorld();
			} catch (Exception e) {
				e.printStackTrace();
				Toast.makeText(this, "Could not load world data", Toast.LENGTH_LONG).show();
			}
    	}
    }
	
	@Override
	protected void onResume() {
		super.onResume();

		this.architectView.onResume();
		if(((PHDApplication)getApplication()).getLastKnownLocation()!=null)
			this.architectView.setLocation(((PHDApplication)getApplication()).getLastKnownLocation().getLatitude(), ((PHDApplication)getApplication()).getLastKnownLocation().getLongitude(), ((PHDApplication)getApplication()).getLastKnownLocation().getAltitude(),1f);

	}
    @Override
    protected void onPause() {
    	super.onPause();
    	if(this.architectView != null)
    		this.architectView.onPause();
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	
    	if(this.architectView != null)
    		this.architectView.onDestroy();
    }
    
    @Override
    public void onLowMemory() {
    	super.onLowMemory();
    	
    	if(this.architectView != null)
    		this.architectView.onLowMemory();
    }

    /**
     * <p>
     * interface method of {@link ArchitectUrlListener} class
     * called when an url with host "architectsdk://" is discovered
     * 
     * can be parsed and allows to react to events triggered in the ARchitect World
     * </p>
     */
	@Override
	public boolean urlWasInvoked(String url) {
		//parsing the retrieved url string
		List<NameValuePair> queryParams = URLEncodedUtils.parse(URI.create(url), "UTF-8");
		
		String id = "";
		// getting the values of the contained GET-parameters
		for(NameValuePair pair : queryParams)
		{
			if(pair.getName().equals("id"))
			{
				id = pair.getValue();
			}
		}
		
		Intent intent = new Intent(this, AlbumDetailActivity.class);
		intent.putExtra(AlbumDetailActivity.EXTA_ALBUM_ID, id);
		this.startActivity(intent);
		return true;
	}
	
	private void loadWorld() throws IOException, JSONException {
		this.architectView.load("tutorial1.html");
		JSONArray poiArray = new JSONArray();
		
		int i = 0;
		for(Album a : app.albumList){
			
			if(a.point !=null && a.type.equals("venue")){
				a.index = i;
				poiArray.put(a.toJSONObject());
				i++;
			}
		}
		
		architectView.callJavascript("setCategory(" + category + ");");
		architectView.callJavascript("newData(" + poiArray.toString() + ");");
		
	}
	
	@Override
	public void sendLocation(float lat, float lon, float accuracy) {
		if(this.architectView != null){
			this.architectView.setLocation(lat, lon, accuracy);
		}
	}
}
