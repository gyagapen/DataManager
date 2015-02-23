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
	public static final String MY_INTER_AD_UNIT_ID = "ca-app-pub-4662434208516595/8101376065";
	public static final String LOAD_ARTICLE_HTML_EXPRESS = "LOAD_ARTICLE_HTML_EXPRESS";

	private static String LEXPRESS_NAME = "Lexpress";
	private static int LEXPRESS_IMG = R.drawable.lexpress;
	private static ArrayList<NewsSubEntry> LEXPRESS_RSS = new ArrayList<NewsSubEntry>() {
		{
			add(new NewsSubEntry("> Live", "http://www.lexpress.mu",
					R.drawable.live_news, true, LOAD_ARTICLE_HTML_EXPRESS));
			add(new NewsSubEntry("> Général", "http://www.lexpress.mu/rss.xml",
					R.drawable.general));
			add(new NewsSubEntry("> Société",
					"http://www.lexpress.mu/taxonomy/term/11/feed",
					R.drawable.societe));
			add(new NewsSubEntry("> Forums",
					"http://www.lexpress.mu/taxonomy/term/121/feed",
					R.drawable.forum));
			add(new NewsSubEntry("> Football",
					"http://www.lexpress.mu/taxonomy/term/32/feed",
					R.drawable.football));
			add(new NewsSubEntry("> Actualités",
					"http://www.lexpress.mu/taxonomy/term/10/feed",
					R.drawable.actualites));
			add(new NewsSubEntry("> Education",
					"http://www.lexpress.mu/taxonomy/term/22/feed",
					R.drawable.education));
			add(new NewsSubEntry("> Rodrigues",
					"http://www.lexpress.mu/taxonomy/term/13/feed",
					R.drawable.rodrigues));
			add(new NewsSubEntry("> Maurice",
					"http://www.lexpress.mu/taxonomy/term/162/feed",
					R.drawable.maurice));
			add(new NewsSubEntry("> Extradition",
					"http://www.lexpress.mu/taxonomy/term/537/feed",
					R.drawable.extradition));
			add(new NewsSubEntry("> Marine",
					"http://www.lexpress.mu/taxonomy/term/583/feed",
					R.drawable.marine));
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
			add(new NewsSubEntry("> Général",
					"http://www.lemauricien.com/rss/articles/all",
					R.drawable.general));
			add(new NewsSubEntry("> Economie",
					"http://www.lemauricien.com/rss/articles/Economie",
					R.drawable.economie));
			add(new NewsSubEntry("> Politique",
					"http://www.lemauricien.com/rss/articles/Politique",
					R.drawable.politique));
			add(new NewsSubEntry("> Faits Divers",
					"http://www.lemauricien.com/rss/articles/Faits%20divers",
					R.drawable.faits_divers));
			add(new NewsSubEntry(
					"> Société",
					"http://www.lemauricien.com/rss/articles/Soci%C3%A9t%C3%A9",
					R.drawable.societe));
			add(new NewsSubEntry("> Magazine",
					"http://www.lemauricien.com/rss/articles/Magazine",
					R.drawable.magazine));
			add(new NewsSubEntry("> Sports",
					"http://www.lemauricien.com/rss/articles/Sports",
					R.drawable.football));
			add(new NewsSubEntry("> International",
					"http://www.lemauricien.com/rss/articles/International",
					R.drawable.international));

		}
	};

	public static String DEFI_PLUS_CODE = "DFP";
	private static String DEFI_PLUS_NAME = "Le Défi Plus";
	private static int DEFI_PLUS_IMG = R.drawable.defi_plus;
	private static ArrayList<NewsSubEntry> DEFI_PLUS_SUBMENU = new ArrayList<NewsSubEntry>() {
		{
			add(new NewsSubEntry("> Général",
					"http://www.defimedia.info/defi-plus.feed",
					R.drawable.general));
			add(new NewsSubEntry("> Actualités",
					"http://www.defimedia.info/defi-plus/dp-actualites.feed",
					R.drawable.actualites));
			add(new NewsSubEntry("> Economie",
					"http://www.defimedia.info/defi-plus/dp-economie.feed",
					R.drawable.economie));
			add(new NewsSubEntry("> Education",
					"http://www.defimedia.info/defi-plus/dp-education.feed",
					R.drawable.education));
			add(new NewsSubEntry("> Enquête",
					"http://www.defimedia.info/defi-plus/dp-enquete.feed",
					R.drawable.enquete));
			add(new NewsSubEntry("> Faits Divers",
					"http://www.defimedia.info/defi-plus/dp-faits-divers.feed",
					R.drawable.faits_divers));
			add(new NewsSubEntry("> Interview",
					"http://www.defimedia.info/defi-plus/dp-interview.feed",
					R.drawable.interview));
			add(new NewsSubEntry("> Magazine",
					"http://www.defimedia.info/defi-plus/dp-magazine.feed",
					R.drawable.magazine));
			add(new NewsSubEntry(
					"> Travail",
					"http://www.defimedia.info/defi-plus/dp-monde-travail.feed",
					R.drawable.travail));
			add(new NewsSubEntry("> Société",
					"http://www.defimedia.info/defi-plus/dp-societe.feed",
					R.drawable.societe));
			add(new NewsSubEntry("> Tribunal",
					"http://www.defimedia.info/defi-plus/dp-tribunaux.feed",
					R.drawable.tribunaux));

		}
	};

	private static String DEFI_QUOTIDIEN_NAME = "Le Défi Quotidien";
	private static int DEFI_QUOTIDIEN_IMG = R.drawable.defiquotidien;
	private static ArrayList<NewsSubEntry> DEFI_QUOTIDIEN_SUBMENU = new ArrayList<NewsSubEntry>() {
		{
			add(new NewsSubEntry("> Live News",
					"http://www.defimedia.info/live-news.feed",
					R.drawable.live_news));
			add(new NewsSubEntry("> Général",
					"http://www.defimedia.info/defi-quotidien.feed",
					R.drawable.general));
			add(new NewsSubEntry(
					"> Actualités",
					"http://www.defimedia.info/defi-quotidien/dq-actualites.feed",
					R.drawable.actualites));
			add(new NewsSubEntry(
					"> Economie",
					"http://www.defimedia.info/defi-quotidien/dq-economie.feed",
					R.drawable.economie));
			add(new NewsSubEntry(
					"> Défi Zen",
					"http://www.defimedia.info/defi-quotidien/dq-defi-zen.feed",
					R.drawable.zen));
			add(new NewsSubEntry(
					"> Faits Divers",
					"http://www.defimedia.info/defi-quotidien/dq-faits-divers.feed",
					R.drawable.faits_divers));
			add(new NewsSubEntry(
					"> Interview",
					"http://www.defimedia.info/defi-quotidien/dq-interview.feed",
					R.drawable.interview));
			add(new NewsSubEntry(
					"> Magazine",
					"http://www.defimedia.info/defi-quotidien/dq-magazine.feed",
					R.drawable.magazine));
			add(new NewsSubEntry("> Société",
					"http://www.defimedia.info/defi-quotidien/dq-societe.feed",
					R.drawable.societe));
			add(new NewsSubEntry(
					"> Tribunal",
					"http://www.defimedia.info/defi-quotidien/dq-tribunaux.feed",
					R.drawable.tribunaux));
			add(new NewsSubEntry("> Tribune",
					"http://www.defimedia.info/defi-quotidien/dq-tribune.feed",
					R.drawable.tribune));
			add(new NewsSubEntry(
					"> Xplik ou k",
					"http://www.defimedia.info/defi-quotidien/dq-xplik-cas.feed",
					R.drawable.discussion));

		}
	};

	private static String DIMANCHE_HEBDO_NAME = "Le Dimanche Hebdo";
	private static int DIMANCHE_HEBDO_IMG = R.drawable.dimanch_hebdo;
	private static ArrayList<NewsSubEntry> DIMANCHE_HEBDO_SUBMENU = new ArrayList<NewsSubEntry>() {
		{
			add(new NewsSubEntry("> Général",
					"http://www.defimedia.info/dimanche-hebdo.feed",
					R.drawable.general));
			add(new NewsSubEntry(
					"> Actualités",
					"http://www.defimedia.info/dimanche-hebdo/dh-actualites.feed",
					R.drawable.actualites));
			add(new NewsSubEntry(
					"> Cover Story",
					"http://www.defimedia.info/dimanche-hebdo/cover-story.feed",
					R.drawable.discussion));
			add(new NewsSubEntry(
					"> Cool Zone",
					"http://www.defimedia.info/dimanche-hebdo/dh-cool-zone.feed",
					R.drawable.zen));
			add(new NewsSubEntry(
					"> Faits Divers",
					"http://www.defimedia.info/dimanche-hebdo/dh-faits-divers.feed",
					R.drawable.faits_divers));
			add(new NewsSubEntry(
					"> Interview",
					"http://www.defimedia.info/dimanche-hebdo/dh-interview.feed",
					R.drawable.interview));
			add(new NewsSubEntry(
					"> Magazine",
					"http://www.defimedia.info/dimanche-hebdo/dh-magazine.feed",
					R.drawable.magazine));
			add(new NewsSubEntry("> People",
					"http://www.defimedia.info/dimanche-hebdo/dh-people.feed",
					R.drawable.societe));
			add(new NewsSubEntry(
					"> Opinion",
					"http://www.defimedia.info/dimanche-hebdo/dh-point-vue.feed",
					R.drawable.forum));
			add(new NewsSubEntry(
					"> Reportage",
					"http://www.defimedia.info/dimanche-hebdo/dh-reportage.feed",
					R.drawable.enquete));
			add(new NewsSubEntry(
					"> Tribunaux",
					"http://www.defimedia.info/dimanche-hebdo/dh-tribunaux.feed",
					R.drawable.tribunaux));

		}
	};

	private static String NEWS_SUNDAY_NAME = "News On Sunday";
	private static int NEWS_SUNDAY_IMG = R.drawable.news_on_sunday;
	private static ArrayList<NewsSubEntry> NEWS_SUNDAY_SUBMENU = new ArrayList<NewsSubEntry>() {
		{
			add(new NewsSubEntry("> General",
					"http://www.defimedia.info/news-sunday.feed",
					R.drawable.general));
			add(new NewsSubEntry("> News",
					"http://www.defimedia.info/news-sunday/nos-news.feed",
					R.drawable.actualites));
			add(new NewsSubEntry("> Court News",
					"http://www.defimedia.info/news-sunday/court-news.feed",
					R.drawable.tribunaux));
			add(new NewsSubEntry("> Education",
					"http://www.defimedia.info/news-sunday/nos-education.feed",
					R.drawable.education));
			add(new NewsSubEntry("> Health",
					"http://www.defimedia.info/news-sunday/health.feed",
					R.drawable.zen));
			add(new NewsSubEntry("> Interview",
					"http://www.defimedia.info/news-sunday/interview.feed",
					R.drawable.interview));
			add(new NewsSubEntry("> Magazine",
					"http://www.defimedia.info/news-sunday/magazine.feed",
					R.drawable.magazine));
			add(new NewsSubEntry(
					"> Parliament",
					"http://www.defimedia.info/news-sunday/nos-parliament.feed",
					R.drawable.politique));
			add(new NewsSubEntry("> People",
					"http://www.defimedia.info/news-sunday/nos-people.feed",
					R.drawable.enquete));
			add(new NewsSubEntry("> Police News",
					"http://www.defimedia.info/news-sunday/police-news.feed",
					R.drawable.discussion));
			add(new NewsSubEntry("> Society",
					"http://www.defimedia.info/news-sunday/society.feed",
					R.drawable.societe));

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

	public static String IONNEWS_CODE = "ION";
	private static String IONNEWS_NAME = "Ion News";
	private static int IONNEWS_IMG = R.drawable.ionnews;
	private static ArrayList<String> IONNEWS_RSS = new ArrayList<String>() {
		{
			add("http://ionnews.mu/feed/");

		}
	};

	static public ArrayList<News> getNewsPapers() {
		ArrayList<News> newsList = new ArrayList<News>();

		News lexpressNews = new News(LEXPRESS_NAME, LEXPRESS_CODE,
				LEXPRESS_IMG, LEXPRESS_RSS);
		newsList.add(lexpressNews);

		News IonNews = new News(IONNEWS_NAME, IONNEWS_CODE, IONNEWS_RSS,
				IONNEWS_IMG);
		newsList.add(IonNews);

		News leMauricienNews = new News(LEMAURICIEN_NAME, LEMAURICIEN_CODE,
				LEMAURICIEN_IMG, LEMAURICIEN_SUBMENU);
		newsList.add(leMauricienNews);

		News defiQuotidienNews = new News(DEFI_QUOTIDIEN_NAME, DEFI_PLUS_CODE,
				DEFI_QUOTIDIEN_IMG, DEFI_QUOTIDIEN_SUBMENU);
		newsList.add(defiQuotidienNews);

		News leMatinalNews = new News(LE_MATINAL_NAME, lE_MATINAL_CODE,
				LE_MATINAL_RSS, LE_MATINAL_IMG);
		newsList.add(leMatinalNews);

		News cinqPlus = new News(CINQPLUS_NAME, CINQPLUS_CODE, CINQPLUS_RSS,
				CINQPLUS_IMG);
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
