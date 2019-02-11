package es.fantasymanager.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.quartz.JobDataMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import es.fantasymanager.data.business.TradeData;
import es.fantasymanager.data.rest.request.BaseSheduledCronJobRequest;
import es.fantasymanager.data.rest.request.TradeRequest;

@RestController
public class TradeSchedulerController extends SchedulerController {

	@PostMapping("/trade")
	public void scheduleTrade(@Valid @RequestBody TradeRequest tradeRequest) {
		super.schedule(tradeRequest);
	}

	@Override
	public JobDataMap getJobDataMap(BaseSheduledCronJobRequest request) {

		TradeRequest tradeRequest = (TradeRequest) request;
		JobDataMap jobDataMap = new JobDataMap();

		Map<String, String> tradeMap = new HashMap<String, String>();

		for (TradeData tradeData : tradeRequest.getTradeList()) {
			tradeMap.put(tradeData.getPlayerToAdd(), tradeData.getPlayerToRemove());
		}

		jobDataMap.put("tradeMap", tradeMap);
		jobDataMap.put("tradeDate", tradeRequest.getTradeDate());

		return jobDataMap;
	}
}
