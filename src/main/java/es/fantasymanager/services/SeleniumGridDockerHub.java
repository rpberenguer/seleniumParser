package es.fantasymanager.services;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
 
public class SeleniumGridDockerHub {
 
    //Declare ThreadLocal Driver (ThreadLocalMap) for ThreadSafe Tests
    protected static ThreadLocal<RemoteWebDriver> driver = new ThreadLocal<>();
 
   
    public void setupDriver (String browser) throws MalformedURLException {
        //Set DesiredCapabilities
        DesiredCapabilities capabilities = new DesiredCapabilities();
 
        //Set BrowserName
        capabilities.setCapability("browserName", browser);
        
        //Set Browser to ThreadLocalMap
        driver.set(new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), capabilities));
    }
 
 
    public void quitDriver() throws Exception {
        getDriver().quit();
    }
 
    public void removeDriver () {
        //Remove the ThreadLocalMap element
        driver.remove();
    }
    
    public WebDriver getDriver() {
    	//Get driver from ThreadLocalMap
    	return driver.get();
    }
}
