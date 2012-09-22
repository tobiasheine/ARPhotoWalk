package com.phd.photowalk;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.phd.photowalk.api.NetHelper;
import com.wikitude.architect.ArchitectUrlListener;
import com.wikitude.architect.ArchitectView;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.WindowManager;
import android.widget.Toast;

public class MainActivity extends Activity implements ArchitectUrlListener, IFLocationUpdate{

private static final String TAG = MainActivity.class.getSimpleName();
	
	private final static float  TEST_LATITUDE =  47.77318f;
	private final static float  TEST_LONGITUDE = 13.069730f;
	private final static float 	TEST_ALTITUDE = 150;
	
	private String apiKey = "nKc9NtnrpQJ/aUtTDoy50t5ndV9zjuslj3B8xcHgvKoBAl6n/e2UznDNPlyBCFxKr2ffCR01ucmHm+xLEAJj0FQdT0u8Jr5rKKobj7gBNl/xCrlBWRXUfdiCctXwQBWTKJuuUmErhvqCgp3QWqDUeDHnQKIpc+yDBNko6qp78A5TYWx0ZWRfX+ZSlAflWlVhPNJvdcOBPOJNtNC0TVO8WFAmrJsgkAdu4diK0QVd15faK/HrbJFtIXyrB3KFGc7sbBvfPwDtHVBkSfzCZSsY1Sdb3lZ02UphzYrFNiblTjsUz96Ss6IfxwgExhw7nyZ2MFJYWZ824STK3Tb2PxrhWsrRqq4fcCM7fjd6XRYMvTbOn6pCq/0aBh9009S9+76O1NIGAsq+kEUguMRM+TVpc+QZeTQS82OLxEJY7DrmQQ2OHEKuZVu7/xGix3V3VFeL4//zWC+WMXDTbRWoJyfwfn8xkU5p3CiMbxOdHIZZw5AWFmVeCu8SUN6DEnymMX+PlHxR2h229hT5Q/VtQTdN4kEEKE+d82I9bNUM1dFBy9uyNGXuaNjQfZaHonj5eVG53pvq0JJIlE9X2Rr/BEHglhmvpRINSZnCHYNyBxkCaVnI5uZ7K5mJOMhBKRkUDW72lNDhrLM6l2LdQyw0AN4wxfyLoXeRrRW5JWL7GYCciTgaDOOmVSVSgTKvQ//jPBh7";
	
	
	private ArchitectView architectView;
	private List<PoiBean> poiBeanList;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //let the application be fullscreen
        this.getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );
        
        //check if the device fulfills the SDK'S minimum requirements
        if(!ArchitectView.isDeviceSupported(this))
        {
        	Toast.makeText(this, "minimum requirements not fulfilled", Toast.LENGTH_LONG).show();
        	this.finish();
        	return;
        }
        setContentView(R.layout.activity_main);
       
        //set the devices' volume control to music to be able to change the volume of possible soundfiles to play
        this.setVolumeControlStream( AudioManager.STREAM_MUSIC );
        this.architectView = (ArchitectView) this.findViewById(R.id.architectView);
        //onCreate method for setting the license key for the SDK
        architectView.onCreate(apiKey);
        
        //in order to inform the ARchitect framework about the user's location Androids LocationManager is used in this case
        //NOT USED IN THIS EXAMPLE
        //locManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        //locManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, this);
        
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
    	
//    	try {
//			loadSampleWorld();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

    }
    
	@Override
	protected void onResume() {
		super.onResume();

		this.architectView.onResume();
		this.architectView.setLocation(TEST_LATITUDE, TEST_LONGITUDE, TEST_ALTITUDE,1f);

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
		
		//get the corresponding poi bean for the given id
		PoiBean bean = poiBeanList.get(Integer.parseInt(id));
		//start a new intent for displaying the content of the bean
		Intent intent = new Intent(this, PoiDetailActivity.class);
		intent.putExtra("POI_NAME", bean.getName());
		intent.putExtra("POI_DESC", bean.getDescription());
		this.startActivity(intent);
		return true;
	}
	
	/**
	 * method for creating random locations in the vicinity of the user
	 * @return array with lat and lon values as doubles
	 */
	private double[] createRandLocation() {
		 
		return new double[]{ TEST_LATITUDE + ((Math.random() - 0.5) / 500), TEST_LONGITUDE + ((Math.random() - 0.5) / 500),  TEST_ALTITUDE + ((Math.random() - 0.5) * 10)};
	}

	/**
	 * loads a sample architect world and
	 * creates a definable amount of pois in beancontainers 
	 * and converts them into a jsonstring that can be sent to the framework
	 * @throws IOException exception thrown while loading an Architect world
	 */
	private void loadSampleWorld() throws IOException {
		this.architectView.load("tutorial1.html");

		poiBeanList = new ArrayList<PoiBean>();
		
		final String urlString = "http://www.eyeem.com/api/v2/search?q=berlin&client_id=QATAfrOjakwFGyoHPTLSmoG8KJAWj6fS&includeAlbums=1&limit=10";
		new Thread(new Runnable(){
			@Override
			public void run() {
				try {
					JSONArray poiArray = new JSONArray();
					JSONObject jsonObject = NetHelper.URL2JSONObject(new URL(urlString));
					JSONObject albums = jsonObject.getJSONObject("albums");
					JSONArray items = albums.getJSONArray("items");

					for(int i=0; i<10; i++) {
						JSONObject album = items.getJSONObject(i);
						JSONObject location = album.optJSONObject("location");
						
						if(location!=null) {
							String lat = (String) location.get("latitude");
							String lon = (String) location.get("longitude");
							
							PoiBean bean = new PoiBean(""+i, "POI #"+i, "Probably one of the best POIs you have ever seen. This is the description of Poi #" +i, (int) (Math.random() * 3), Double.parseDouble(lat), Double.parseDouble(lon), TEST_ALTITUDE + ((Math.random() - 0.5) * 10));
							poiArray.put(bean.toJSONObject());
							
							poiBeanList.add(bean);
						}
					}
						
					architectView.callJavascript("newData(" + poiArray.toString() + ");");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * listener method called when the location of the user has changed
	 * used for informing the ArchitectView about a new location of the user
	 */

	@Override
	public void sendLocation(float lat, float lon, float accuracy) {
		// TODO Auto-generated method stub
		if(this.architectView != null){
//			this.architectView.setLocation(lat, lon, accuracy);
			((PHDApplication)getApplication()).setLocationUpdater(null);
			try {
				loadSampleWorld();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			
	}
	
}