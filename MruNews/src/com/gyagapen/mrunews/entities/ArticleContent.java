package com.gyagapen.mrunews.entities;

import java.util.ArrayList;

import com.androidquery.AQuery;

public class ArticleContent {
	
	
	private String Title;
	private String Redactor;
	private String Content;
	private String HtmlContent;
	private String ImageLink;
	private String NewsName;
	private String commentsHtml;
	private String commentsIframeLink;
	private int commentCount;;

	
	public String getCommentsHtml() {
		return commentsHtml;
	}
	
	public void setCommentsHtml(String commentsHtml) {
		this.commentsHtml = commentsHtml;
	}
	
	public String getCommentsIframeLink() {
		return commentsIframeLink;
	}
	
	public void setCommentsIframeLink(String commentsIframeLink) {
		this.commentsIframeLink = commentsIframeLink;
	}
	

	public String getNewsName() {
		return NewsName;
	}

	public void setNewsName(String newsName) {
		NewsName = newsName;
	}
	
	public void setHtmlContent(String htmlContent) {
		HtmlContent = htmlContent;
	}
	
	public String getHtmlContent() {
		return HtmlContent;
	}

	public ArticleContent() {
		
	}
	
	public int getCommentCount() {
		return commentCount;
	}
	
	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}
	
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getRedactor() {
		return Redactor;
	}
	public void setRedactor(String redactor) {
		Redactor = redactor;
	}
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		
		//replace breakline by line separators
		content.replace("#BREAKLINE#", System.getProperty ("line.separator"));
		
		Content = content;
	}
	public String getImageLink() {
		return ImageLink;
	}
	public void setImageLink(String imageLink) {
		ImageLink = imageLink;
	}
	

	

}
