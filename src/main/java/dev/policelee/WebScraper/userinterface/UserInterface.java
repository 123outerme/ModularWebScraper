package dev.policelee.WebScraper.userinterface;

public abstract class UserInterface {
	
	public abstract String getCommand();
	public abstract void sendResponse(String response);
}
