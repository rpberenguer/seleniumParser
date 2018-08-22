package es.fantasymanager;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;


/**
 * Basic root configuration that scans the Configuration classes defining
 * responsibilities context.
 */

@SpringBootApplication(scanBasePackages = "es.fantasymanager")
public class FantasyManagerRootConfig extends SpringBootServletInitializer {

	public static void main(String[] args) throws Exception {
		configureApplication(new SpringApplicationBuilder()).run(args);
	}

	private static SpringApplicationBuilder configureApplication(SpringApplicationBuilder builder) {
		builder.sources(FantasyManagerRootConfig.class);
		return builder;
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return configureApplication(builder);
	}
}
