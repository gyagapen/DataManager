package com.gyagapen.mrunews.common;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.Card.OnCardClickListener;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gyagapen.mrunews.R;
import com.gyagapen.mrunews.entities.ArticleHeader;

public class NewsCard extends Card {

    private TextView mTitle;
    private TextView mSecondaryTitle;
    private ArticleHeader articleHeader;
	private Animation anim;


    /**
     * Constructor with a custom inner layout
     * @param context
     */
    public NewsCard(Context context, ArticleHeader articleHeader) {
        this(context, R.layout.row_card, articleHeader);
    }

    /**
     *
     * @param context
     * @param innerLayout
     */
    public NewsCard(Context context, int innerLayout, ArticleHeader articleHeader) {
        super(context, innerLayout);
        init(articleHeader);
    }

    /**
     * Init
     */
    private void init(ArticleHeader articleHeader){

    	this.articleHeader = articleHeader;

        //Set a OnClickListener listener
        setOnClickListener(new OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
                Toast.makeText(getContext(), "Click Listener card=", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void setupInnerViewElements(ViewGroup parent, View view) {

        //Retrieve elements
        mTitle = (TextView) parent.findViewById(R.id.card_main_inner_simple_title);
        mSecondaryTitle = (TextView) parent.findViewById(R.id.card_main_inner_secondary_title);


        if (mTitle!=null)
            mTitle.setText(articleHeader.getTitle());

        if (mSecondaryTitle!=null)
            mSecondaryTitle.setText(articleHeader.getPublishedDate());
        


    }
    

    
    

    
    
}