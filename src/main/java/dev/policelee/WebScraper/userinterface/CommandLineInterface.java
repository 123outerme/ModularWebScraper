package dev.policelee.WebScraper.userinterface;

import java.util.Scanner;

public class CommandLineInterface extends UserInterface {

	Scanner input;
	
	public CommandLineInterface()
	{
		input = new Scanner(System.in);
	}
	
	@Override
	public String getCommand()
	{
		return input.nextLine();
	}

	@Override
	public void sendResponse(String response)
	{
		// TODO Auto-generated method stub
		System.out.println(response);
	}

}
