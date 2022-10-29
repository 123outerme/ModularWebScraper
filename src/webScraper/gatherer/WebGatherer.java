package webScraper.gatherer;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class WebGatherer
{
	private boolean useHeadlessBrowser;
	
	public WebGatherer()
	{
		this.useHeadlessBrowser = false;
	}
	
	public Document downloadFromURL(String url)
	{
		return null;  //use headless browser or GET request
	}
	
	public Document getDocument(String url)
	{
		if (this.useHeadlessBrowser)
		{
			return this.downloadFromURL(url);
		}
		
		Document doc;
		try
		{
			doc = Jsoup.connect(url).get();
		} 
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.useHeadlessBrowser = true; //attempt using a headless browser
			return this.downloadFromURL(url);
		}
		return doc;
	}
	
}
