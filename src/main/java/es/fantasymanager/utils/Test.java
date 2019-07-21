package es.fantasymanager.utils;

import java.time.LocalDateTime;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Locale;

public class Test {

//	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy - h:mm a", Locale.ENGLISH);
	
	private static final DateTimeFormatter formatter = new DateTimeFormatterBuilder()
	        .appendPattern("MMM dd - h:mm a")
	        .parseDefaulting(ChronoField.YEAR_OF_ERA, Year.now().getValue())
	        .toFormatter(Locale.ENGLISH);

	        
//	private static final DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH);
//	private static final DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH);

	public static void main(String[] args) {
		System.out.println("Parse News Started! " + Thread.currentThread().getId());

		String strDate = "Jul 22 - 1:12 PM";
//		String[] split = strDate.split(" - ");
//		LocalDate date = LocalDate.parse(split[0], formatter1);
//		System.out.println("DateTime: " + date);
//
//		LocalTime time = LocalTime.parse(split[1], formatter2);
//		System.out.println("Time: " + time);

		LocalDateTime dateTime = LocalDateTime.parse(strDate, formatter);
		System.out.println("dateTime: " + dateTime);


	}

}
