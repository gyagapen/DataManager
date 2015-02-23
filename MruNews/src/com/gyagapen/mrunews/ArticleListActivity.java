package com.gyagapen.mrunews;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.view.CardListView;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.gyagapen.mrunews.adapters.ArticleOnClickListener;
import com.gyagapen.mrunews.adapters.ListArticleAdapter;
import com.gyagapen.mrunews.common.CustomCardThumbails;
import com.gyagapen.mrunews.common.LogsProvider;
import com.gyagapen.mrunews.common.NewsCard;
import com.gyagapen.mrunews.entities.ArticleHeader;
import com.gyagapen.mrunews.parser.GetImageLinkAsync;
import com.gyagapen.mrunews.parser.HTMLPageParser;
import com.gyagapen.mrunews.parser.RSSReader;
import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;
import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;

@SuppressLint("NewApi")
public class ArticleListActivity extends Activity implements Runnable {

	private ArrayList<String> rssFeed;
	private String rssId;
	private ArrayList<ArticleHeader> articleList = null;
	private boolean isRefreshed = false;
	private LogsProvider logsProvider = null;
	private ArrayList<GetImageLinkAsync> imageAsyncTasks = null;
	private boolean isNotRssFeed;
	private String notRssParseCode;

	// waiting dialog
	private ProgressDialog progressDialog;

	private Animation anim;

	private Handler mHandler = null;

	// private PullToRefreshListView HeaderListView;
	private CardListView HeaderListView;

	String newsTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.article_list);

		logsProvider = new LogsProvider(null, this.getClass());

		// getting info from intent
		Intent myIntent = getIntent();
		rssFeed = myIntent.getStringArrayListExtra("rssFeed");
		rssId = myIntent.getStringExtra("rssCode");
		newsTitle = myIntent.getStringExtra("newsTitle");
		isNotRssFeed = myIntent.getBooleanExtra("isNotRssFeed", false);
		notRssParseCode = myIntent.getStringExtra("notRssParseCode");

		// set activity title
		setTitle(newsTitle);

		initUIComponent();

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		loadArticles();

	}

	public void initUIComponent() {
		HeaderListView = (CardListView) findViewById(R.id.listViewArticle);

		// set behavior on pull to refresh action
		/*
		 * HeaderListView.setOnRefreshListener(new OnRefreshListener<ListView>()
		 * {
		 * 
		 * @Override public void onRefresh(PullToRefreshBase<ListView>
		 * refreshView) { isRefreshed = true; loadArticles(); } });
		 */
	}

	public void populateArticleList(ArrayList<ArticleHeader> articleList) {

		ArrayList<Card> cards = new ArrayList<Card>();
		imageAsyncTasks = new ArrayList<GetImageLinkAsync>();
		CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(this, cards);

		for (int i = 0; i < articleList.size(); i++) {
			// Create a Card
			NewsCard card = new NewsCard(this, R.layout.row_card,
					articleList.get(i));
			card.setTitle(articleList.get(i).getTitle());

			CustomCardThumbails cardThumbnail = new CustomCardThumbails(null,
					this);
			cardThumbnail.setExternalUsage(true);
			card.addCardThumbnail(cardThumbnail);

			GetImageLinkAsync getImgASync = new GetImageLinkAsync(card, this,
					mCardArrayAdapter);
			getImgASync.execute(articleList.get(i).getLink(), articleList
					.get(i).getId());
			imageAsyncTasks.add(getImgASync);

			card.setOnClickListener(new ArticleOnClickListener(articleList
					.get(i), newsTitle));

			cards.add(card);
		}

		mCardArrayAdapter.notifyDataSetChanged();

		AnimationAdapter animCardArrayAdapter = new SwingBottomInAnimationAdapter(
				mCardArrayAdapter);
		animCardArrayAdapter.setAbsListView(HeaderListView);
		
		HeaderListView.setExternalAdapter(animCardArrayAdapter,
				mCardArrayAdapter);

		// HeaderListView.setAdapter(mCardArrayAdapter);

		// scroll bar
		HeaderListView.setFastScrollEnabled(true);
		HeaderListView.setScrollbarFadingEnabled(false);
		HeaderListView.setScrollContainer(true);
		HeaderListView.setTextFilterEnabled(true);

		/*
		 * HeaderListView.getRefreshableView().setAdapter(listArticleAdapter);
		 * 
		 * // scroll bar
		 * HeaderListView.getRefreshableView().setFastScrollEnabled(true);
		 * HeaderListView.getRefreshableView().setScrollbarFadingEnabled(false);
		 * HeaderListView.getRefreshableView().setScrollContainer(true);
		 * HeaderListView.getRefreshableView().setTextFilterEnabled(true);
		 */

	}

	@Override
	public void run() {

		RSSReader rssReader = new RSSReader();
		articleList = new ArrayList<ArticleHeader>();
		boolean useCache = true;

		try {

			// force network response
			if (isRefreshed) {
				useCache = false;
			}

			if(isNotRssFeed)
			{
				articleList = HTMLPageParser.getArticleListFromHTML(notRssParseCode, rssFeed.get(0));
			}else
			{
				articleList = rssReader.readFeed(rssFeed, rssId, useCache);
			}
			isRefreshed = false;

			mHandler.sendEmptyMessage(0);

		} catch (Exception e) {
			mHandler.sendEmptyMessage(1);
			logsProvider.error(e);
		}

		progressDialog.dismiss();

		// animation
		anim = AnimationUtils.loadAnimation(getApplicationContext(),
				R.animator.fade_in);
		HeaderListView.setAnimation(anim);
		anim.start();

	}

	public void finish() {
		super.finish();

		// stop asynctask
		if (imageAsyncTasks != null) {
			
			for(int i=0;i<imageAsyncTasks.size();i++)
			{
				imageAsyncTasks.get(i).cancel(true);
			}
		}

		// transition animation
		overridePendingTransition(R.animator.push_right_in,
				R.animator.push_right_out);

	}

	private void loadArticles() {

		// display waiting dialog
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Please wait...");
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						ArticleListActivity.this.finish();
					}
				});

		progressDialog.setOnCancelListener(new OnCancelListener() {

			// when progress dialog is cancelled
			public void onCancel(DialogInterface dialog) {
				// close activity
				ArticleListActivity.this.finish();
			}
		});

		progressDialog.show();

		mHandler = new Handler() {
			public void handleMessage(Message msg) {

				if (msg.what == 0) {

					populateArticleList(articleList);
					// HeaderListView.onRefreshComplete();
				} else if (msg.what == 1) {
					displayErrorMessage("An error has occured");
				}

			}
		};

		// meanwhile retrieving list of articles
		Thread tRetrieveList = new Thread(this);

		tRetrieveList.start();

	}

	public void displayErrorMessage(String text) {
		AlertDialog ad = new AlertDialog.Builder(this).create();
		ad.setMessage(text);
		ad.setCanceledOnTouchOutside(false);
		ad.setButton(DialogInterface.BUTTON_NEGATIVE, "Ok",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						ArticleListActivity.this.finish();
					}
				});

		ad.show();
	}

}
