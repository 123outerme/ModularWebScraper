package webScraper.parser;

import org.jsoup.nodes.Document;

public abstract class WebParser<T>
{
	public abstract T parseWebContent(Document webContent);
	public abstract String getNextURL(Document webContent);
}
