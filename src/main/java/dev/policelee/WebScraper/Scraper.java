package dev.policelee.WebScraper;

import java.sql.SQLException;

import org.jsoup.nodes.Document;

import dev.policelee.WebScraper.db.ScraperDatabase;
import dev.policelee.WebScraper.gatherer.WebGatherer;
import dev.policelee.WebScraper.parser.WebParser;

public class Scraper<T> extends Thread
{
	private String name;
	private int pageCount;
	
	private WebGatherer gatherer;
	private WebParser<T> parser;
	private ScraperDatabase<T> db;
	
	private String url;
	
	private long threadId;
	
	public Scraper(String name, WebGatherer gatherer, WebParser<T> parser, ScraperDatabase<T> db, String startUrl) {
		super();
		this.name = name;
		this.gatherer = gatherer;
		this.parser = parser;
		this.db = db;
		this.url = startUrl;
		this.pageCount = 0;
	}
	
	public void run()
	{
		this.threadId = Thread.currentThread().getId();
		//this.log("Scraper started");
		
		while(this.url != null)
		{	
			//String webContent = this.gatherer.downloadFromURL(this.url);
			Document webContent = this.gatherer.getDocument(this.url);
			
			T result = this.parser.parseWebContent(webContent);
			try
			{
				this.pageCount++;
				this.db.submitData(result);
				this.url = this.parser.getNextURL(webContent);
				//this.log("Stored result on previous URL.");
			}
			catch (SQLException e)
			{
				e.printStackTrace();
			}
			
			//this.log("Next url: " + this.url);
			
			//probably have some kind of delay to not set off DDOS prevention	
			if (this.url != null)
			{
				try
				{
					Thread.sleep(3000);  //sleep for this many milliseconds
					//this.log("Sleeping...");
				}
				catch (InterruptedException ie) 
				{
					//this.log("Sleep interrupted");
					Thread.currentThread().interrupt();
				}
			}
		}
		this.gatherer.closeDriver();
		this.db.close();
		this.interrupt();  //execution is over: given site has been scraped to the scraper's limits
	}
	
	private String log(String msg)
	{
		return "Thread ID " + this.threadId + ": " + msg;
	}
	
	public String printStatus()
	{
		return "Scraper \"" + this.name + "\" (ID " + this.threadId + "): " + this.pageCount + " page(s) accessed. Currently accessing \"" + this.url + "\".";
	}
	
	public String getScraperName()
	{
		return this.name;
	}
	
	public long getThreadId()
	{
		return this.threadId;
	}
}
