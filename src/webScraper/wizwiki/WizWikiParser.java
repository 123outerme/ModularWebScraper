package webScraper.wizwiki;

import org.jsoup.nodes.Document;

import webScraper.parser.WebParser;

public class WizWikiParser extends WebParser<WizWikiData> {

	public WizWikiData parseWebContent(Document webContent)
	{
		System.out.println(webContent.title());
		// TODO Auto-generated method stub
		return null;
	}

	public String getNextURL(Document webContent)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
