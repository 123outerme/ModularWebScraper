package dev.policelee.WebScraper.wizwiki;

import dev.policelee.WebScraper.db.WebData;

public class WizWikiData extends WebData
{
	public String title;
	public int gold;
	public int exp;
	public String otherRewards;
	public String location;
	public String previousQuest;
	public String nextQuest;
	
	public WizWikiData(String url, String title, int gold, int exp, String otherRewards, String location, String previousQuest, String nextQuest)
	{
		super(url);
		this.title = title;
		this.gold = gold;
		this.exp = exp;
		this.otherRewards = otherRewards;
		this.location = location;
		this.previousQuest = previousQuest;
		this.nextQuest = nextQuest;
	}
	
	@Override
	public String toString()
	{
		return "Quest \"" + title + "\" (" + gold + " gold, " + exp + " exp, other rewards: \"" + otherRewards
				+ "\", location: \"" + location + "\", previous quest \"" + previousQuest + "\", next quest \"" + nextQuest + "\")";
	}
}
