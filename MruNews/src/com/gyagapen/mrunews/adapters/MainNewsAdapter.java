package com.gyagapen.mrunews.adapters;

import java.util.ArrayList;

import com.gyagapen.mrunews.R;
import com.gyagapen.mrunews.R.animator;
import com.gyagapen.mrunews.R.id;
import com.gyagapen.mrunews.R.layout;
import com.gyagapen.mrunews.entities.News;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class MainNewsAdapter extends ArrayAdapter<News> implements ListAdapter,
		SectionIndexer {

	private ArrayList<News> news = null;
	private Activity activity = null;
	
	private int screenHeight;
	private int screenWidth;

	public MainNewsAdapter(ArrayList<News> news, Activity anActivity) {

		super(anActivity, 0);
		this.news = news;
		activity = anActivity;
		
		// screen size
		DisplayMetrics metrics = new DisplayMetrics();
		anActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		screenHeight = metrics.heightPixels;
		screenWidth = metrics.widthPixels;
	}

	public int getCount() {

		return news.size();
	}

	public News getItem(int position) {

		return news.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		final LayoutInflater inflater = activity.getLayoutInflater();

		final News currentEntry = news.get(position);

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.main_news_entry, parent, false); // initialize
																				// the
																				// layout
																				// from
	
		}
		
		
	

		// News image
		ImageView imageView = (ImageView) convertView
				.findViewById(R.id.headerImageView);
		imageView.setImageResource(currentEntry.getImageRessource());
		
		//News title
		TextView textView = (TextView)convertView.findViewById(R.id.tvArticleTitle);
		textView.setText(currentEntry.getNewsName());

		// add onclick listener
		NewsOnClickListener newsClickListener = new NewsOnClickListener(
				currentEntry);
		convertView.setOnClickListener(newsClickListener);
		
		

		Animation anim = AnimationUtils.loadAnimation(getContext(),
				R.animator.push_right_in);
		anim.setStartOffset(position*100);
		convertView.setAnimation(anim);
		anim.start();

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
