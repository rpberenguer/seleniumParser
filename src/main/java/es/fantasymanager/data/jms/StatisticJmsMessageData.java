package es.fantasymanager.data.jms;

import java.util.List;

import lombok.Data;

@Data
public class StatisticJmsMessageData {

	private List<String> gameIds;
}
