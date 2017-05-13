package br.com.bf.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Dates {

	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
	
	public static String formatDateUTC(Date date) {
		return dateFormat.format(date);
	}
	
	public static Date parseDateUTC(String dateFormatted) throws ParseException {
		return dateFormat.parse(dateFormatted);
	}
	
}
