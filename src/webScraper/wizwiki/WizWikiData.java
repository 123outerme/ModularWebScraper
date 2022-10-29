package webScraper.wizwiki;

import webScraper.db.WebData;

public class WizWikiData extends WebData
{
	public int gold;
	public int exp;
	public String otherRewards;
	public String location;
	public String previousQuest;
	public String nextQuest;
	
	public WizWikiData(String url, int gold, int exp, String otherRewards, String location, String previousQuest, String nextQuest)
	{
		super(url);
		this.gold = gold;
		this.exp = exp;
		this.otherRewards = otherRewards;
		this.location = location;
		this.previousQuest = previousQuest;
		this.nextQuest = nextQuest;
	}
}
