package es.fantasymanager.data.rest.request;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TradeRequest extends BaseSheduledCronJobRequest implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private String playerToAdd;

	private String playerToRemove;
}
