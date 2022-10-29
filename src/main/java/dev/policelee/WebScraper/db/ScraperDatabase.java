package dev.policelee.WebScraper.db;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public abstract class ScraperDatabase<T>
{
	
	protected String propertiesFilepath;
	protected Connection conn;
	
	protected ScraperDatabase(String propertiesFile)
	{
		this.propertiesFilepath = propertiesFile;
		this.connectToSQLServer();
	}
	
	protected void connectToSQLServer()
	{
		File f = new File(this.propertiesFilepath);
		Scanner fileScanner;
		try
		{
			fileScanner = new Scanner(f);
			String dbUrl = fileScanner.nextLine();
			String username = fileScanner.nextLine();
			String password = fileScanner.nextLine();
			
			fileScanner.close();
			
			this.conn = DriverManager.getConnection(dbUrl, username, password);
		}
		catch (FileNotFoundException e1)
		{
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public void close()
	{
		try
		{
			this.conn.close();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	protected abstract void createDatabase() throws SQLException;
	
	public abstract void submitData(T data) throws SQLException;
	
	public abstract ResultSet getData(String url) throws SQLException;
	
	public ResultSet doCustomQuery(String sqlQuery) throws SQLException
	{
		PreparedStatement stmt = this.conn.prepareStatement(sqlQuery);
		return stmt.executeQuery();
	}
}
