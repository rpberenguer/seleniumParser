package es.fantasymanager.data.rest.request;

import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import es.fantasymanager.utils.LocalDateDeserializer;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class StatisticRequest extends BaseSheduledCronJobRequest implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@JsonDeserialize(using = LocalDateDeserializer.class)
	private LocalDate startDate;

	@JsonDeserialize(using = LocalDateDeserializer.class)
	private LocalDate endDate;

	private String nbaId;
}
