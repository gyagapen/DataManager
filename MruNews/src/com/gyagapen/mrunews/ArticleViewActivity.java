package com.gyagapen.mrunews;

import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthAdapter.Provider;
import org.brickred.socialauth.android.SocialAuthError;
import org.brickred.socialauth.android.SocialAuthListener;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.gyagapen.mrunews.common.VideoChromeWebClient;
import com.gyagapen.mrunews.entities.ArticleContent;
import com.gyagapen.mrunews.parser.HTMLPageParser;

public class ArticleViewActivity extends Activity implements Runnable {

	private String articleLink = null;
	private String articleId = null;

	private WebView webView = null;

	private String displayContent = null;
	private ArticleContent artContent = null;

	private Button buttonComments;
	private Button buttonLinkToWeb;
	private Button share = null;

	private String buttonCommentText = null;
	private String artTitle = null;
	private String imageLink = null;
	private LinearLayout buttonsLayout = null;

	HTMLPageParser htmlPageParser = null;

	private int screenHeight;
	private int screenWidth;
	private boolean activityIsActive = true;

	private FrameLayout frameLayout;
	private VideoChromeWebClient customWebClient;

	// SocialAuth Component
	SocialAuthAdapter adapter;

	// The "x" and "y" position of the "Show Button" on screen.
	Point p;

	private Animation anim;

	// waiting dialog
	private ProgressDialog progressDialog;

	private Handler mHandler = null;
	
	private AdView adView = null;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.article_webview);
		
		//load ads
		adView = (AdView)this.findViewById(R.id.adViewWeb);
	    AdRequest adRequest = new AdRequest.Builder()
	    .build();
	    adView.loadAd(adRequest);

		// screen size
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		screenHeight = metrics.heightPixels;
		screenWidth = metrics.widthPixels;

		// retrieve parameters
		Intent myIntent = getIntent();
		articleLink = myIntent.getStringExtra("ArticleLink");
		articleId = myIntent.getStringExtra("ArticleId");
		String newsName = myIntent.getStringExtra("NewsName");

		setTitle(newsName);

		initView();

		// display waiting dialog
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Please wait...");
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						ArticleViewActivity.this.finish();
					}
				});

		progressDialog.setOnCancelListener(new OnCancelListener() {

			// when progress dialog is cancelled
			public void onCancel(DialogInterface dialog) {
				// close activity
				ArticleViewActivity.this.finish();
			}
		});

		progressDialog.show();

		mHandler = new Handler() {
			public void handleMessage(Message msg) {

				if (msg.what == 0) {

					// set number of comments
					buttonComments.setText(buttonCommentText);

					// resize display content
					displayContent = "<html><head><style>iframe {max-width: 100%; width:auto; height: auto;}img {max-width: 100%;height: auto;}</style></head><body>"
							+ displayContent + "</body></html>";

					webView.loadData(displayContent,
							"text/html; charset=utf-8", "utf-8");

					// button layout is shown
					buttonsLayout.setVisibility(View.VISIBLE);

					progressDialog.dismiss();

					// animation
					anim = AnimationUtils.loadAnimation(
							getApplicationContext(), R.animator.zoom_in_center);
					frameLayout.setAnimation(anim);
					anim.start();
				}
			}
		};

		// meanwhile retrieving article content
		Thread tRetrieveContent = new Thread(this);

		tRetrieveContent.start();

	}

	public void initView() {

		webView = (WebView) findViewById(R.id.htmlWebView);

		frameLayout = (FrameLayout) findViewById(R.id.articleViewFrame);

		// Initialize the WebView
		webView.setScrollbarFadingEnabled(true);
		webView.getSettings().setLoadsImagesAutomatically(true);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setPluginState(PluginState.ON);

		buttonsLayout = (LinearLayout) findViewById(R.id.buttonsLayout);

		customWebClient = new VideoChromeWebClient(frameLayout, this);
		webView.setWebChromeClient(customWebClient);

		// cache
		webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		webView.getSettings().setAppCachePath(
				getExternalCacheDir().getAbsolutePath());
		webView.getSettings().setAppCacheEnabled(true);

		buttonComments = (Button) findViewById(R.id.butComment);
		frameLayout.getForeground().setAlpha(0);

		buttonComments.setBackgroundResource(R.drawable.blue_gradient);
		buttonComments.setTextColor(Color.WHITE);
		buttonComments.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				// Open popup window
				if (p != null)
					showPopup(ArticleViewActivity.this, p);
			}
		});

		buttonLinkToWeb = (Button) findViewById(R.id.butLinkToWeb);
		buttonLinkToWeb.setTextColor(Color.WHITE);
		buttonLinkToWeb.setBackgroundResource(R.drawable.blue_gradient);
		buttonLinkToWeb.setOnClickListener(new OnClickListener() {

			// open link in browser
			public void onClick(View v) {
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
						.parse(articleLink));
				startActivity(browserIntent);
			}
		});

		share = (Button) findViewById(R.id.butShare);

		share.setText("Share");
		share.setTextColor(Color.WHITE);
		share.setBackgroundResource(R.drawable.blue_gradient);

		// Add it to Library
		adapter = new SocialAuthAdapter(new ResponseListener());

		// Add providers
		adapter.addProvider(Provider.FACEBOOK, R.drawable.facebook);
		adapter.addProvider(Provider.TWITTER, R.drawable.twitter);
		adapter.addProvider(Provider.EMAIL, R.drawable.email);

		// Providers require setting user call Back url
		adapter.addCallBack(Provider.TWITTER,
				"http://socialauth.in/socialauthdemo/socialAuthSuccessAction.do");

		adapter.enable(share);

	}

	@Override
	public void run() {
		// getting info to display
		htmlPageParser = new HTMLPageParser(articleLink, articleId);
		try {
			artContent = htmlPageParser.parsePage();

			artTitle = "<h3>" + artContent.getTitle() + "</h3>";
			imageLink = artContent.getImageLink();
			String articleHtmlContent = artContent.getHtmlContent();
			displayContent = artTitle + "<body style=\"text-align:justify\">"
					+ articleHtmlContent + "</body></Html>";
			buttonCommentText = buttonComments.getText() + " ("
					+ artContent.getCommentCount() + ")";

			mHandler.sendEmptyMessage(0);

			// progressDialog.dismiss();

		} catch (Exception e) {

			e.printStackTrace();

			progressDialog.dismiss();

			if (activityIsActive) {
				// fallback : open webview activity
				Intent webViewIntent = new Intent(this, WebViewActivity.class);
				webViewIntent.putExtra("link", articleLink);
				startActivity(webViewIntent);

				// end the activity
				finish();
			}
		}

	}

	public void finish() {
		super.finish();

		activityIsActive = false;

		// transition animation
		overridePendingTransition(R.animator.zoom_in_center,
				R.animator.zoom_out);
	}

	protected void onPause() {

		// close progress dialog if it's opened
		if (progressDialog.isShowing()) {
			progressDialog.dismiss();
		}

		super.onPause();
	}

	public void onBackPressed() {

		// close video full screen view

		if (customWebClient.getmCustomViewContainer() != null) {
			customWebClient.onHideCustomView();
		} else {
			super.onBackPressed();
		}

	}

	@Override
	protected void onDestroy() {

		activityIsActive = false;
		adView.destroy();

		// webView.destroy();
		super.onDestroy();
	}

	// Get the x and y position after the button is draw on screen
	// (It's important to note that we can't get the position in the onCreate(),
	// because at that stage most probably the view isn't drawn yet, so it will
	// return (0, 0))
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {

		int[] location = new int[2];

		// Get the x, y location and store it in the location[] array
		// location[0] = x, location[1] = y.
		buttonComments.getLocationOnScreen(location);

		// Initialize the Point with x, and y positions
		p = new Point();
		p.x = (int) (location[0] * 1.1);
		p.y = (int) (location[1] * 0.7);
	}

	// The method that displays the popup.
	private void showPopup(final Activity context, Point p) {
		int popupWidth = (int) (screenWidth * 0.9);
		int popupHeight = (int) (screenHeight * 0.75);

		// Inflate the popup_layout.xml
		LinearLayout viewGroup = (LinearLayout) context
				.findViewById(R.id.popup);
		LayoutInflater layoutInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View layout = layoutInflater.inflate(R.layout.comment_webview,
				viewGroup);

		final WebView commentWebView = (WebView) layout
				.findViewById(R.id.commentWebView);

		final WebView waitingWebView = (WebView) layout.findViewById(R.id.loadWebView);
		waitingWebView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		waitingWebView.loadUrl("file:///android_asset/loading.gif");

		if (artContent.getCommentsHtml() != null) {
			commentWebView.loadData(artContent.getCommentsHtml(),
					"text/html; charset=utf-8", "utf-8");
			commentWebView.setVisibility(View.VISIBLE);
			waitingWebView.setVisibility(View.GONE);
		} else if (artContent.getCommentsIframeLink() != null) {
			// load from link
			commentWebView.getSettings().setJavaScriptEnabled(true);

			// set loading image
			commentWebView.setWebViewClient(new WebViewClient() {

				public void onPageFinished(WebView view, String url) {
					// hide loading image
					commentWebView.setVisibility(View.VISIBLE);
					waitingWebView.setVisibility(View.GONE);

				}

			});

			commentWebView.loadUrl(artContent.getCommentsIframeLink());
		}

		// Creating the PopupWindow
		final PopupWindow popup = new PopupWindow(context);
		popup.setContentView(layout);
		popup.setWidth(popupWidth);
		popup.setHeight(popupHeight);
		popup.setFocusable(true);

		// Some offset to align the popup a bit to the right, and a bit down,
		// relative to button's position.
		int OFFSET_X = 0;
		int OFFSET_Y = 0;

		popup.setOnDismissListener(new OnDismissListener() {

			public void onDismiss() {

				// undim background
				frameLayout.getForeground().setAlpha(0);
			}
		});

		// Displaying the popup at the specified location, + offsets.
		// popup.showAtLocation(layout, Gravity.CENTER, p.x + OFFSET_X, p.y
		// + OFFSET_Y);

		popup.showAtLocation(layout, Gravity.CENTER, 0, 0);

		// dim background
		frameLayout.getForeground().setAlpha(200);

	}

	/**
	 * Listens Response from Library
	 * 
	 */

	private final class ResponseListener implements DialogListener {
		/**
			 * 
			 */
		private static final long serialVersionUID = 1L;

		public void onComplete(Bundle values) {

			// Variable to receive message status
			boolean status = false;

			Log.d("ShareButton", "Authentication Successful");

			// Get name of provider after authentication
			String providerName = values.getString(SocialAuthAdapter.PROVIDER);
			Log.d("ShareButton", "Provider Name = " + providerName);

			// Share via Email Intent
			if (providerName.equalsIgnoreCase("share_mail")) {

				Intent email = new Intent(Intent.ACTION_SEND);
				// email.putExtra(Intent.EXTRA_EMAIL, new String[] { to });
				email.putExtra(Intent.EXTRA_SUBJECT, artTitle
						+ " via Moris News");
				String content = "Please check this news: " + articleLink;
				email.putExtra(Intent.EXTRA_TEXT, content);
				// need this to prompts email client only
				email.setType("message/rfc822");
				startActivity(Intent.createChooser(email,
						"Choose an Email client"));

				status = true;
				// post via twitter
			} else if (providerName.equals("twitter")) {
				adapter.updateStatus("test", new MessageListener(), true);
				status = true;
			} // post via facebook
			else {
				try {

					adapter.updateStory(artTitle, "", "", "", articleLink,
							imageLink, new MessageListener());

					status = true;
				} catch (Exception e) {
					status = false;
				}

			}

			// Post Toast or Dialog to display on screen
			if (status)
				Toast.makeText(ArticleViewActivity.this,
						"Message posted on " + providerName, Toast.LENGTH_SHORT)
						.show();
			else
				Toast.makeText(ArticleViewActivity.this,
						"Message not posted on" + providerName,
						Toast.LENGTH_SHORT).show();

		}

		public void onError(SocialAuthError error) {
			Log.d("ShareButton", "Authentication Error " + error.getMessage());
		}

		public void onCancel() {
			Log.d("ShareButton", "Authentication Cancelled");
		}

		@Override
		public void onBack() {
			// TODO Auto-generated method stub

		}

	}

	// To get status of message after authentication
	private final class MessageListener implements SocialAuthListener<Integer> {

		public void onExecute(Integer t) {
			Integer status = t;
			if (status.intValue() == 200 || status.intValue() == 201
					|| status.intValue() == 204)
				Toast.makeText(ArticleViewActivity.this, "Message posted",
						Toast.LENGTH_LONG).show();
			else
				Toast.makeText(ArticleViewActivity.this, "Message not posted",
						Toast.LENGTH_LONG).show();
		}

		public void onError(SocialAuthError e) {

		}

		@Override
		public void onExecute(String arg0, Integer arg1) {
			// TODO Auto-generated method stub

		}

	}

}
