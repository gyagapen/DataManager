package com.gyagapen.mrunews.entities;

public class ArticleHeader {
	
	
	private String Id;
	private String Title;
	private String Description;
	private String Link;
	private String ImageLink;
	public String getImageLink() {
		return ImageLink;
	}

	public void setImageLink(String imageLink) {
		ImageLink = imageLink;
	}

	private String PublishedDate;
	
	public ArticleHeader() {

	}
	
	public ArticleHeader(String id,String title, String description, String link, String publishedDate) {
		super();
		Id = id;
		Title = title;
		Description = description;
		Link = link;
		PublishedDate = publishedDate;
	}

	
	
	public String getId() {
		return Id;
	}



	public void setId(String id) {
		Id = id;
	}



	public String getPublishedDate() {
		return PublishedDate;
	}

	public void setPublishedDate(String publishedDate) {
		PublishedDate = publishedDate;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getLink() {
		return Link;
	}

	public void setLink(String link) {
		Link = link;
	}
	
	
	
	

}
