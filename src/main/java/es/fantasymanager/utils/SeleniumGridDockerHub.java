package es.fantasymanager.utils;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.stereotype.Service;

@Service
public class SeleniumGridDockerHub {

	//Declare ThreadLocal Driver (ThreadLocalMap) for ThreadSafe Tests
	protected static ThreadLocal<WebDriver> driver = new ThreadLocal<>();


	public void setupDriver (String browser) throws MalformedURLException {
		//Set DesiredCapabilities
		DesiredCapabilities capabilities = new DesiredCapabilities();

		//Set BrowserName
		capabilities.setCapability("browserName", browser);


		//Set Browser to ThreadLocalMap
		driver.set(new RemoteWebDriver(new URL("http://192.168.99.100:32508/wd/hub"), capabilities));
//		driver.set(new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), capabilities));
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
