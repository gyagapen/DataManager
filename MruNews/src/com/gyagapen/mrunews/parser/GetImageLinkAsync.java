package com.gyagapen.mrunews.parser;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.view.CardView;
import android.content.Context;
import android.os.AsyncTask;

import com.androidquery.AQuery;
import com.gyagapen.mrunews.common.CustomCardThumbails;

/**
 * Load an image asynchronously
 * 
 * @author Gui
 * 
 */
public class GetImageLinkAsync extends AsyncTask<String, Void, String> {

	private Card card;
	private AQuery aq;
	private String imageId;
	private Context context;
	CardArrayAdapter mCardArrayAdapter = null;

	public GetImageLinkAsync(Card card, Context context,
			CardArrayAdapter mCardArrayAdapter) {
		this.card = card;
		this.context = context;
		this.mCardArrayAdapter = mCardArrayAdapter;
	}

	protected String doInBackground(String... params) {

		String imageLink = null;
		// get images
		try {
			imageId = params[0];
			if (!isCancelled()) {
				imageLink = HTMLPageParser.getImageFromLink(params[0],
						params[1], null);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return imageLink;
	}

	@Override
	protected void onPostExecute(String result) {

		if (!isCancelled()) {
			CustomCardThumbails cardThumbnail = (CustomCardThumbails) card
					.getCardThumbnail();

			String imageLink = cardThumbnail.getImageLink();
			if ((imageLink == null) || (imageLink.trim().equals(""))) {

				cardThumbnail.setImageLink(result);
				card.addCardThumbnail(cardThumbnail);

				if (mCardArrayAdapter != null) {
					mCardArrayAdapter.notifyDataSetChanged();
				}

			}
		}

	}
}
