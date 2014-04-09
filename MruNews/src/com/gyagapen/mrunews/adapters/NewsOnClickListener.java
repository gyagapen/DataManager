package com.gyagapen.mrunews.adapters;

import java.io.Serializable;

import com.gyagapen.mrunews.ArticleListActivity;
import com.gyagapen.mrunews.NewsSubMenuActivity;
import com.gyagapen.mrunews.entities.News;

import android.content.Intent;
import android.os.Parcelable;
import android.view.View;
import android.view.View.OnClickListener;

public class NewsOnClickListener implements OnClickListener {

	private News newspaper = null;

	public NewsOnClickListener(News aNewspaper) {
		newspaper = aNewspaper;
	}

	
	/**
	 * Trigerred when clicking on a article
	 */

	public void onClick(View v) {
		
		if (newspaper.getSubEntries().size() == 0)
		{
			Intent intent = new Intent(v.getContext(), ArticleListActivity.class);
			intent.putExtra("rssFeed", newspaper.getNewsRssFeed());
			intent.putExtra("rssCode", newspaper.getNewsId());
			intent.putExtra("newsTitle", newspaper.getNewsName());
			v.getContext().startActivity(intent);
		}
		else
		{
			
			Intent intent = new Intent(v.getContext(), NewsSubMenuActivity.class);
			intent.putExtra("rssFeedList", (Serializable)newspaper.getSubEntries());
			intent.putExtra("rssCode", newspaper.getNewsId());
			intent.putExtra("newsTitle", newspaper.getNewsName());
			v.getContext().startActivity(intent);
			
		}
	}

}
