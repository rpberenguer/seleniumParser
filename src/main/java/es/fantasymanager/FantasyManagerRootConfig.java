package es.fantasymanager;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;

import com.innofis.omnichannel.ecorp.alertmanager.configuration.hystrix.shutdown.HystrixShutdownHandler;

/**
 * Basic root configuration that scans the Configuration classes defining
 * responsibilities context.
 */

@SpringBootApplication(scanBasePackages = "com.innofis.omnichannel")
public class FantasyManagerRootConfig extends SpringBootServletInitializer {

	public static void main(String[] args) throws Exception {
		ConfigurableApplicationContext cac = configureApplication(new SpringApplicationBuilder()).run(args);
		cac.addApplicationListener(new HystrixShutdownHandler());
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
