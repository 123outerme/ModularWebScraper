package webScraper.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class ScraperDatabase<T>
{
	protected Connection conn;
	protected ScraperDatabase()
	{
		//TODO connect to a SQL database
	}
	
	
	public abstract void submitData(T data) throws SQLException;
	
	public abstract ResultSet getData(String sqlQuery) throws SQLException;
}
