package dev.policelee.WebScraper.testscraper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dev.policelee.WebScraper.db.ScraperDatabase;

public class TestDatabase extends ScraperDatabase<TestData> {
	public TestDatabase()
	{
		super(null);
		// TODO Auto-generated constructor stub
	}

	public ResultSet getData(String url) throws SQLException
	{
		/*
		PreparedStatement stmt = this.conn.prepareStatement("");  //TODO
		return stmt.executeQuery();
		//*/
		return null;
	}

	public void submitData(TestData data) throws SQLException
	{
		/*
		PreparedStatement stmt = this.conn.prepareStatement("");  //TODO
		stmt.executeQuery();
		//*/
	}

	@Override
	protected void createDatabase() throws SQLException
	{
		//nothing
	}
}