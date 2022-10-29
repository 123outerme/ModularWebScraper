package dev.policelee.WebScraper.gatherer;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.firefox.FirefoxDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class WebGatherer
{
	private boolean useHeadlessBrowser;
	
	private FirefoxDriver driver;
	private boolean isDriverInitted;
	
	private HttpClient client;
	
	public WebGatherer()
	{
		this.useHeadlessBrowser = false;
		setup();
	}
	
	public WebGatherer(boolean useWorkaround)
	{
		this.useHeadlessBrowser = useWorkaround;
		setup();
	}
	
	private void setup()
	{
		this.client = HttpClient.newHttpClient();
		this.isDriverInitted = false;
	}
	
	public Document downloadFromURL(String url) throws IOException, InterruptedException
	{
		/*alternate
		driver.get(url);
		driver.manage().timeouts().implicitlyWait(Duration.ofMillis(500));
		String htmlText = driver.findElement(By.tagName("body")).getText();
		return Jsoup.parse(htmlText);  //use headless browser or GET request
		//*/
		
		String API_KEY = "6fcd715b-2f81-4eb2-aedc-6714945a26e8";
		
		String requestEndpoint = url; //URLEncoder.encode(url, StandardCharsets.UTF_8.toString());
		//System.out.println(requestEndpoint);
		
		HttpRequest request = HttpRequest.newBuilder(URI.create("https://proxy.scrapeops.io/v1/?api_key=" + API_KEY
																	+ "&url=" + requestEndpoint))
								.build();
		
		HttpResponse<String> response = this.client.send(request, HttpResponse.BodyHandlers.ofString());
		//System.out.println(response.body());
		Document d = Jsoup.parse(response.body());
		d.setBaseUri(url);
		return d;
	}
	
	public Document getDocument(String url)
	{
		if (this.useHeadlessBrowser)
		{
			try
			{
				return this.downloadFromURL(url);
			}
			catch(Exception e)
			{
				System.err.println(e);
				return null;
			}
		}
		
		Document doc;
		try
		{
			doc = Jsoup.connect(url).get();
		} 
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			this.useHeadlessBrowser = true; //attempt using a headless browser
			try
			{
				return this.downloadFromURL(url);
			}
			catch(Exception secondE)
			{
				System.err.println(e);
				return null;
			}
		}
		return doc;
	}
	
	private void initializeDriver()
	{
		if (!this.isDriverInitted)
		{
			WebDriverManager.firefoxdriver().setup();
			this.driver = new FirefoxDriver();
			this.isDriverInitted = true;
		}
	}
	
	public void closeDriver()
	{
		if (this.isDriverInitted)
		{
			this.driver.close();
			this.isDriverInitted = false;
		}
	}
	
}
