package dev.policelee.WebScraper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import dev.policelee.WebScraper.gatherer.WebGatherer;
import dev.policelee.WebScraper.testscraper.TestData;
import dev.policelee.WebScraper.testscraper.TestDatabase;
import dev.policelee.WebScraper.testscraper.TestParser;
import dev.policelee.WebScraper.userinterface.CommandLineInterface;
import dev.policelee.WebScraper.userinterface.UserInterface;
import dev.policelee.WebScraper.wizwiki.WizWikiData;
import dev.policelee.WebScraper.wizwiki.WizWikiDatabase;
import dev.policelee.WebScraper.wizwiki.WizWikiParser;

public class Main
{
	public static ArrayList<Scraper<?>> webScrapers;
	public static boolean quit = false;
	public static UserInterface ui;
	
	public static void main(String args[])
	{
		ui = new CommandLineInterface();
		webScrapers = new ArrayList<Scraper<?>>();
		
		ui.sendResponse("Welcome to the Stevo Web Scraper!");
		
		
		while(!quit)
		{
			System.out.print("/");
			String command = ui.getCommand();
			
			parseCommand(command);
		}
		
		ui.sendResponse("Goodbye!");
	}
	
	public static void parseCommand(String command)
	{
		String[] pieces = command.split(" ");
		
		//quit, exit
		if (command.equals("quit") || command.equals("exit") || command.equals("q"))
			quit = true;
		
		//halt, stop, quit/exit with running threads
		if (command.equals("halt") || command.equals("stop") || (quit && webScrapers.size() > 0))
		{
			for(int i = 0; i < webScrapers.size(); i++)
				webScrapers.get(i).interrupt();
			
			if (webScrapers.size() > 0)
				ui.sendResponse("Halted " + webScrapers.size() + " scraper thread(s).");
			else
				ui.sendResponse("No scrapers were running.");
			webScrapers.clear();
		}
		
		//halt <ID>
		if (command.matches("halt [0-9]+"))
		{
			//System.out.println(command.split("halt ")[1]);
			Long targetId = Long.parseLong(command.split("halt ")[1]);
			boolean found = false;
			
			for(int i = 0; i < webScrapers.size(); i++)
			{
				Scraper<?> s = webScrapers.get(i);
				if (s.getThreadId() == targetId)
				{
					s.interrupt();
					found = true;
					ui.sendResponse("Halted Scraper \"" + s.getScraperName() + "\" (ID " + s.getThreadId() + ").");
					webScrapers.remove(i);
				}
			}
			
			if (!found)
				ui.sendResponse("No scraper found with that ID.");
		}
		
		//status
		if (command.equals("status"))
		{
			if (webScrapers.size() > 0)
			{
				ui.sendResponse("Scraper status(es): ");
			
				for(int i = 0; i < webScrapers.size(); i++)
				{
					ui.sendResponse(webScrapers.get(i).printStatus());
				}
			}
			else
				System.out.println("No scrapers currently running.");
		}
		
		if (command.equals("help"))
		{
			ui.sendResponse("Commands:\n" + 
							"quit | exit | q\n" +
							"halt [#] | stop\n" +
							"status\n" +
							"wizWiki [command]\n" +
							"test start\n" +
							"help");
		}
		
		//working with the Wiz101 wiki scraper
		if (command.startsWith("wizWiki"))
		{
			//start the wiki scraper
			if (pieces.length == 1)
			{
				ui.sendResponse("Commands:\n" + 
								"wizWiki start <quest title>\n" + 
								"wizWiki query <custom|url|quest> <parameter>\n" + 
								"\tcustom: parameter = SQL query\n" +
								"\turl: parameter = full URL\n" +
								"\tquest: parameter = quest name");
			}
			else
			{
				WizWikiDatabase db = new WizWikiDatabase();
				
			
				if (pieces[1].equals("start") && pieces.length >= 4)
				{
					String quest = "";
					
					for(int i = 2; i < pieces.length; i++)
						quest += pieces[i];
					
					String url = "https://www.wizard101central.com/wiki/Quest:" + quest;
					
					//create webscraper thread
					WebGatherer gatherer = new WebGatherer(true);
					WizWikiParser parser = new WizWikiParser();
					
					Scraper<WizWikiData> s = new Scraper<WizWikiData>("Wiz Wiki", gatherer, parser, db, url);
					
					s.start();
					
					webScrapers.add(s);
					
					ui.sendResponse("Started Wizard101 Wiki Scraper.");
					//Tutorial
				}
				
				if (pieces[1].equals("query") && pieces.length >= 3)
				{
					if (pieces[2].equals("custom"))
					{
						String query = "";
						for(int i = 3; i < pieces.length; i++)
							query += pieces[i];
						try
						{
							ResultSet rs = db.doCustomQuery(query);
							
							//https://stackoverflow.com/questions/24229442/print-the-data-in-resultset-along-with-column-names
							ResultSetMetaData rsmd = rs.getMetaData();
							int columnsNumber = rsmd.getColumnCount();
							String response = "";
							while (rs.next())
							{
							    for (int i = 1; i <= columnsNumber; i++)
							    {
							        if (i > 1)
							        	response += (",  ");
							        String columnValue = rs.getString(i);
							        response += columnValue + " " + rsmd.getColumnName(i);
							    }
							    response += "\n";
							}
							ui.sendResponse(response);
							
						}
						catch (SQLException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					if (pieces[2].equals("url"))
					{
						try
						{
							String query = "";
							for(int i = 3; i < pieces.length; i++)
								query += pieces[i];
							ResultSet rs = db.getData(query);
							
							//parse data and present as WizWikiData object
							WizWikiData data = new WizWikiData(rs.getString("url"), rs.getString("title"),
									rs.getInt("gold"), rs.getInt("exp"), rs.getString("other_rewards"), rs.getString("location"),
									rs.getString("prev_quest"), rs.getString("next_quest"));
							
							ui.sendResponse(data.toString());
							
						}
						catch (SQLException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
					if (pieces[2].equals("quest"))
					{
						try
						{
							String query = "";
							for(int i = 3; i < pieces.length; i++)
								query += pieces[i];
							ResultSet rs = db.getDataByTitle(query);
							
							//parse data and present as WizWikiData object
							WizWikiData data = new WizWikiData(rs.getString("url"), rs.getString("title"),
									rs.getInt("gold"), rs.getInt("exp"), rs.getString("other_rewards"), rs.getString("location"),
									rs.getString("prev_quest"), rs.getString("next_quest"));
							
							ui.sendResponse(data.toString());
						}
						catch (SQLException e)
						{
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		}
		
		if (command.startsWith("test"))
		{
			//start test
			if (pieces[1].equals("start"))
			{
				WebGatherer gatherer = new WebGatherer();
				TestParser parser = new TestParser();
				TestDatabase db = new TestDatabase();
				
				Scraper<TestData> s = new Scraper<TestData>("Test", gatherer, parser, db, "http://example.org/");
				
				s.start();
				
				webScrapers.add(s);
				
				ui.sendResponse("Started Test webscraper.");
			}
		}
	}
}
