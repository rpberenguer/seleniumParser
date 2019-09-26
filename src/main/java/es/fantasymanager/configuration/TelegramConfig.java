package es.fantasymanager.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties("telegram")
@Data
public class TelegramConfig {

	private String newsChatId;
	private String transactionsChatId;
	private String tradesChatId;

}