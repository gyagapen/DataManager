package com.gyagapen.mrunews.adapters;

import java.util.ArrayList;

import com.gyagapen.mrunews.ArticleListActivity;
import com.gyagapen.mrunews.entities.NewsSubEntry;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class SubMenuNewsOnClickListener implements OnClickListener {

	private NewsSubEntry newsSubMenu = null;
	private String newsName;
	private String newsCode;

	public SubMenuNewsOnClickListener(NewsSubEntry aNewspaper, String newsName, String newsCode) {
		newsSubMenu = aNewspaper;
		this.newsName = newsName;
		this.newsCode = newsCode;
	}

	/**
	 * Trigerred when clicking on a article
	 */

	public void onClick(View v) {
		
		ArrayList<String> rssFeedList = new ArrayList<String>();
		rssFeedList.add(newsSubMenu.getRssFeed());

		Intent intent = new Intent(v.getContext(), ArticleListActivity.class);
		intent.putExtra("rssFeed", rssFeedList);
		intent.putExtra("rssCode", newsCode);
		intent.putExtra("newsTitle", newsName+newsSubMenu.getSubMenuName());
		v.getContext().startActivity(intent);

	}

}
