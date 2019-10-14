package es.fantasymanager.data.enums;

import es.fantasymanager.scheduler.jobs.AbstractCronJob;
import es.fantasymanager.scheduler.jobs.NewsParserJob;
import es.fantasymanager.scheduler.jobs.StatisticParserJob;
import es.fantasymanager.scheduler.jobs.TradeParserJob;
import es.fantasymanager.scheduler.jobs.TransactionParserJob;
import lombok.Getter;

@Getter
public enum JobsEnum {
	TRADE_PARSER("trade-parser", "Trade parser", TradeParserJob.class),
	STATISTIC_PARSER("statistic-parser", "Statistic parser", StatisticParserJob.class),
	NEWS_PARSER("news-parser", "News parser", NewsParserJob.class),
	TRANSACTION_PARSER("transaction-parser", "Transaction parser", TransactionParserJob.class);

	private String name;
	private String description;
	private Class<? extends AbstractCronJob> clazz;

	JobsEnum(String name, String description, Class<? extends AbstractCronJob> clazz) {
		this.name = name;
		this.description = description;
		this.clazz = clazz;
	}

	public static JobsEnum fromName(String name) {
		for (JobsEnum jobEnum : JobsEnum.values()) {
			if (jobEnum.name().equalsIgnoreCase(name)) {
				return jobEnum;
			}
		}
		return null;
	}
}
