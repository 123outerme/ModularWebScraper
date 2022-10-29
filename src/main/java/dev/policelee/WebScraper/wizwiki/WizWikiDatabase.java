package dev.policelee.WebScraper.wizwiki;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dev.policelee.WebScraper.db.ScraperDatabase;

public class WizWikiDatabase extends ScraperDatabase<WizWikiData> {
	public WizWikiDatabase()
	{
		super(System.getProperty("user.dir") + "/wizWiki.properties");
		// TODO Auto-generated constructor stub
		
		try
		{
			this.createDatabase();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	protected void createDatabase() throws SQLException
	{
		PreparedStatement stmt = this.conn.prepareStatement("CREATE TABLE IF NOT EXISTS wiz_wiki("
				+ "url VARCHAR(256), title VARCHAR(256), gold INT, exp INT, other_rewards VARCHAR(256), location VARCHAR(256), prev_quest VARCHAR(256), next_quest VARCHAR(256), PRIMARY KEY(title)");
		stmt.executeQuery();
	}

	public ResultSet getData(String url) throws SQLException
	{
		PreparedStatement stmt = this.conn.prepareStatement("SELECT * FROM wiz_wiki WHERE url=...");  //TODO
		return stmt.executeQuery();
	}
	
	public ResultSet getDataByTitle(String title) throws SQLException
	{
		PreparedStatement stmt = this.conn.prepareStatement("SELECT * FROM wiz_wiki WHERE title=...");  //TODO
		return stmt.executeQuery();
	}

	public void submitData(WizWikiData data) throws SQLException
	{
		/*
		PreparedStatement stmt = this.conn.prepareStatement("INSERT INTO wiz_wiki VALuES()");  //TODO
		stmt.executeQuery();
		//*/
	}
}
