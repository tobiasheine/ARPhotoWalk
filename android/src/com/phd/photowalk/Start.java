package com.phd.photowalk;

import android.app.Activity;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.phd.photowalk.api.SimpleEyeEmAPI;

public class Start extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start);
		
		findViewById(R.id.getLastLocation).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final Location loc = ((PHDApplication)getApplication()).getLastKnownLocation();
				if(loc != null){
					new AsyncTask<Void, Void, Void>(){
						protected Void doInBackground(Void[] params) {
							try{
								System.out.println(SimpleEyeEmAPI.getPhotosAroundYou(""+(float)loc.getLatitude(), ""+(float)loc.getLongitude()));			}catch (Exception e) {
							}
							return null;
						};
						
					}.execute();
				}
				else
					Toast.makeText(getApplicationContext(), "No fucking location!!", Toast.LENGTH_LONG).show();
			}
		});
		
//		new AsyncTask<Void, Void, Void>(){
//			protected Void doInBackground(Void[] params) {
//				try{
//					NetHelper.URL2JSONObject(new URL("http://www.eyeem.com/api/v2/albums/radius:52.522044:13.411149/photos&client_id=QATAfrOjakwFGyoHPTLSmoG8KJAWj6fS&detailed=1"));
//					NetHelper.downloadURL2String(new URL("http://www.eyeem.com/api/v2/albums/radius:52.522044:13.411149/photos&client_id=QATAfrOjakwFGyoHPTLSmoG8KJAWj6fS&detailed=1"));
//					}catch (Exception e) {
//				}
//				return null;
//			};
//			
//		}.execute();
		
	}
	
}
