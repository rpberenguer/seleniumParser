package com.parser.selenium;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
 
public class TestBase {
 
    //Declare ThreadLocal Driver (ThreadLocalMap) for ThreadSafe Tests
    protected static ThreadLocal<RemoteWebDriver> driver = new ThreadLocal<>();
//    private String playerToAdd;
//	private String playerToRemove;
 
    @BeforeMethod
    @Parameters(value={"browser"})
    public void setupTest (String browser) throws MalformedURLException {
        //Set DesiredCapabilities
        DesiredCapabilities capabilities = new DesiredCapabilities();
 
        //Set BrowserName
        capabilities.setCapability("browserName", browser);
        
        //Set Browser to ThreadLocalMap
        driver.set(new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), capabilities));
        
        // Set the players
//        this.playerToAdd = playerToAdd;
//        this.playerToRemove = playerToRemove;
    }
 
 
    @AfterMethod
    public void tearDown() throws Exception {
        getDriver().quit();
    }
 
    @AfterClass 
    public void terminate () {
        //Remove the ThreadLocalMap element
        driver.remove();
    }
    
    public WebDriver getDriver() {
    	//Get driver from ThreadLocalMap
    	return driver.get();
    }

//	public String getPlayerToAdd() {
//		return playerToAdd;
//	}
//
//	public String getPlayerToRemove() {
//		return playerToRemove;
//	}
}
