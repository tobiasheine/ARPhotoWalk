package com.phd.photowalk;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.phd.photowalk.widgets.ImageLoaderHelper;

public class FullscreenPhoto extends Activity {
	
	private ImageLoader img_loader=null;
	private PWalkApplication ac;
	public final static String EXTRA_FILENAME = "filename";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photowidget_fullscreen);
		ac = (PWalkApplication) getApplication();
		ImageView photoImageView = (ImageView) findViewById(R.id.fullscreen_image);
		Bundle extras = getIntent().getExtras();
	
	    getImageLoader().displayImage(getThumbnailPathByWidth(ac.mDeviceWidth, extras.getString(EXTRA_FILENAME)), photoImageView,ImageLoaderHelper.getOptions());
	}

	public ImageLoader getImageLoader() {
		img_loader = ((PWalkApplication)getApplicationContext()).getImageLoader();
		if (img_loader==null) {
			img_loader = ImageLoader.getInstance();
			// Initialize ImageLoader with configuration. Do it once.
			img_loader.init(ImageLoaderHelper.getConfiguration(this));
		}
		((PWalkApplication)getApplicationContext()).setImageLoader(img_loader);
		return img_loader;
	}
	
	public void bindImageView(ImageView view,String url) {
		getImageLoader().displayImage(url,view,ImageLoaderHelper.getOptions());
	}
	
	public static String getThumbnailPathByWidth(int width,String filename) {
		return "http://www.eyeem.com/thumb/" + "w/" + width +"/"+ filename;
	}
	
	
}
