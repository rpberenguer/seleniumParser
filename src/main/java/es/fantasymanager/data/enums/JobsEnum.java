package es.fantasymanager.data.enums;

import lombok.Getter;

@Getter
public enum JobsEnum {
	TRADE_PARSER ("trade-parser", "Trade parser")
	, STATISTIC_PARSER ("statistic-parser", "Statistic parser")
	;

	private String name;
	private String description;

	JobsEnum(String name, String description) {
		this.name = name;
		this.description = description;
	}
}
