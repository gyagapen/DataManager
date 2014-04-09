package com.gyagapen.mrunews.entities;

import java.io.Serializable;

public class NewsSubEntry implements Serializable {
	
	private String subMenuName;
	private String rssFeed;
	private static final long serialVersionUID = 1L;
	
	public NewsSubEntry(String subMenuName, String rssFeed) {
	
		this.rssFeed = rssFeed;
		this.subMenuName = subMenuName;
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
	
	

}
