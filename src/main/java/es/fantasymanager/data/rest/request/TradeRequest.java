package es.fantasymanager.data.rest.request;

import java.io.Serializable;
import java.util.List;

import es.fantasymanager.data.business.TradeData;
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
}
