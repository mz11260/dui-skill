package com.zm.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    /** the millisecond of one day */
    private final static long MILLISECOND_OF_ONE_DAY = 1000 * 3600 * 24;
    /** simple formatter for date */
    private final static String YYYY_MM_DD = "yyyy-MM-dd";

    /**
     * 两个日期之间的间隔天数
     *
     * <ul>
     * <li>first date input 2018-07-18 14:20:20, second date input 2018-07-15 09:20:20, returns 2</li>
     * <li>first date input 2018-07-15 14:20:20, second date input 2018-07-18 09:20:20, returns 2</li>
     * <li>first date input 2018-07-16 14:20:20, second date input 2018-07-18 09:20:20, returns 1</li>
     * <li>first date input 2018-07-17 14:20:20, second date input 2018-07-18 09:20:20, returns 0</li>
     * <li>first date input 2018-07-18 14:20:20, second date input 2018-07-18 09:20:20, returns 0</li>
     * </ul>
     *
     * @param firstDate the first date, not altered, not null
     * @param secondDate  the second date, not altered, not null
     * @return interval days
     * @throws ParseException if the beginning of the specified string cannot be parsed.
     */
    public final static int intervalDays(final Date firstDate, final Date secondDate) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(YYYY_MM_DD);
        int differenceDays = differenceDays(format.format(firstDate), format.format(secondDate));
        return differenceDays == 0 ? 0 : differenceDays - 1;
    }

    /**
     * 两个日期之间的间隔天数
     *
     * <ul>
     * <li>first date input 2018-07-18 14:20:20, second date input 2018-07-15 09:20:20, returns 2</li>
     * <li>first date input 2018-07-15 14:20:20, second date input 2018-07-18 09:20:20, returns 2</li>
     * <li>first date input 2018-07-16 14:20:20, second date input 2018-07-18 09:20:20, returns 1</li>
     * <li>first date input 2018-07-17 14:20:20, second date input 2018-07-18 09:20:20, returns 0</li>
     * <li>first date input 2018-07-18 14:20:20, second date input 2018-07-18 09:20:20, returns 0</li>
     * </ul>
     *
     * @param date1 the first date string, not altered, not null. format yyyy-MM-dd HH:mm:ss
     * @param date2 the second date, not altered, not null
     * @return interval days
     * @throws ParseException if the beginning of the specified string cannot be parsed.
     */
    public final static int intervalDays(final String date1, final Date date2) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(YYYY_MM_DD);
        int differenceDays = differenceDays(date1, format.format(date2));
        return differenceDays == 0 ? 0 : differenceDays - 1;
    }

    /**
     * 两个日期之间的间隔天数
     *
     * <ul>
     * <li>first date input 2018-07-18 14:20:20, second date input 2018-07-15 09:20:20, returns 2</li>
     * <li>first date input 2018-07-15 14:20:20, second date input 2018-07-18 09:20:20, returns 2</li>
     * <li>first date input 2018-07-16 14:20:20, second date input 2018-07-18 09:20:20, returns 1</li>
     * <li>first date input 2018-07-17 14:20:20, second date input 2018-07-18 09:20:20, returns 0</li>
     * <li>first date input 2018-07-18 14:20:20, second date input 2018-07-18 09:20:20, returns 0</li>
     * </ul>
     *
     * @param date1 the first date string, not altered, not null. format yyyy-MM-dd HH:mm:ss
     * @param date2 the second date string, not altered, not null. format yyyy-MM-dd HH:mm:ss
     * @return interval days
     * @throws ParseException if the beginning of the specified string cannot be parsed.
     */
    public final static int intervalDays(final String date1, final String date2) throws ParseException {
        int differenceDays = differenceDays(date1, date2);
        return differenceDays == 0 ? 0 : differenceDays - 1;
    }

    /**
     * 两个日期之间的相差天数
     *
     * <ul>
     * <li>first date input 2018-07-18 14:20:20, second date input 2018-07-15 09:20:20, returns 3</li>
     * <li>first date input 2018-07-15 14:20:20, second date input 2018-07-18 09:20:20, returns 3</li>
     * <li>first date input 2018-07-16 14:20:20, second date input 2018-07-18 09:20:20, returns 2</li>
     * <li>first date input 2018-07-17 14:20:20, second date input 2018-07-18 09:20:20, returns 1</li>
     * <li>first date input 2018-07-18 14:20:20, second date input 2018-07-18 09:20:20, returns 0</li>
     * </ul>
     *
     * @param firstDate first date.
     * @param secondDate second date.
     * @return difference days
     *
     * @throws ParseException if the beginning of the specified string cannot be parsed.
     */
    public final static int differenceDays(final Date firstDate, final Date secondDate) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(YYYY_MM_DD);
        return differenceDays(format.format(firstDate), format.format(secondDate));
    }

    /**
     * 两个日期之间的相差天数
     *
     * <ul>
     * <li>first date input 2018-07-18 14:20:20, second date input 2018-07-15 09:20:20, returns 3</li>
     * <li>first date input 2018-07-15 14:20:20, second date input 2018-07-18 09:20:20, returns 3</li>
     * <li>first date input 2018-07-16 14:20:20, second date input 2018-07-18 09:20:20, returns 2</li>
     * <li>first date input 2018-07-17 14:20:20, second date input 2018-07-18 09:20:20, returns 1</li>
     * <li>first date input 2018-07-18 14:20:20, second date input 2018-07-18 09:20:20, returns 0</li>
     * </ul>
     *
     * @param firstDate first date.
     * @param secondDate second date string. format yyyy-MM-dd HH:mm:ss
     * @return difference days
     *
     * @throws ParseException if the beginning of the specified string cannot be parsed.
     */
    public final static int differenceDays(final Date firstDate, final String secondDate) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(YYYY_MM_DD);
       return differenceDays(format.format(firstDate), secondDate);
    }

    /**
     * 两个日期之间的相差天数
     *
     * <ul>
     * <li>first date input 2018-07-18 14:20:20, second date input 2018-07-15 09:20:20, returns 3</li>
     * <li>first date input 2018-07-15 14:20:20, second date input 2018-07-18 09:20:20, returns 3</li>
     * <li>first date input 2018-07-16 14:20:20, second date input 2018-07-18 09:20:20, returns 2</li>
     * <li>first date input 2018-07-17 14:20:20, second date input 2018-07-18 09:20:20, returns 1</li>
     * <li>first date input 2018-07-18 14:20:20, second date input 2018-07-18 09:20:20, returns 0</li>
     * </ul>
     *
     * @param firstDate first date string. format yyyy-MM-dd HH:mm:ss
     * @param secondDate second date string. format yyyy-MM-dd HH:mm:ss
     * @return difference days
     *
     * @throws ParseException if the beginning of the specified string cannot be parsed.
     */
    public final static int differenceDays(final String firstDate, final String secondDate) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat(YYYY_MM_DD);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(format.parse(firstDate));
        long time1 = calendar.getTimeInMillis();

        calendar.setTime(format.parse(secondDate));
        long time2 = calendar.getTimeInMillis();

        long intervalDays = Math.abs(time1 - time2) / MILLISECOND_OF_ONE_DAY;
        return Integer.parseInt(String.valueOf(intervalDays));
    }

    public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }
        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        return date.after(begin) && date.before(end);
    }

}
