package com.gyagapen.mrunews.adapters;

import java.util.ArrayList;

import com.gyagapen.mrunews.R;
import com.gyagapen.mrunews.R.id;
import com.gyagapen.mrunews.R.layout;
import com.gyagapen.mrunews.entities.NewsSubEntry;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.SectionIndexer;
import android.widget.TableRow;
import android.widget.TextView;


public class SubMenuAdapter  extends ArrayAdapter<NewsSubEntry> implements ListAdapter, SectionIndexer	{

	private ArrayList<NewsSubEntry> subMenus = null;
	private  Activity activity = null;
	private String newsName;
	private String newsCode;
	

	public SubMenuAdapter(ArrayList<NewsSubEntry> someMenus, Activity anActivity, String newsName, String newsCode) {

		super(anActivity, 0);
		subMenus = someMenus;
		activity = anActivity;
		this.newsName = newsName;
		this.newsCode = newsCode;
	}

	public int getCount() {

		return subMenus.size();
	}


	public NewsSubEntry getItem(int position) {

		return subMenus.get(position);
	}


	public long getItemId(int position) {
		return position;
	}


	public View getView(int position, View convertView, ViewGroup parent) {

		final LayoutInflater inflater = activity.getLayoutInflater();
		
		
		final NewsSubEntry currentEntry = subMenus.get(position);
		
		if( convertView == null ){
	        //We must create a View:
	        convertView = inflater.inflate(R.layout.sub_menu_entry, parent, false);    // initialize the layout from xml
	    }
		
		
		
		//Title
		final TextView subMenuName = (TextView) convertView.findViewById(R.id.tvSubMenuEntry);		
		subMenuName.setText(currentEntry.getSubMenuName());
		
	
		//add onclick listener
		final TableRow table = (TableRow)convertView.findViewById(R.id.tableSubMenu); 
		SubMenuNewsOnClickListener subMenuClickListener = new SubMenuNewsOnClickListener(currentEntry, newsName, newsCode);
		//table.setOnClickListener(subMenuClickListener);
		
		

		return convertView;

	}
	


	@Override
	public int getPositionForSection(int section) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSectionForPosition(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object[] getSections() {
		// TODO Auto-generated method stub
		return null;
	}





}
