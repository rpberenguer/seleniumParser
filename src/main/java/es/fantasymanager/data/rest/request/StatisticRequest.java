package es.fantasymanager.data.rest.request;

import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import es.fantasymanager.utils.LocalDateDeserializer;
import lombok.Data;

@Data
public class StatisticRequest implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@JsonDeserialize(using = LocalDateDeserializer.class)
	private LocalDate startDate;

	@JsonDeserialize(using = LocalDateDeserializer.class)
	private LocalDate endDate;
}
