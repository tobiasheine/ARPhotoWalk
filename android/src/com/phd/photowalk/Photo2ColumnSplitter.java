package com.phd.photowalk;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.phd.photowalk.model.Photo;


public class Photo2ColumnSplitter {

	private ArrayList<Vector<Photo>> list;
	private List<Photo> mPhotos;
	private int mRemainingPixels;
	private PHDApplication ac;
	private int max_pics_in_row=0;
	
	public Photo2ColumnSplitter(PHDApplication ac) {
		this.ac=ac;
		mRemainingPixels = ac.mDeviceWidth;
		list = new ArrayList<Vector<Photo>>();		
	}
	
	/**
	 * separates the photos into vectors of MAX 3, and preps them for the
	 * adapter
	 */
	@SuppressWarnings("unchecked") 
	public void splitPictures(List<Photo> photos2add) {

		Vector<Photo> temp;
		if ((list.size()>0 && list.get(list.size()-1).size()<4)){
			temp = list.get(list.size()-1);
			list.remove(list.size()-1);
		}else{
			temp = new Vector<Photo>();
		}
		for(int i=0;i<photos2add.size();i++) {
			Photo act_pic=photos2add.get(i);
			int img_width=(int)((act_pic.getAspectRatio()*(94*ac.mScreenDensity))+8*ac.mScreenDensity);
			mRemainingPixels -= img_width;
			if(mRemainingPixels<0 && temp.size()>0){
				
				list.add((Vector<Photo>) temp.clone());
				
				if (temp.size()>max_pics_in_row)
					max_pics_in_row=temp.size();
				
				temp = new Vector<Photo>();
				mRemainingPixels = ac.mDeviceWidth-img_width;
			}	
			temp.add(act_pic);
		}
		if(temp.size()>0){
			list.add((Vector<Photo>) temp.clone());
		}
	}
	
	public void split() {
		splitPictures(mPhotos);
	}
	
	public void refresh() {
		list=null;
		mRemainingPixels = ac.mDeviceWidth;	
	}
	
	public ArrayList<Vector<Photo>> getList() {
		return list;
	}
	
	public List<Photo> getPhotos() {
		return mPhotos;
	}
	
	public void setPhotos(List<Photo> photos) {
		this.mPhotos=photos;
	}

	public void reset(){
		if(list != null && mPhotos != null){
			list.clear();
			mPhotos.clear();
		}
		max_pics_in_row = 0;
	}
}