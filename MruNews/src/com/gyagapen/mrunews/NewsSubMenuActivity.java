package com.gyagapen.mrunews;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.view.CardListView;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;

import com.gyagapen.mrunews.adapters.ArticleOnClickListener;
import com.gyagapen.mrunews.adapters.SubMenuAdapter;
import com.gyagapen.mrunews.adapters.SubMenuNewsOnClickListener;
import com.gyagapen.mrunews.common.CustomCardThumbails;
import com.gyagapen.mrunews.common.NewsCard;
import com.gyagapen.mrunews.entities.NewsSubEntry;
import com.gyagapen.mrunews.parser.GetImageLinkAsync;
import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;
import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;

public class NewsSubMenuActivity extends Activity{

	private ArrayList<String> rssFeed;
	private ArrayList<String> rssName;
	private String rssId;
	private ArrayList<NewsSubEntry> subMenuList = null;

	private Animation anim;

	private CardListView subMenuListView;
	
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
		subMenuListView = (CardListView) findViewById(R.id.lv_submenu);
	}

	public void populateSubMenuList() {
//		SubMenuAdapter subMenuAdapter = new SubMenuAdapter(
//				subMenuList, this, newsTitle, rssId);
//		subMenuListView.setAdapter(subMenuAdapter);
//
//		// scroll bar
//		subMenuListView.setFastScrollEnabled(true);
//		subMenuListView.setScrollbarFadingEnabled(false);
//		subMenuListView.setScrollContainer(true);
//		subMenuListView.setTextFilterEnabled(true);
		
		ArrayList<Card> cards = new ArrayList<Card>();
		CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(this, cards);

		for (int i = 0; i < subMenuList.size(); i++) {
			// Create a Card
			Card card = new Card(this, R.layout.submenu_row_card);
			card.setTitle(subMenuList.get(i).getSubMenuName());

			CardThumbnail cardThumbnail = new CardThumbnail(this);
			cardThumbnail.setDrawableResource(subMenuList.get(i).getImageResource());
			card.addCardThumbnail(cardThumbnail);

			SubMenuNewsOnClickListener subMenuClickListener = new SubMenuNewsOnClickListener(subMenuList.get(i), newsTitle, rssId);
			card.setOnClickListener(subMenuClickListener);

			cards.add(card);
		}

		AnimationAdapter animCardArrayAdapter = new SwingBottomInAnimationAdapter(
				mCardArrayAdapter);
		animCardArrayAdapter.setAbsListView(subMenuListView);
		
		subMenuListView.setExternalAdapter(animCardArrayAdapter,
				mCardArrayAdapter);
		
		// animation
		anim = AnimationUtils.loadAnimation(getApplicationContext(),
				R.animator.fade_in);
		subMenuListView.setAnimation(anim);
		anim.start();

	}

	
	public void finish() {
	    super.finish();
	    //transition animation
	    overridePendingTransition(R.animator.push_right_in, R.animator.push_right_out);
	}

}
