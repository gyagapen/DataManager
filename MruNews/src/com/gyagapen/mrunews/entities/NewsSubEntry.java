package com.gyagapen.mrunews.entities;

import java.io.Serializable;

import com.gyagapen.mrunews.R;

public class NewsSubEntry implements Serializable {
	
	private String subMenuName;
	private String rssFeed;
	private int imageResource = R.drawable.actualites;
	private static final long serialVersionUID = 1L;
	//not rss feed, to parse page
	private boolean isNotRssFeed;
	private String notRssParseCode;
	
	public NewsSubEntry(String subMenuName, String rssFeed) {
	
		this.rssFeed = rssFeed;
		this.subMenuName = subMenuName;
		this.isNotRssFeed = false;
		this.notRssParseCode= ""; 
	}
	
	public NewsSubEntry(String subMenuName, String rssFeed, int imageResource, boolean isNotRssFeed, String notRssParseCode) {
		
		this.rssFeed = rssFeed;
		this.subMenuName = subMenuName;
		this.imageResource = imageResource;
		this.isNotRssFeed = isNotRssFeed;
		this.notRssParseCode= notRssParseCode;
	}
	
	
	public NewsSubEntry(String subMenuName, String rssFeed, int imageResource) {
		
		this.rssFeed = rssFeed;
		this.subMenuName = subMenuName;
		this.imageResource = imageResource;
		this.isNotRssFeed = false;
		this.notRssParseCode= ""; 
	}

	public String getSubMenuName() {
		return subMenuName;
	}

	public void setSubMenuName(String subMenuName) {
		this.subMenuName = subMenuName;
	}

	public String getRssFeed() {
		return rssFeed;
	}

	public void setRssFeed(String rssFeed) {
		this.rssFeed = rssFeed;
	}
	
	public void setImageResource(int imageResource) {
		this.imageResource = imageResource;
	}
	
	public int getImageResource() {
		return imageResource;
	}  
	
	public boolean isNotRssFeed() {
		return isNotRssFeed;
	}
	
	public String getNotRssParseCode() {
		return notRssParseCode;
	}
	

}
