package com.gyagapen.mrunews;

import com.gyagapen.mrunews.R;
import com.gyagapen.mrunews.R.id;
import com.gyagapen.mrunews.R.layout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

public class WebViewActivity extends Activity {

	private WebView webView;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.article_web_content);

		//display an error as it's fallback
		displayErrorMessage("An error occured while generating the mobile version, a web view will be opened instead");
		
		webView = (WebView) findViewById(R.id.WebViewArticle);
		webView.getSettings().setJavaScriptEnabled(true);

		// getting link to display
		Intent myIntent = getIntent();
		String link = myIntent.getStringExtra("link");

		webView.loadUrl(link);
	}
	
	
	public void displayErrorMessage(String text) {
		AlertDialog ad = new AlertDialog.Builder(this).create();
		ad.setMessage(text);
		ad.setCanceledOnTouchOutside(false);
		ad.setButton(DialogInterface.BUTTON_NEGATIVE, "Ok",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

		ad.show();
	}
}
