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
public class SemdexListActivity extends Activity implements Runnable {

	private String semdexLink = "http://www.stockexchangeofmauritius.com/officialquotes/";
	private SemdexEntities semdexEntityList = null;
	private SemdexListAdapter semdexListAdapter;
	private boolean isRefreshed = false;
	private LogsProvider logsProvider = null;

	// waiting dialog
	private ProgressDialog progressDialog;

	private Animation anim;

	private Handler mHandler = null;

	private PullToRefreshListView SemdexListView;
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
		
		SemdexListView = (PullToRefreshListView) findViewById(R.id.lvSemdex);

		// set behavior on pull to refresh action
		SemdexListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
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
		SemdexListView.getRefreshableView().setAdapter(semdexListAdapter);

		// scroll bar
		SemdexListView.getRefreshableView().setFastScrollEnabled(true);
		SemdexListView.getRefreshableView().setScrollbarFadingEnabled(false);
		SemdexListView.getRefreshableView().setScrollContainer(true);
		SemdexListView.getRefreshableView().setTextFilterEnabled(true);
		


	}
	

	public void run() {

		
			HTMLPageParser htmlParser = new HTMLPageParser(semdexLink,"");
			boolean useCache = true;

			try {

				// force network response
				if (isRefreshed) {
					useCache = false;
				}

				semdexEntityList = htmlParser.getSemdexList(true);
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
		SemdexListView.getRefreshableView().setAnimation(anim);
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
						SemdexListActivity.this.finish();
					}
				});
		
		progressDialog.setOnCancelListener(new OnCancelListener() {
			
			//when progress dialog is cancelled
			public void onCancel(DialogInterface dialog) {
				//close activity
				SemdexListActivity.this.finish();
			}
		});

		progressDialog.show();

		mHandler = new Handler() {
			public void handleMessage(Message msg) {

				if (msg.what == 0) {

					populateSemdexList(semdexEntityList);;
					SemdexListView.onRefreshComplete();
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
						SemdexListActivity.this.finish();
					}
				});

		ad.show();
	}
	
	

}
