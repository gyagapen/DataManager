package com.gyagapen.mrunews.parser;

import java.io.IOException;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ArrayAdapter;

import com.androidquery.AQuery;
import com.gyagapen.mrunews.adapters.ListArticleAdapter;

/**
 * Load an image asynchronously
 * 
 * @author Gui
 * 
 */
public class GetImageAsync extends AsyncTask<String, Void, String> {

	private View parentView;
	private AQuery aq;
	private String imageId;
	private ListArticleAdapter arrayAdapter;
	private int position;

	public GetImageAsync(View parentView, ListArticleAdapter arrayAdapter, int position) {
		this.parentView = parentView;
		this.arrayAdapter = arrayAdapter;
		this.position = position;
	}

	protected String doInBackground(String... params) {

		String imageLink = null;
		// get images
		try {
			imageId = params[0];
			if(!isCancelled())
			{
				imageLink = HTMLPageParser.getImageFromLink(params[0], params[1], null);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return imageLink;
	}

	@Override
	protected void onPostExecute(String result) {

		if(!isCancelled())
		{
			arrayAdapter.updateArticleLink(position, result);
			arrayAdapter.setNotifyOnChange(true);
		}

	}
}
