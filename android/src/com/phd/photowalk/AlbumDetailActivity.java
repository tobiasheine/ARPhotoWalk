package com.phd.photowalk;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import com.phd.photowalk.api.SimpleEyeEmAPI;
import com.phd.photowalk.model.Photo;
import com.phd.photowalk.widgets.ActionBar;
import com.phd.photowalk.widgets.PhotoGridColumnAdapter;

public class AlbumDetailActivity extends Activity {
	
	public static String EXTA_ALBUM_ID="album_id";
	private Photo2ColumnSplitter splitter;
	private String albumId;
	private PhotoGridColumnAdapter adapter;
	private ListView list;
	private PHDApplication app;
	private ActionBar actionBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (PHDApplication) getApplication();
		setContentView(R.layout.album_detail);
		list = (ListView) findViewById(R.id.photolist);
		
		actionBar = (ActionBar) findViewById(R.id.actionbar);
		actionBar.setTitle("What's going on here?!");
		
		
		albumId = getIntent().getStringExtra(EXTA_ALBUM_ID);
		splitter = new Photo2ColumnSplitter((PHDApplication) getApplication());
		
		new LoadAlbumPhotosTask().execute(albumId);
	}
	
	
	private class LoadAlbumPhotosTask extends AsyncTask<String, Void, List<Photo>>{
		@Override
		protected void onPreExecute() {
			actionBar.setBusy(true);
		}
		
		@Override
		protected List<Photo> doInBackground(String... params) {
			List<Photo> photoList = new ArrayList<Photo>();
			
			try{
				JSONObject photoObject = SimpleEyeEmAPI.getAlbumsPhotos(params[0]);
				photoObject = photoObject.getJSONObject("photos");
				
				JSONArray photos = null; 
				photos = photoObject.getJSONArray("items");
				for (int i = 0; i < photos.length(); i++) {
					photoList.add(Photo.parsePhoto(photos.getJSONObject(i)));
				}
				
			}catch (Exception e) {
				// TODO: handle exception
			}
			
			return photoList;
		}
		
		protected void onPostExecute(List<Photo> result) {
			
			splitter.setPhotos(result);
			splitter.split();
			adapter = new PhotoGridColumnAdapter(AlbumDetailActivity.this, splitter.getList(), result);
			list.setAdapter(adapter);
			adapter.notifyDataSetChanged();

			
			actionBar.setBusy(false);
		};
	}

}
