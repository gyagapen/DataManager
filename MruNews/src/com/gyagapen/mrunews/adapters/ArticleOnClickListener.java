package com.gyagapen.mrunews.adapters;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.Card.OnCardClickListener;

import com.gyagapen.mrunews.ArticleViewActivity;
import com.gyagapen.mrunews.entities.ArticleHeader;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;

public class ArticleOnClickListener implements OnCardClickListener {

	ArticleHeader article = null;
	String newsName;

	public ArticleOnClickListener(ArticleHeader anArticle, String newsName) {
		article = anArticle;
		this.newsName = newsName;
	}

	

	@Override
	public void onClick(Card card, View view) {
		
		Intent intent = new Intent(view.getContext(), ArticleViewActivity.class);
		intent.putExtra("ArticleLink",article.getLink());
		intent.putExtra("ArticleId",article.getId());
		intent.putExtra("NewsName",newsName);
		
		view.getContext().startActivity(intent);
		
	}

}

