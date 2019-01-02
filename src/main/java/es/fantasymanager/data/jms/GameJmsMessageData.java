package es.fantasymanager.data.jms;

import java.time.LocalDate;

import lombok.Data;

@Data
public class GameJmsMessageData {

	private LocalDate startDate;
	private LocalDate endDate;
}
