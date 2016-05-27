package com.bluetooth;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;

public class SetActionBars {
	public Context context;
	public String myActionBar_name;
	public SetActionBars() {
		// TODO Auto-generated constructor stub
	}
	
	public SetActionBars(Context context, String myActionBar_name) {
		super();
		this.context = context;
		this.myActionBar_name = myActionBar_name;
	}


	public void setActionBars() {
		((Activity) context).getActionBar().setTitle(myActionBar_name);
		int options = ActionBar.DISPLAY_SHOW_TITLE;
		int mask = ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE
				| ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_SHOW_CUSTOM;

		((Activity) context).getActionBar().setDisplayOptions(options, mask);
		((Activity) context).getActionBar().setDisplayHomeAsUpEnabled(true);
	}
}
