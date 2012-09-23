package com.phd.photowalk.widgets;

import com.phd.photowalk.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ActionBar extends RelativeLayout {

	private View actionBarView;
	private ProgressBar mProgress;
	private TextView title;
	
	
	public ActionBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.actionBarView = inflater.inflate(R.layout.actionbar, null);
		this.mProgress = (ProgressBar) actionBarView.findViewById(R.id.actionbar_progress);
		this.title = (TextView) actionBarView.findViewById(R.id.title);
		
		addView(actionBarView);
	}
	
	public void setBusy(boolean isBusy){
		if(isBusy)
			this.mProgress.setVisibility(View.VISIBLE);
		else
			this.mProgress.setVisibility(View.INVISIBLE);
	}
	
	public void setTitle(String title){
		this.title.setText(title);
	}
}
