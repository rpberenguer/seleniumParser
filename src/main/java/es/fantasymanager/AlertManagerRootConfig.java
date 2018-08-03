package es.fantasymanager;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.innofis.omnichannel.ecorp.alertmanager.configuration.hystrix.shutdown.HystrixShutdownHandler;
import com.innofis.omnichannel.ecorp.alertmanager.configuration.ribbon.RibbonConfiguration;

/**
 * Basic root configuration that scans the Configuration classes defining
 * responsibilities context.
 */

@SpringBootApplication(scanBasePackages = "com.innofis.omnichannel")
@EnableDiscoveryClient
@EnableCircuitBreaker
@EnableJpaAuditing
@RibbonClient(name = "${spring.application.name}", configuration = RibbonConfiguration.class)
public class AlertManagerRootConfig extends SpringBootServletInitializer {

	public static void main(String[] args) throws Exception {
		ConfigurableApplicationContext cac = configureApplication(new SpringApplicationBuilder()).run(args);
		cac.addApplicationListener(new HystrixShutdownHandler());
	}

	private static SpringApplicationBuilder configureApplication(SpringApplicationBuilder builder) {
		builder.sources(AlertManagerRootConfig.class);
		return builder;
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return configureApplication(builder);
	}
}
