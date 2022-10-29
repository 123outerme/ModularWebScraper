package webScraper;

import java.util.ArrayList;
import java.util.Scanner;

import webScraper.gatherer.WebGatherer;
import webScraper.wizwiki.WizWikiData;
import webScraper.wizwiki.WizWikiDatabase;
import webScraper.wizwiki.WizWikiParser;

public class Main
{
	public static void main(String args[])
	{
		Scanner input = new Scanner(System.in);
		boolean quit = false;
		ArrayList<Scraper<?>> webScrapers = new ArrayList<Scraper<?>>();
		
		System.out.println("Welcome to the Stevo Web Scraper!");
		
		
		while(!quit)
		{
			String command = input.nextLine();
			
			if (command.equals("/quit"))
				quit = true;
			
			if (command.equals("/halt"))
			{
				for(int i = 0; i < webScrapers.size(); i++)
					webScrapers.get(i).interrupt();
				
				System.out.println("Halted " + webScrapers.size() + " scraper threads.");
				webScrapers.clear();
			}
			
			if (command.equals("/start wizWiki"))
			{
				System.out.print("URL = ");
				String url = input.next();
				
				//create webscraper thread
				WebGatherer gatherer = new WebGatherer();
				WizWikiParser parser = new WizWikiParser();
				WizWikiDatabase db = new WizWikiDatabase();
				
				Scraper<WizWikiData> s = new Scraper<WizWikiData>(gatherer, parser, db, url);
				
				s.start();
				
				webScrapers.add(s);
				
				System.out.println("Started Wizard101 Wiki Scraper.");
				//https://www.wizard101central.com/wiki/Quest:Tutorial
			}
		}
		
		input.close();
		System.out.println("Goodbye!");
	}
}
