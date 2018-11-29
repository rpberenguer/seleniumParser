package es.fantasymanager.data.rest.request;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.SafeHtml;
import org.hibernate.validator.constraints.SafeHtml.WhiteListType;

import es.fantasymanager.utils.Constants;
import lombok.Data;

@Data
public class BaseSheduledCronJobRequest implements Serializable, Constants {

	private static final long serialVersionUID = 303195868565969531L;

	@SafeHtml(whitelistType = WhiteListType.NONE)
	@NotNull
	@Length(max = 120)
	private String description;

	@SafeHtml(whitelistType = WhiteListType.NONE)
	@NotNull
	@Pattern(regexp = Constants.CRON_REGEX_VALIDATOR)
	private String cronExpression;

}
