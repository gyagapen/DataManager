package com.gyagapen.mrunews;

import java.util.ArrayList;

import com.gyagapen.mrunews.R;
import com.gyagapen.mrunews.R.animator;
import com.gyagapen.mrunews.R.id;
import com.gyagapen.mrunews.R.layout;
import com.gyagapen.mrunews.adapters.SubMenuAdapter;
import com.gyagapen.mrunews.entities.NewsSubEntry;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;

public class NewsSubMenuActivity extends Activity{

	private ArrayList<String> rssFeed;
	private ArrayList<String> rssName;
	private String rssId;
	private ArrayList<NewsSubEntry> subMenuList = null;

	private Animation anim;

	private ListView subMenuListView;
	
	private String newsTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_submenu);

		// getting info from intent
		Intent myIntent = getIntent();
		subMenuList = (ArrayList<NewsSubEntry>)getIntent().getSerializableExtra("rssFeedList");
		rssId = myIntent.getStringExtra("rssCode");
		newsTitle = myIntent.getStringExtra("newsTitle"); 
		
		
		//set activity title
		setTitle(newsTitle);
		
		initUIComponent();

		populateSubMenuList();
		
	}

	public void initUIComponent() {
		subMenuListView = (ListView) findViewById(R.id.lv_submenu);
	}

	public void populateSubMenuList() {
		SubMenuAdapter subMenuAdapter = new SubMenuAdapter(
				subMenuList, this, newsTitle, rssId);
		subMenuListView.setAdapter(subMenuAdapter);

		// scroll bar
		subMenuListView.setFastScrollEnabled(true);
		subMenuListView.setScrollbarFadingEnabled(false);
		subMenuListView.setScrollContainer(true);
		subMenuListView.setTextFilterEnabled(true);
		
		// animation
		anim = AnimationUtils.loadAnimation(getApplicationContext(),
				R.animator.push_right_in);
		subMenuListView.setAnimation(anim);
		anim.start();

	}

	
	public void finish() {
	    super.finish();
	    //transition animation
	    overridePendingTransition(R.animator.push_right_in, R.animator.push_right_out);
	}

}
