package com.gyagapen.mrunews.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.opengl.Visibility;
import android.preference.PreferenceManager.OnActivityDestroyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.gyagapen.mrunews.R;
import com.gyagapen.mrunews.R.id;
import com.gyagapen.mrunews.R.layout;
import com.gyagapen.mrunews.entities.ArticleHeader;
import com.gyagapen.mrunews.entities.SemdexEntity;
import com.gyagapen.mrunews.parser.GetImageAsync;
import com.handmark.pulltorefresh.library.PullToRefreshListView;


public class SemdexListAdapter  extends ArrayAdapter<SemdexEntity> implements ListAdapter, SectionIndexer	{

	private ArrayList<SemdexEntity> semdexEntities = null;
	private  Activity activity = null;

	private static String sections = "-abcdefghilmnopqrstuvz";

	public SemdexListAdapter(ArrayList<SemdexEntity> someEntities, Activity anActivity) {

		super(anActivity,0);
		semdexEntities = someEntities;
		activity = anActivity;

	}

	public int getCount() {

		return semdexEntities.size();
	}


	public SemdexEntity getItem(int position) {

		return semdexEntities.get(position);
	}


	public long getItemId(int position) {
		return position;
	}


	public View getView(int position, View convertView, ViewGroup parent) {

		final LayoutInflater inflater = activity.getLayoutInflater();

		
		final SemdexEntity currentEntry = semdexEntities.get(position);
		
		if( convertView == null ){
	        //We must create a View:
	        convertView = inflater.inflate(R.layout.semdex_list_entry, parent, false);    // initialize the layout from xml
	    }
		
		
		//Title
		final TextView tvSemName = (TextView) convertView.findViewById(R.id.tvSemdexName);		
		tvSemName.setText(currentEntry.getName());
		
		final TextView tvSemNominal = (TextView) convertView.findViewById(R.id.tvSemdexNominal);		
		tvSemNominal.setText(currentEntry.getNominal());
		
		final TextView semdexLCPrice = (TextView) convertView.findViewById(R.id.tvSemdexLastClosingPrice);		
		semdexLCPrice.setText(currentEntry.getLastClosingPrice());
		
		final TextView semdexLatestPrice = (TextView) convertView.findViewById(R.id.tvSemdexLatest);		
		semdexLatestPrice.setText(currentEntry.getLatestPrice());
		
		final TextView semdexChangePerc = (TextView) convertView.findViewById(R.id.tvSemdexChangePerc);		
		semdexChangePerc.setText(currentEntry.getChangePercentage());

		return convertView;

	}


	public int getPositionForSection(int section) {

		for (int i=0; i < this.getCount(); i++) {
			String item = this.getItem(i).getName().toLowerCase();
			if (item.charAt(0) == sections.charAt(section))
				return i;
		}
		return 0;
	}


	public int getSectionForPosition(int arg0) {

		return 0;
	}


	public Object[] getSections() {

		String[] sectionsArr = new String[sections.length()];
		for (int i=0; i < sections.length(); i++)
			sectionsArr[i] = "" + sections.charAt(i);

		return sectionsArr;
	}



	
	



}
