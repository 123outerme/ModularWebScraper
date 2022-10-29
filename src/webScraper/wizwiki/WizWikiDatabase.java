package webScraper.wizwiki;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import webScraper.db.ScraperDatabase;

public class WizWikiDatabase extends ScraperDatabase<WizWikiData> {
	public WizWikiDatabase()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	public ResultSet getData(String url) throws SQLException
	{
		PreparedStatement stmt = this.conn.prepareStatement("");  //TODO
		return stmt.executeQuery();
	}

	public void submitData(WizWikiData data) throws SQLException
	{
		PreparedStatement stmt = this.conn.prepareStatement("");  //TODO
		stmt.executeQuery();
	}
}
