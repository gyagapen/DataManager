package com.gyagapen.mrunews.common;

import android.app.Activity;
import android.app.ProgressDialog;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.gyagapen.mrunews.ArticleViewActivity;

public class VideoChromeWebClient extends WebChromeClient {
	FrameLayout.LayoutParams LayoutParameters = new FrameLayout.LayoutParams(
			FrameLayout.LayoutParams.MATCH_PARENT,
			FrameLayout.LayoutParams.MATCH_PARENT);

	private View mCustomView = null;
	private FrameLayout mContentView;
	private FrameLayout mCustomViewContainer;
	private WebChromeClient.CustomViewCallback mCustomViewCallback;
	private Activity activity;

	public VideoChromeWebClient(
			FrameLayout frameLayout, Activity act) {

		mContentView = frameLayout;
		activity = act;
	}
	
	public View getmCustomViewContainer() {
		return mCustomView;
	}

	 
	
	@Override
	public void onShowCustomView(View view, CustomViewCallback callback) {
		// if a view already exists then immediately terminate the new one
		if (mCustomView != null) {
			callback.onCustomViewHidden();
			return;
		}
		// mContentView = (RelativeLayout) findViewById(R.id.activity_main);
		mContentView.setVisibility(View.GONE);
		mCustomViewContainer = new FrameLayout(activity.getBaseContext());
		mCustomViewContainer.setLayoutParams(LayoutParameters);
		mCustomViewContainer.setBackgroundResource(android.R.color.black);
		view.setLayoutParams(LayoutParameters);
		mCustomViewContainer.addView(view);
		mCustomView = view;
		mCustomViewCallback = callback;
		mCustomViewContainer.setVisibility(View.VISIBLE);
		activity.setContentView(mCustomViewContainer);
		
	}

	@Override
	public void onHideCustomView() {
		if (mCustomView == null) {
			return;
		} else {
			// Hide the custom view.
			mCustomView.setVisibility(View.GONE);
			// Remove the custom view from its container.
			mCustomViewContainer.removeView(mCustomView);
			mCustomView = null;
			mCustomViewContainer.setVisibility(View.GONE);
			mCustomViewCallback.onCustomViewHidden();
			// Show the content view.
			mContentView.setVisibility(View.VISIBLE);
			activity.setContentView(mContentView);
		}
	}

	
	
}