package es.fantasymanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Basic root configuration that scans the Configuration classes defining
 * responsibilities context.
 */

//@SpringBootApplication(scanBasePackages = "es.fantasymanager")
@SpringBootApplication
@EnableJpaRepositories
public class FantasyManagerRootConfig /*extends SpringBootServletInitializer*/ {

	static {
		System.setProperty("webdriver.chrome.driver", "E:\\webdrivers\\chromedriver.exe");
	}

	public static void main(String[] args) {
		SpringApplication.run(FantasyManagerRootConfig.class, args);
	}

//	@Override
//	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//		return application.sources(FantasyManagerRootConfig.class);
//	}
}
