package com.gyagapen.mrunews.common;

import it.gmariotti.cardslib.library.internal.CardThumbnail;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;
import com.gyagapen.mrunews.R;

public class CustomCardThumbails extends CardThumbnail {

	private String imageLink = null;
	private LogsProvider logsProvider = null;
	private AQuery aq;

	public CustomCardThumbails(String imageLink, Context context) {
		super(context);

		logsProvider = new LogsProvider(context, getClass());

	}

	@Override
	public void setupInnerViewElements(ViewGroup parent, View viewImage) {

		aq = new AQuery(viewImage);
		if (imageLink != null) {

			if (!imageLink.trim().equals("")) {
				aq.id(viewImage).image(imageLink);
			}
		} else {
			aq.id(viewImage).image(R.drawable.lazy_loading_image);
		}

	}

	public void setImageLink(String imageLink) {
		this.imageLink = imageLink;
	}

	public String getImageLink() {
		return imageLink;
	}

}
