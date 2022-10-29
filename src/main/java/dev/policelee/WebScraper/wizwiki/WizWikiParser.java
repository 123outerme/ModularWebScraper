package dev.policelee.WebScraper.wizwiki;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import dev.policelee.WebScraper.parser.WebParser;

public class WizWikiParser extends WebParser<WizWikiData> {

	public WizWikiData parseWebContent(Document webContent)
	{
		if (webContent == null)
			return null;
		
		//System.out.println(webContent.getElementsByClass("firstHeading").get(0).wholeText());  //quest title in form "Quest:<title>"
		String sourceUrl = webContent.baseUri();
		String questTitle = webContent.getElementsByClass("firstHeading").get(0).wholeText().split("Quest:")[1];
		
		int gold = 0;
		
		Elements goldElements = webContent.getElementsByAttributeValueMatching("href", "\\S*wiki/File:\\(Icon\\)_Gold\\S*");
		if (goldElements.size() > 0)
		{
			//System.out.println(goldElements.get(0).html());
			Element goldElement = goldElements.get(0).parent().parent().parent().parent().child(1).firstElementChild().firstElementChild();
			gold = Integer.parseInt(goldElement.text());
		}
		
		int exp = 0;
		Elements expElements = webContent.getElementsByAttributeValueMatching("href", "\\S*wiki/File:\\(Icon\\)_XP\\S*");
		if (expElements.size() > 0)
		{
			//System.out.println(expElements.get(0).html());
			Element expElement = expElements.get(0).parent().parent().parent().parent().child(1).firstElementChild().firstElementChild();
			exp = Integer.parseInt(expElement.text());
		}
		
		//TODO:
		String otherRewards = null;
		
		String location = null;
		Elements locations = webContent.getElementsMatchingOwnText("Giver Location:");
		if (locations.size() > 0)
		{
			Elements locationElements = locations.get(0).parent().parent().getElementsByTag("a");
			
			if (locationElements.size() > 0)
				location = locationElements.get(0).text();
		}
		
		//Previous quest name
		String previousQuest = null;
		Elements previousLinks = webContent.getElementsMatchingOwnText("Prequest:");
		if (previousLinks.size() > 0)
		{
			Element previousLink = previousLinks.get(0);
			//System.out.println(previousLink.outerHtml());
			
			previousLink = previousLink.nextElementSibling().firstElementChild().firstElementChild();
			previousQuest = previousLink.text();
		}
		
		//Next quest name
		String nextQuest = null;
		Elements nextLinks = webContent.getElementsMatchingOwnText("Leads to:");
		if (nextLinks.size() > 0)
		{
			Element nextLink = nextLinks.get(0);
			//System.out.println(nextLink.outerHtml());
			
			nextLink = nextLink.parent().nextElementSibling().firstElementChild().firstElementChild();
			nextQuest = nextLink.text();
		}
		
		
		//System.out.println(webContent.getElementById("Quest_Information").parent().parent().wholeText());  //body of quest information to parse
		
		WizWikiData pageData = new WizWikiData(sourceUrl, questTitle, gold, exp, otherRewards, location, previousQuest, nextQuest);
		
		System.out.println(pageData.toString());
		
		return null;
	}

	public String getNextURL(Document webContent)
	{
		if (webContent == null)
			return null;
		
		//Elements nextLinks = webContent.getElementsByAttributeValueMatching("href", "\\S*wiki/Quest:\\S+");
		Elements nextLinks = webContent.getElementsMatchingOwnText("Leads to:");
		
		String nextUrl = null;
		if (nextLinks.size() > 0)
		{
			Element nextLink = nextLinks.get(0);
			//System.out.println(nextLink.outerHtml());
			
			nextLink = nextLink.parent().nextElementSibling().firstElementChild().firstElementChild();
			
			//System.out.println(nextLink.outerHtml() + " " + nextLink.absUrl("href") + " " + nextLink.attr("href") + " " + nextLink.attr("abs:href"));
			nextUrl = nextLink.absUrl("href");
		}
		
		//System.out.println("Next url: \"" + nextUrl + "\"");
		
		return nextUrl;
	}

}
