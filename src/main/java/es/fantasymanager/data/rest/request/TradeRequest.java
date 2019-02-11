package es.fantasymanager.data.rest.request;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import es.fantasymanager.data.business.TradeData;
import es.fantasymanager.utils.LocalDateTimeDeserializer;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TradeRequest extends BaseSheduledCronJobRequest implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private List<TradeData> tradeList;

	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime tradeDate;
}
