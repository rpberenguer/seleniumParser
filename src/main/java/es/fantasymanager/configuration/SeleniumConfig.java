package es.fantasymanager.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties("selenium")
@Data
public class SeleniumConfig {

	private String leagueId;
	private String urlRecentAtivity;

}