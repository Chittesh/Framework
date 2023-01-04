package com.framework.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

import org.apache.commons.lang3.RandomStringUtils;

public class Randomness {

	private static final String DATE_TEMPLATE = "yyyy-MM-dd";
	private static final Random random = new Random();
	
	private Randomness() {
		
	}
	
    public static String generateMessageId() {
        return randomAlphaNumeric(8) + "-" + randomAlphaNumeric(6) + "-" + randomAlphaNumeric(6) + "-" + randomAlphaNumeric(6) + "-" + randomAlphaNumeric(10);
    }

    public static String generateCurrentDatetime() {
        String repDate = "";
        DateFormat dfms = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"); // for display with milliseconds
        Calendar cal = Calendar.getInstance();
        repDate = dfms.format(cal.getTime());
        return repDate;
    }

    public static String generateCurrentXMLDatetime() {
        DateFormat dfms = new SimpleDateFormat(DATE_TEMPLATE); // XML date time
        DateFormat dfmst = new SimpleDateFormat("HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        String Date = dfms.format(cal.getTime());
        String time = dfmst.format(cal.getTime());
        return Date + "T" + time;
    }

    public static String generateCurrentXMLDatetime(int daysOut) {
        DateFormat dfms = new SimpleDateFormat(DATE_TEMPLATE); // XML date time
        DateFormat dfmst = new SimpleDateFormat("HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, daysOut);
        String date = dfms.format(cal.getTime());
        String time = dfmst.format(cal.getTime());
        return date + "T" + time;
    }

    public static String generateCurrentXMLDate() {
        DateFormat dfms = new SimpleDateFormat(DATE_TEMPLATE); // XML date time
        Calendar cal = Calendar.getInstance();
        String date = dfms.format(cal.getTime());
        return date;
    }

    public static String generateCurrentXMLDate(int daysOut) {
        DateFormat dfms = new SimpleDateFormat(DATE_TEMPLATE); // XML date time
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, daysOut);
        String date = dfms.format(cal.getTime());
        return date;
    }

    public static String randomNumber(int length) {
        return RandomStringUtils.randomNumeric(length);
    }

    public static int randomNumberBetween(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }

    public static String randomString(int length) {
        return RandomStringUtils.randomAlphabetic(length);
    }

    public static String randomAlphaNumeric(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }

    /**
     * Get the random characters
     *
     * @param charLength
     *            Number of random chars to be generated
     *
     * @return String
     */
    public static String generateRandomChars(int charLength) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < charLength; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    public static Date randomDate() {
        int month = random.nextInt(Calendar.DECEMBER) + Calendar.JANUARY;
        int year = Randomness.randomNumberBetween(1940, 2016);
        int day = random.nextInt(30) + 1;

        GregorianCalendar calendar = new GregorianCalendar(year, month, day);
        return calendar.getTime();
    }
}