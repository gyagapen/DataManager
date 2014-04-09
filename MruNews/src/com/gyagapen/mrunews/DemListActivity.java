package com.gyagapen.mrunews;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.TextView;

import com.gyagapen.mrunews.adapters.SemdexListAdapter;
import com.gyagapen.mrunews.common.LogsProvider;
import com.gyagapen.mrunews.entities.SemdexEntities;
import com.gyagapen.mrunews.entities.SemdexEntity;
import com.gyagapen.mrunews.parser.HTMLPageParser;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

@SuppressLint("NewApi")
public class DemListActivity extends Activity implements Runnable {

	private String demLink = "http://www.stockexchangeofmauritius.com/demquotes/";
	private SemdexEntities demEntityList = null;
	private SemdexListAdapter semdexListAdapter;
	private boolean isRefreshed = false;
	private LogsProvider logsProvider = null;

	// waiting dialog
	private ProgressDialog progressDialog;

	private Animation anim;

	private Handler mHandler = null;

	private PullToRefreshListView demListView;
	private TextView tvLastUpdated;

	String newsTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.semdex_list);

		logsProvider = new LogsProvider(null, this.getClass());

		initUIComponent();

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		loadSemdexEntites();

	}

	public void initUIComponent() {
		
		tvLastUpdated = (TextView)findViewById(R.id.tvSemdexLastUpdated);
		
		demListView = (PullToRefreshListView) findViewById(R.id.lvSemdex);

		// set behavior on pull to refresh action
		demListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				isRefreshed = true;
				loadSemdexEntites();
			}
		});
	}

	public void populateSemdexList(SemdexEntities semdexList) {
		
		//last updated text box
		tvLastUpdated.setText(semdexList.getLastUpdated());
		
		semdexListAdapter = new SemdexListAdapter(semdexList.getSemdexEntities(), this);
		demListView.getRefreshableView().setAdapter(semdexListAdapter);

		// scroll bar
		demListView.getRefreshableView().setFastScrollEnabled(true);
		demListView.getRefreshableView().setScrollbarFadingEnabled(false);
		demListView.getRefreshableView().setScrollContainer(true);
		demListView.getRefreshableView().setTextFilterEnabled(true);
		


	}
	

	public void run() {

		
			HTMLPageParser htmlParser = new HTMLPageParser(demLink,"");
			boolean useCache = true;

			try {

				// force network response
				if (isRefreshed) {
					useCache = false;
				}

				demEntityList = htmlParser.getSemdexList(false);
				isRefreshed = false;

				mHandler.sendEmptyMessage(0);

			} catch (Exception e) {
				e.printStackTrace();
				//mHandler.sendEmptyMessage(1);
			}
		

		progressDialog.dismiss();

		// animation
		anim = AnimationUtils.loadAnimation(getApplicationContext(),
				R.animator.push_right_in);
		demListView.getRefreshableView().setAnimation(anim);
		anim.start();

	}

	public void finish() {
		super.finish();

		// transition animation
		overridePendingTransition(R.animator.push_right_in,
				R.animator.push_right_out);

	}

	private void loadSemdexEntites() {

		// display waiting dialog
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Please wait...");
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						DemListActivity.this.finish();
					}
				});
		
		progressDialog.setOnCancelListener(new OnCancelListener() {
			
			//when progress dialog is cancelled
			public void onCancel(DialogInterface dialog) {
				//close activity
				DemListActivity.this.finish();
			}
		});

		progressDialog.show();

		mHandler = new Handler() {
			public void handleMessage(Message msg) {

				if (msg.what == 0) {

					populateSemdexList(demEntityList);;
					demListView.onRefreshComplete();
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
						DemListActivity.this.finish();
					}
				});

		ad.show();
	}
	
	

}
