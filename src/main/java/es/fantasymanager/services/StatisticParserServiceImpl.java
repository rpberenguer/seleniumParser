package es.fantasymanager.services;

import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.fantasymanager.data.jms.GameJmsMessageData;
import es.fantasymanager.services.interfaces.StatisticParserService;
import es.fantasymanager.utils.Constants;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StatisticParserServiceImpl implements StatisticParserService, Constants {

	@Autowired
	private JmsTemplate jmsTemplate;

	@Override
	@Transactional
	public void getStatistics(LocalDate dateTimeFrom, final LocalDate dateTimeTo) throws MalformedURLException {

		log.info("Statistic Parser Started! " + Thread.currentThread().getId());

//		GameJmsMessageData message = new GameJmsMessageData();
//		message.setStartDate(dateTimeFrom);
//		message.setEndDate(dateTimeTo);
//
//		log.info("sending with convertAndSend() to queue <" + message + ">");
//		jmsTemplate.convertAndSend(GAME_QUEUE, message);

		// al ser los dos inclusive añadimos +1
		long daysBetween = ChronoUnit.DAYS.between(dateTimeFrom, dateTimeTo) + 1;
		int numOfWeeks = (int) (daysBetween / 7);
		LocalDate endDate = null;

		for (int i = 0; i < numOfWeeks; i++) {

			endDate = dateTimeFrom.plusDays(6);

			GameJmsMessageData message = new GameJmsMessageData();
			message.setStartDate(dateTimeFrom);

			if (endDate.isAfter(dateTimeTo)) {
				message.setEndDate(dateTimeTo);
			} else {
				message.setEndDate(endDate);
			}

			log.info("sending with convertAndSend() to queue <" + message + ">");
			jmsTemplate.convertAndSend(GAME_QUEUE, message);

			dateTimeFrom = endDate.plusDays(1);
		}

		if (null == endDate || endDate.isBefore(dateTimeTo)) {
			GameJmsMessageData message = new GameJmsMessageData();
			message.setStartDate(dateTimeFrom);
			message.setEndDate(dateTimeTo);

			log.info("sending with convertAndSend() to queue <" + message + ">");
			jmsTemplate.convertAndSend(GAME_QUEUE, message);
		}

		log.info("Statistic Parser Ended! " + Thread.currentThread().getId());
	}
}
