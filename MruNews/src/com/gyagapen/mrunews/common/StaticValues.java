package com.gyagapen.mrunews.common;

import java.util.ArrayList;

import android.content.Context;

import com.gyagapen.mrunews.R;
import com.gyagapen.mrunews.entities.News;
import com.gyagapen.mrunews.entities.NewsSubEntry;

public class StaticValues {
	
	public static final String MAIL_RECIPIENT = "gyagapen@gmail.com";
	public static final String MAIL_SUBJECT = "Moris News";
	public static final String MY_AD_UNIT_ID = "ca-app-pub-4662434208516595/3831694465";

	private static String LEXPRESS_NAME = "Lexpress";
	private static int LEXPRESS_IMG = R.drawable.lexpress;
	private static ArrayList<NewsSubEntry> LEXPRESS_RSS = new ArrayList<NewsSubEntry>() {
		{
			add(new NewsSubEntry("> Général","http://www.lexpress.mu/rss.xml"));
			add(new NewsSubEntry("> Société","http://www.lexpress.mu/taxonomy/term/11/feed"));
			add(new NewsSubEntry("> Forums","http://www.lexpress.mu/taxonomy/term/121/feed"));
			add(new NewsSubEntry("> Football","http://www.lexpress.mu/taxonomy/term/32/feed"));
			add(new NewsSubEntry("> Actualités","http://www.lexpress.mu/taxonomy/term/10/feed"));
			add(new NewsSubEntry("> Education","http://www.lexpress.mu/taxonomy/term/22/feed"));
			add(new NewsSubEntry("> Rodrigues","http://www.lexpress.mu/taxonomy/term/13/feed"));
			add(new NewsSubEntry("> Maurice","http://www.lexpress.mu/taxonomy/term/162/feed"));
			add(new NewsSubEntry("> Extradition","http://www.lexpress.mu/taxonomy/term/537/feed"));
			add(new NewsSubEntry("> Marine","http://www.lexpress.mu/taxonomy/term/583/feed"));
		}
	};
	public static String LEXPRESS_CODE = "LEX";
	
	private static String CINQPLUS_NAME = "5Plus";
	private static int CINQPLUS_IMG = R.drawable.cinqplus;
	private static ArrayList<String> CINQPLUS_RSS = new ArrayList<String>() {
		{
			add("http://www.5plus.mu/rss.xml");
		}
	};
	public static String CINQPLUS_CODE = "CINQPLUS";
	
	public static String LEMAURICIEN_CODE = "MAU";
	private static String LEMAURICIEN_NAME = "Le Mauricien ";
	private static int LEMAURICIEN_IMG = R.drawable.lemauricien;
	private static ArrayList<NewsSubEntry> LEMAURICIEN_SUBMENU = new ArrayList<NewsSubEntry>() {
		{
			add(new NewsSubEntry("> Général","http://www.lemauricien.com/rss/articles/all"));
			add(new NewsSubEntry("> Economie","http://www.lemauricien.com/rss/articles/Economie"));
			add(new NewsSubEntry("> Politique","http://www.lemauricien.com/rss/articles/Politique"));
			add(new NewsSubEntry("> Faits Divers","http://www.lemauricien.com/rss/articles/Faits%20divers"));
			add(new NewsSubEntry("> Société","http://www.lemauricien.com/rss/articles/Soci%C3%A9t%C3%A9"));
			add(new NewsSubEntry("> Magazine","http://www.lemauricien.com/rss/articles/Magazine"));
			add(new NewsSubEntry("> Sports","http://www.lemauricien.com/rss/articles/Sports"));
			add(new NewsSubEntry("> International","http://www.lemauricien.com/rss/articles/International"));

		}
	};
		

	
	public static String DEFI_PLUS_CODE = "DFP";
	private static String DEFI_PLUS_NAME = "Le Défi Plus";
	private static int DEFI_PLUS_IMG = R.drawable.defi_plus;
	private static ArrayList<NewsSubEntry> DEFI_PLUS_SUBMENU = new ArrayList<NewsSubEntry>() {
		{
			add(new NewsSubEntry("> Général","http://www.defimedia.info/defi-plus.feed"));
			add(new NewsSubEntry("> Actualités","http://www.defimedia.info/defi-plus/dp-actualites.feed"));
			add(new NewsSubEntry("> Economie","http://www.defimedia.info/defi-plus/dp-economie.feed"));
			add(new NewsSubEntry("> Education","http://www.defimedia.info/defi-plus/dp-education.feed"));
			add(new NewsSubEntry("> Enquête","http://www.defimedia.info/defi-plus/dp-enquete.feed"));
			add(new NewsSubEntry("> Faits Divers","http://www.defimedia.info/defi-plus/dp-faits-divers.feed"));
			add(new NewsSubEntry("> Interview","http://www.defimedia.info/defi-plus/dp-interview.feed"));
			add(new NewsSubEntry("> Magazine","http://www.defimedia.info/defi-plus/dp-magazine.feed"));
			add(new NewsSubEntry("> Travail","http://www.defimedia.info/defi-plus/dp-monde-travail.feed"));
			add(new NewsSubEntry("> Société","http://www.defimedia.info/defi-plus/dp-societe.feed"));
			add(new NewsSubEntry("> Tribunal","http://www.defimedia.info/defi-plus/dp-tribunaux.feed"));

		}
	};
	
	private static String DEFI_QUOTIDIEN_NAME = "Le Défi Quotidien";
	private static int DEFI_QUOTIDIEN_IMG = R.drawable.defiquotidien;
	private static ArrayList<NewsSubEntry> DEFI_QUOTIDIEN_SUBMENU = new ArrayList<NewsSubEntry>() {
		{
			add(new NewsSubEntry("> Live News","http://www.defimedia.info/live-news.feed"));
			add(new NewsSubEntry("> Général","http://www.defimedia.info/defi-quotidien.feed"));
			add(new NewsSubEntry("> Actualités","http://www.defimedia.info/defi-quotidien/dq-actualites.feed"));
			add(new NewsSubEntry("> Economie","http://www.defimedia.info/defi-quotidien/dq-economie.feed"));
			add(new NewsSubEntry("> Défi Zen","http://www.defimedia.info/defi-quotidien/dq-defi-zen.feed"));
			add(new NewsSubEntry("> Faits Divers","http://www.defimedia.info/defi-quotidien/dq-faits-divers.feed"));
			add(new NewsSubEntry("> Interview","http://www.defimedia.info/defi-quotidien/dq-interview.feed"));
			add(new NewsSubEntry("> Magazine","http://www.defimedia.info/defi-quotidien/dq-magazine.feed"));
			add(new NewsSubEntry("> Société","http://www.defimedia.info/defi-quotidien/dq-societe.feed"));
			add(new NewsSubEntry("> Tribunal","http://www.defimedia.info/defi-quotidien/dq-tribunaux.feed"));
			add(new NewsSubEntry("> Tribune","http://www.defimedia.info/defi-quotidien/dq-tribune.feed"));
			add(new NewsSubEntry("> Xplik ou k","http://www.defimedia.info/defi-quotidien/dq-xplik-cas.feed"));

		}
	};
	
	
	private static String DIMANCHE_HEBDO_NAME = "Le Dimanche Hebdo";
	private static int DIMANCHE_HEBDO_IMG = R.drawable.dimanch_hebdo;
	private static ArrayList<NewsSubEntry> DIMANCHE_HEBDO_SUBMENU = new ArrayList<NewsSubEntry>() {
		{
			add(new NewsSubEntry("> Général","http://www.defimedia.info/dimanche-hebdo.feed"));
			add(new NewsSubEntry("> Actualités","http://www.defimedia.info/dimanche-hebdo/dh-actualites.feed"));
			add(new NewsSubEntry("> Cover Story","http://www.defimedia.info/dimanche-hebdo/cover-story.feed"));
			add(new NewsSubEntry("> Cool Zone", "http://www.defimedia.info/dimanche-hebdo/dh-cool-zone.feed"));
			add(new NewsSubEntry("> Faits Divers","http://www.defimedia.info/dimanche-hebdo/dh-faits-divers.feed"));
			add(new NewsSubEntry("> Interview","http://www.defimedia.info/dimanche-hebdo/dh-interview.feed"));
			add(new NewsSubEntry("> Magazine","http://www.defimedia.info/dimanche-hebdo/dh-magazine.feed"));
			add(new NewsSubEntry("> People","http://www.defimedia.info/dimanche-hebdo/dh-people.feed"));
			add(new NewsSubEntry("> Opinion","http://www.defimedia.info/dimanche-hebdo/dh-point-vue.feed"));
			add(new NewsSubEntry("> Reportage","http://www.defimedia.info/dimanche-hebdo/dh-reportage.feed"));
			add(new NewsSubEntry("> Tribunaux","http://www.defimedia.info/dimanche-hebdo/dh-tribunaux.feed"));

		}
	};
	
	
	private static String NEWS_SUNDAY_NAME = "News On Sunday";
	private static int NEWS_SUNDAY_IMG = R.drawable.news_on_sunday;
	private static ArrayList<NewsSubEntry> NEWS_SUNDAY_SUBMENU = new ArrayList<NewsSubEntry>() {
		{
			add(new NewsSubEntry("> General","http://www.defimedia.info/news-sunday.feed"));
			add(new NewsSubEntry("> News","http://www.defimedia.info/news-sunday/nos-news.feed"));
			add(new NewsSubEntry("> Court News","http://www.defimedia.info/news-sunday/court-news.feed"));
			add(new NewsSubEntry("> Education","http://www.defimedia.info/news-sunday/nos-education.feed"));
			add(new NewsSubEntry("> Health", "http://www.defimedia.info/news-sunday/health.feed"));
			add(new NewsSubEntry("> Interview","http://www.defimedia.info/news-sunday/interview.feed"));
			add(new NewsSubEntry("> Magazine","http://www.defimedia.info/news-sunday/magazine.feed"));
			add(new NewsSubEntry("> Parliament","http://www.defimedia.info/news-sunday/nos-parliament.feed"));
			add(new NewsSubEntry("> People","http://www.defimedia.info/news-sunday/nos-people.feed"));
			add(new NewsSubEntry("> Police News","http://www.defimedia.info/news-sunday/police-news.feed"));
			add(new NewsSubEntry("> Society","http://www.defimedia.info/news-sunday/society.feed"));

		}
	};
	
	public static String lE_MATINAL_CODE = "MAT";
	private static String LE_MATINAL_NAME = "Le Matinal";
	private static int LE_MATINAL_IMG = R.drawable.lematinal;
	private static ArrayList<String> LE_MATINAL_RSS = new ArrayList<String>() {
		{
			add("http://www.lematinal.com/feed/index.1.rss");

		}
	};


	

	static public ArrayList<News> getNewsPapers() {
		ArrayList<News> newsList = new ArrayList<News>();

		News lexpressNews = new News(LEXPRESS_NAME, LEXPRESS_CODE, LEXPRESS_IMG,LEXPRESS_RSS);
		newsList.add(lexpressNews);
		
		
		News leMatinalNews = new News(LE_MATINAL_NAME, lE_MATINAL_CODE,
				LE_MATINAL_RSS,LE_MATINAL_IMG);
		newsList.add(leMatinalNews);	
		
		News leMauricienNews = new News(LEMAURICIEN_NAME, LEMAURICIEN_CODE, LEMAURICIEN_IMG, LEMAURICIEN_SUBMENU);
		newsList.add(leMauricienNews);
		
		News defiQuotidienNews = new News(DEFI_QUOTIDIEN_NAME, DEFI_PLUS_CODE,
				 DEFI_QUOTIDIEN_IMG, DEFI_QUOTIDIEN_SUBMENU);
		newsList.add(defiQuotidienNews);
		
		News cinqPlus = new News(CINQPLUS_NAME, CINQPLUS_CODE, CINQPLUS_RSS, CINQPLUS_IMG);
		newsList.add(cinqPlus);
		
		News defiPlusNews = new News(DEFI_PLUS_NAME, DEFI_PLUS_CODE,
				 DEFI_PLUS_IMG, DEFI_PLUS_SUBMENU);
		newsList.add(defiPlusNews);
		
		News dimancheHebdoNews = new News(DIMANCHE_HEBDO_NAME, DEFI_PLUS_CODE,
				 DIMANCHE_HEBDO_IMG, DIMANCHE_HEBDO_SUBMENU);
		newsList.add(dimancheHebdoNews);
		
		News newsOnSundayNews = new News(NEWS_SUNDAY_NAME, DEFI_PLUS_CODE,
				 NEWS_SUNDAY_IMG, NEWS_SUNDAY_SUBMENU);
		newsList.add(newsOnSundayNews);


		return newsList;
	}

}
