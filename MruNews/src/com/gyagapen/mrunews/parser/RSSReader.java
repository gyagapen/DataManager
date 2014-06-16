package com.gyagapen.mrunews.parser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.xml.sax.SAXException;

import nl.matshofman.saxrssreader.RssFeed;
import nl.matshofman.saxrssreader.RssItem;
import nl.matshofman.saxrssreader.RssReader;

import com.gyagapen.mrunews.entities.ArticleHeader;

public class RSSReader {

	public ArrayList<ArticleHeader> readFeed(ArrayList<String> feedAdresses,
			String feedTag, boolean useCache) throws SAXException, IOException{

		//date formatter
		SimpleDateFormat dateFormatter=new SimpleDateFormat("dd/MM/yyyy");

		
		ArrayList<ArticleHeader> articles = new ArrayList<ArticleHeader>();

		for (int i = 0; i < feedAdresses.size(); i++) {

			String feedAdress = feedAdresses.get(i);

			URL url = new URL(feedAdress);
			RssFeed feed = RssReader.read(url);
			
			ArrayList<RssItem> rssItems = feed.getRssItems();	
			
			for(RssItem rssItem : rssItems) {
				
				
				ArticleHeader articleHeader = new ArticleHeader();
				articleHeader.setId(feedTag);
				articleHeader.setTitle(rssItem.getTitle());
				articleHeader.setDescription(rssItem.getDescription());
				articleHeader.setLink(rssItem.getLink());
				
				Date pubDate = rssItem.getPubDate();
				if (pubDate != null)
				{
					articleHeader.setPublishedDate(dateFormatter.format(pubDate));	
				}
				else
				{
					articleHeader.setPublishedDate("");
				}

					articles.add(articleHeader);
			}
			
//			URLConnection httpcon = (URLConnection) url
//					.openConnection();
//			
//			//force network response
//			if(!useCache)
//			{
//				httpcon.addRequestProperty("Cache-Control", "no-cache");
//			}
//			// Reading the feed
//			SyndFeedInput input = new SyndFeedInput();
//			SyndFeed feed = null;
//			try
//			{
//				XmlReader xmlReader = new XmlReader(httpcon);
//				feed = input.build(xmlReader);
//			}
//			catch (Exception e)
//			{
//				 httpcon = (HttpURLConnection) url
//							.openConnection();
//				 XmlReader xmlReader = new XmlReader(httpcon);
//				 feed = input.build(xmlReader);
//			}
//			List entries = feed.getEntries();
//			Iterator itEntries = entries.iterator();
//
//			int countArticle = 0;
//			while (itEntries.hasNext()) {
//				SyndEntry entry = (SyndEntry) itEntries.next();
//				Log.i("test read", entry.getTitle());
//
//				// build Article Header
//				ArticleHeader articleHeader = new ArticleHeader();
//				articleHeader.setId(feedTag);
//				articleHeader.setTitle(entry.getTitle());
//				articleHeader.setDescription(entry.getDescription().toString());
//				articleHeader.setLink(entry.getLink());
//				
//				Date pubDate = entry.getPublishedDate();
//				if (pubDate != null)
//				{
//					articleHeader.setPublishedDate(dateFormatter.format(pubDate));	
//				}
//				else
//				{
//					articleHeader.setPublishedDate("");
//				}
//
//				articles.add(articleHeader);
//			}
			
			//httpcon.disconnect();
		}
		
		

		return articles;
	}
}