package webScraper;

import java.sql.SQLException;

import org.jsoup.nodes.Document;

import webScraper.db.ScraperDatabase;
import webScraper.gatherer.WebGatherer;
import webScraper.parser.WebParser;

public class Scraper<T> extends Thread
{
	private WebGatherer gatherer;
	private WebParser<T> parser;
	private ScraperDatabase<T> db;
	
	private String url;
	
	private long threadId;
	
	public Scraper(WebGatherer gatherer, WebParser<T> parser, ScraperDatabase<T> db, String startUrl) {
		super();
		this.gatherer = gatherer;
		this.parser = parser;
		this.db = db;
		this.url = startUrl;
	}
	
	public void run()
	{
		this.threadId = Thread.currentThread().getId();
		this.log("Scraper started");
		
		while(this.url != null)
		{	
			//String webContent = this.gatherer.downloadFromURL(this.url);
			Document webContent = this.gatherer.getDocument(this.url);
			
			T result = this.parser.parseWebContent(webContent);
			try
			{
				this.db.submitData(result);
				this.url = this.parser.getNextURL(webContent);
				this.log("Stored result on previous URL.");
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
			
			this.log("Next url: " + this.url);
			
			//probably have some kind of delay to not set off DDOS prevention	
			try
			{
				Thread.sleep(3000);  //sleep for this many milliseconds
				this.log("Sleeping...");
			}
			catch (InterruptedException ie) 
			{
				this.log("Sleep interrupted");
				Thread.currentThread().interrupt();
			}
		}
		this.notify();  //execution is over: given site has been scraped to the scraper's limits
	}
	
	private void log(String msg)
	{
		System.out.println("Thread ID " + this.threadId + ": " + msg);
	}
}
