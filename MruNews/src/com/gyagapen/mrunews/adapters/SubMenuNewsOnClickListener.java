package com.gyagapen.mrunews.adapters;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.Card.OnCardClickListener;

import java.util.ArrayList;

import com.gyagapen.mrunews.ArticleListActivity;
import com.gyagapen.mrunews.entities.NewsSubEntry;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class SubMenuNewsOnClickListener implements OnCardClickListener {

	private NewsSubEntry newsSubMenu = null;
	private String newsName;
	private String newsCode;

	public SubMenuNewsOnClickListener(NewsSubEntry aNewspaper, String newsName, String newsCode) {
		newsSubMenu = aNewspaper;
		this.newsName = newsName;
		this.newsCode = newsCode;
	}


	public void onClick(Card card, View view) {
		

		ArrayList<String> rssFeedList = new ArrayList<String>();
		rssFeedList.add(newsSubMenu.getRssFeed());

		Intent intent = new Intent(view.getContext(), ArticleListActivity.class);
		intent.putExtra("rssFeed", rssFeedList);
		intent.putExtra("rssCode", newsCode);
		intent.putExtra("newsTitle", newsName+newsSubMenu.getSubMenuName());
		intent.putExtra("isNotRssFeed", newsSubMenu.isNotRssFeed());
		intent.putExtra("notRssParseCode", newsSubMenu.getNotRssParseCode());
		view.getContext().startActivity(intent);
		
	}

}
