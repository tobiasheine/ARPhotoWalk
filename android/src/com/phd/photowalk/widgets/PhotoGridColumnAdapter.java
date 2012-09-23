package com.phd.photowalk.widgets;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.app.Activity;
import android.content.Intent;
import android.sax.StartElementListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.phd.photowalk.FullscreenPhoto;
import com.phd.photowalk.PHDApplication;
import com.phd.photowalk.model.Photo;

public class PhotoGridColumnAdapter extends BaseAdapter
{
	
	private static String BASE_DOMAIN=("www.eyeem.com");
	private static String PHOTO_PATH = "http://"+BASE_DOMAIN+"/";
	private static String THUMB_BASE = "thumb/";
	
	public ArrayList<Vector<Photo>> list;
	private Activity activity;
	
	public PhotoGridColumnAdapter(Activity activity,ArrayList<Vector<Photo>> list, List<Photo> mPhotos) {
		this.activity = activity;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	
	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		super.notifyDataSetChanged();
	}
	
	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		int height=(int) (((PHDApplication)activity.getApplication()).mScreenDensity*102);
		
		LinearLayout lin=new LinearLayout(activity);
		lin.setOrientation(LinearLayout.HORIZONTAL);
			
		for ( final Photo act_pic : list.get(position)) {

			ImageView img_v=new ImageView(activity);
			img_v.setLayoutParams(new LinearLayout.LayoutParams((int)(act_pic.getAspectRatio()*height),height));
			img_v.setScaleType(ScaleType.CENTER_CROP);
			img_v.setPadding(5, 5, 5, 5);
			getImageLoader().displayImage(getThumbnailPathByHeight(height,act_pic),img_v,ImageLoaderHelper.getOptions());
			
			img_v.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent fullscreen = new Intent(activity,FullscreenPhoto.class);
					fullscreen.putExtra(FullscreenPhoto.EXTRA_FILENAME, act_pic.filename);
					activity.startActivity(fullscreen);
				}
			});
			
			lin.addView(img_v);
		}
		
	   return lin;
	}
	
	public static String getThumbnailPathByHeight(int height,Photo mPhoto) {
		return PHOTO_PATH + THUMB_BASE + "h/"+ height +"/"+ mPhoto.thumbUrl.substring(33);
	}
	
	private ImageLoader img_loader=null;
	
	private ImageLoader getImageLoader() {
		
		img_loader = ((PHDApplication)activity.getApplication()).getImageLoader();
		if (img_loader==null) {
			img_loader = ImageLoader.getInstance();
			img_loader.init(ImageLoaderHelper.getConfiguration(activity));
		}
		((PHDApplication)activity.getApplication()).setImageLoader(img_loader);
		return img_loader;
	}
}
