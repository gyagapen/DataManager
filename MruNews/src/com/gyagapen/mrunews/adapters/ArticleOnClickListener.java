package com.gyagapen.mrunews.adapters;

import com.gyagapen.mrunews.ArticleViewActivity;
import com.gyagapen.mrunews.entities.ArticleHeader;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class ArticleOnClickListener implements OnClickListener {

	ArticleHeader article = null;
	String newsName;

	public ArticleOnClickListener(ArticleHeader anArticle, String newsName) {
		article = anArticle;
		this.newsName = newsName;
	}

	
	/**
	 * Trigerred when clicking on a article
	 */

	public void onClick(View v) {
				
		Intent intent = new Intent(v.getContext(), ArticleViewActivity.class);
		intent.putExtra("ArticleLink",article.getLink());
		intent.putExtra("ArticleId",article.getId());
		intent.putExtra("NewsName",newsName);
		
		v.getContext().startActivity(intent);
		

	}

}

