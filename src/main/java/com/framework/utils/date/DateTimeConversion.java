package com.framework.utils.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import com.framework.exception.AutomationException;
import com.framework.utils.TestReporter;

public class DateTimeConversion {

	private DateTimeConversion() {
		
	}
	
	/**
	 * This method will convert a string date from a format to a format.
	 * 
	 * @param date
	 *            - String
	 * @param fromFormat
	 *            - the format it is origianlly in. Samples are "MM/dd/yyyy", "MMMM
	 *            dd, yyyy", and "dd-MM-yy", "m/d/yyyy"
	 * @param toFormat
	 *            - the format you want the date to be in
	 * @return the convert date in string
	 */
	public static String convert(String date, String fromFormat, String toFormat) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(fromFormat, Locale.ENGLISH);
		Date parsedDate = null;
		try {
			parsedDate = dateFormat.parse(date);
		} catch (ParseException e) {
			TestReporter.logTrace("Exception - date could not be parsed");
			e.printStackTrace();
		}
		return convert(parsedDate, toFormat);
	}

	/**
	 * This method converts a string date to a specified format
	 * 
	 * @param date
	 * @param format
	 *            Samples are "MM/dd/yyyy", "MMMM dd, yyyy", and "dd-MM-yy",
	 *            "m/d/yyyy"
	 * @return
	 */
	public static String convert(String date, String format) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		Date parsedDate = null;
		try {
			parsedDate = dateFormat.parse(date);
		} catch (ParseException e) {
			TestReporter.logTrace("Exception - date could not be parsed");
			e.printStackTrace();
		}
		return convert(parsedDate, format);
	}

	/**
	 * This method converts a Date object to a specified format
	 * 
	 * @param date
	 * @param toFormat
	 *            format Samples are "MM/dd/yyyy", "MMMM dd, yyyy", and "dd-MM-yy",
	 *            "m/d/yyyy"
	 * @return
	 */
	public static String convert(Date date, String toFormat) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(toFormat, Locale.ENGLISH);
		return dateFormat.format(date);
	}

	/**
	 * 
	 * @param daysOut
	 *            - Number of days from current date to get date for
	 * @param format
	 *            - Desired format of date. Samples are "MM/dd/yyyy", "MMMM dd,
	 *            yyyy", and "dd-MM-yy"
	 * @return String future date in desired format
	 */
	public static String getDaysOut(String daysOut, String format) {
		DateFormat dateFormat = new SimpleDateFormat(format);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, Integer.parseInt(daysOut));
		String convertedDate = dateFormat.format(cal.getTime());
		return convertedDate;
	}

	/**
	 * Get System Date in a given date pattern (e.g.: MM/dd/yyyy, dd/MM/yy,
	 * "yyyy-MM-dd'T'HH:mm:ss)
	 * 
	 * @param pattern
	 * @return
	 */
	public static String getSystemDate(String pattern) {
		DateFormat dateFormat = new SimpleDateFormat(pattern);
		Calendar cal = Calendar.getInstance();
		String convertedDate = dateFormat.format(cal.getTime());
		return convertedDate;
	}

	/**
	 * Converting Timezone from UTC to EST/EDT
	 * 
	 * @param strDate
	 * @return
	 * @throws ParseException
	 */
	public static String convertTimeZoneUTCtoEST(String strDate) throws ParseException {
		DateFormat utcFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		utcFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date date = utcFormat.parse(strDate);
		DateFormat estFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm aa");
		estFormat.setTimeZone(TimeZone.getTimeZone("America/New_York"));
		return estFormat.format(date);
	}

	/**
	 * Uses joda time class (which is what jdkv8 uses) to get the current date/time
	 * in a specified timezone. Will return in a string based on the pattern
	 * specified, e.g. MM/dd/yyyy, dd/MM/yy, yyyy-MM-dd'T'HH:mm:ss, MM/dd/yyyy hh:mm
	 * aa Timezone must match the joda time set, see for more details:
	 * http://www.joda.org/joda-time/timezones.html examples: UTC, America/New_York,
	 * America/Los_Angeles
	 * 
	 * @date 8/28/2017
	 * @param timezone
	 *            UTC, America/New_York, America/Los_Angeles
	 * @param format
	 *            MM/dd/yyyy, dd/MM/yy, yyyy-MM-dd'T'HH:mm:ss, MM/dd/yyyy hh:mm aa
	 * @return
	 */
	public static String getSystemDateByTimezone(String timezone, String format) {
		DateTime dt = new DateTime();
		DateTime timezoneDT = dt.withZone(DateTimeZone.forID(timezone));
		return timezoneDT.toString(format);
	}

	/**
	 * Compares a string date(valueToCheck) between a range of 2 dates, start & end
	 * 
	 * @param valueToCheck
	 * @param startTime
	 * @param endTime
	 * @param timeZone
	 *            such as UTC, EST, PST,
	 * @param format
	 *            such e.g.: MM/dd/yyyy, dd/MM/yy, yyyy-MM-dd'T'HH:mm:ss, MM/dd/yyyy
	 *            hh:mm aa
	 * @return
	 */
	public static boolean isTimeWithinInterval(String valueToCheck, String startTime, String endTime, String format) {
		try {
			DateFormat df = new SimpleDateFormat(format);
			Date end = df.parse(endTime);
			Date start = df.parse(startTime);
			Date value = df.parse(valueToCheck);

			if (end.after(value) && (start.before(value) || start.equals(value))) {
				return true;
			}
		} catch (ParseException e) {
			throw new AutomationException("Parse exception with converting date time: " + e.getMessage());
		}
		return false;
	}

	/**
	 * Compares a string date(valueTOCheck) between a range of 2 dates, a start date
	 * and a start date + minutes.
	 * 
	 * @param valueToCheck
	 * @param startTime
	 * @param numberOfMinsToAdd
	 * @return
	 */
	public static boolean isTimeWithinInterval(String valueToCheck, String startTime, int numberOfMinsToAdd, String format) {
		String endAsString;
		DateFormat df = new SimpleDateFormat(format);
		try {
			Date start = df.parse(startTime);
			Calendar cal = Calendar.getInstance();
			cal.setTime(start);
			cal.add(Calendar.MINUTE, numberOfMinsToAdd);
			endAsString = df.format(cal.getTime());
		} catch (ParseException e) {
			throw new AutomationException("Parse exception with converting date time: " + e.getMessage());
		}
		return isTimeWithinInterval(valueToCheck, startTime, endAsString, format);
	}
}