package com.gyagapen.mrunews.entities;

import java.util.ArrayList;

public class News {

	private String newsName;
	private String newsId;
	private ArrayList<String> newsRssFeeds;
	private int imageRessource;
	private ArrayList<NewsSubEntry> subEntries;

	public News(String newsName, String newsId, ArrayList<String> newsRssFeed, int imgRes) {
		super();
		this.newsName = newsName;
		this.newsId = newsId;
		this.newsRssFeeds = newsRssFeed;
		imageRessource = imgRes;
		subEntries = new ArrayList<NewsSubEntry>();
	}


	public News(String newsName, String newsId, int imgRes, ArrayList<NewsSubEntry> subEntries) {
		super();
		this.newsName = newsName;
		this.newsId = newsId;
		this.newsRssFeeds = new ArrayList<String>();
		imageRessource = imgRes;
		this.subEntries =subEntries;
	}
	
	public ArrayList<NewsSubEntry> getSubEntries() {
		return subEntries;
	}

	public void setSubEntries(ArrayList<NewsSubEntry> subEntries) {
		this.subEntries = subEntries;
	}

	public String getNewsName() {
		return newsName;
	}

	public void setNewsName(String newsName) {
		this.newsName = newsName;
	}

	public String getNewsId() {
		return newsId;
	}

	public void setNewsId(String newsId) {
		this.newsId = newsId;
	}

	public ArrayList<String> getNewsRssFeed() {
		return newsRssFeeds;
	}

	public void setNewsRssFeed(ArrayList<String> newsRssFeed) {
		this.newsRssFeeds = newsRssFeed;
	}

	public ArrayList<String> getNewsRssFeeds() {
		return newsRssFeeds;
	}

	public void setNewsRssFeeds(ArrayList<String> newsRssFeeds) {
		this.newsRssFeeds = newsRssFeeds;
	}

	public int getImageRessource() {
		return imageRessource;
	}

	public void setImageRessource(int imageRessource) {
		this.imageRessource = imageRessource;
	}

}
