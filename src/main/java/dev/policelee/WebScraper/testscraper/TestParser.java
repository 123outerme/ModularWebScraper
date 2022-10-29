package dev.policelee.WebScraper.testscraper;

import org.jsoup.nodes.Document;

import dev.policelee.WebScraper.parser.WebParser;

public class TestParser extends WebParser<TestData> {

	@Override
	public TestData parseWebContent(Document webContent)
	{
		System.out.println(webContent.toString());
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNextURL(Document webContent)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
