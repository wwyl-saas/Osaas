package com.fate.common.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @program: parent
 * @description: 时间工具
 * @author: chenyixin
 * @create: 2019-05-03 23:09
 **/
public class DateUtil {


    public static String dateToString(Date date, String pattern) {
        String strDateTime = null;
        SimpleDateFormat formater = new SimpleDateFormat(pattern);
        strDateTime = date == null ? null : formater.format(date);
        return strDateTime;
    }

    public static String dateToString(Date date) {
        String _pattern = "yyyy-MM-dd";
        return date == null ? null : dateToString(date, _pattern);
    }

    public static String dateTimeToString(Date date) {
        String _pattern = "yyyy-MM-dd HH:mm:ss";
        return date == null ? null : dateToString(date, _pattern);
    }

    public static String dateStampToString(Date date) {
        String _pattern = "yyyy-MM-dd HH:mm:ss:SSS";
        return date == null ? null : dateToString(date, _pattern);
    }

    public static String dateStampToStringNoSp(Date date) {
        String _pattern = "yyyyMMddHHmmssSSS";
        return date == null ? null : dateToString(date, _pattern);
    }

    public static String dateStampToStringMs(Date date) {
        String _pattern = "yyyyMMddHHmmss";
        return date == null ? null : dateToString(date, _pattern);
    }

    public static String dateStampToStringYY(Date date) {
        String _pattern = "yyMMddHHmmss";
        return date == null ? null : dateToString(date, _pattern);
    }

    public static String dateStampToStringyyyyMMdd(Date date) {
        String _pattern = "yyyyMMdd";
        return date == null ? null : dateToString(date, _pattern);
    }

    public static String dateStampToDotyyyyMMdd(Date date) {
        String _pattern = "yyyy.MM.dd";
        return date == null ? null : dateToString(date, _pattern);
    }

    public static String dateStampToStringHHmmss(Date date) {
        String _pattern = "HHmmss";
        return date == null ? null : dateToString(date, _pattern);
    }

    public static Date stringToDate(String str, String pattern) {
        Date dateTime = null;

        try {
            if (str != null && !str.equals("")) {
                SimpleDateFormat formater = new SimpleDateFormat(pattern);
                dateTime = formater.parse(str);
            }
        } catch (Exception var4) {
            ;
        }

        return dateTime;
    }

    public static Date stringToDate(String str) {
        String _pattern = "yyyy-MM-dd";
        return stringToDate(str, _pattern);
    }

    public static Date stringToDateTime(String str) {
        String _pattern = "yyyy-MM-dd HH:mm:ss";
        return stringToDate(str, _pattern);
    }

    public static Timestamp stringToDateHMS(String str) {
        Timestamp time = null;
        time = Timestamp.valueOf(str);
        return time;
    }

    public static LocalDateTime getDateTimeOfTimestamp(long timestamp) {
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZoneId zone = ZoneId.systemDefault();
        return LocalDateTime.ofInstant(instant, zone);
    }

    public static long getTimestampOfDateTime(LocalDateTime localDateTime) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        return instant.toEpochMilli();
    }

    public static String getLocalDateString(LocalDate localDate) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return localDate.format(fmt);
    }

    public static String getChinese8LocalDateString(LocalDate localDate) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(localDate.getYear()).append("年")
                .append(localDate.getMonthValue()).append("月")
                .append(localDate.getDayOfMonth()).append("日");
        return stringBuilder.toString();
    }

    public static String getChinese4LocalDateString(LocalDate localDate) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(localDate.getMonthValue()).append("月")
                .append(localDate.getDayOfMonth()).append("日");
        return stringBuilder.toString();
    }

    public static String getLocalDateTimeString(LocalDateTime localDateTime) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDateTime.format(fmt);
    }

    public static String getLocalTimeString(LocalTime localTime) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm:ss");
        return localTime.format(fmt);
    }

    public static LocalDate getLocalDate(String date) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date, fmt);
    }

    public static LocalDateTime getLocalDateTime(String dateTime) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(dateTime, fmt);
    }

    public static LocalTime getLocalTime(String time) {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("HH:mm:ss");
        return LocalTime.parse(time, fmt);
    }

    /**
     * 获取日期对应周的基础年份
     *
     * @param date
     * @return
     */
    public static int getWeekBasedYear(LocalDate date) {
        WeekFields weekFields = WeekFields.ISO;
        return date.get(weekFields.weekBasedYear());
    }

    /**
     * 获取日期对应周数1-52
     *
     * @param date
     * @return
     */
    public static int getWeekOfWeekBasedYear(LocalDate date) {
        WeekFields weekFields = WeekFields.ISO;
        return date.get(weekFields.weekOfWeekBasedYear());
    }

    /**
     * 计算两个时间点之间的天数
     */
    public static long getBetweenDay(LocalDate start, LocalDate end) {
        return ChronoUnit.DAYS.between(start, end);
    }

    /**
     * 获取指定日期范围内的所有指定星期 包含开始日期和结束日期
     *
     * @param weeks 1,3,7表示周一，周三，周日
     * @return List<LocalDate>
     */
    public static List<LocalDate> getWeekLocalDateListByRange(LocalDate startDay, LocalDate endDay, List<String> weeks) {
        List<LocalDate> localDateList = new ArrayList<>();
        TemporalField fieldISO = WeekFields.of(DayOfWeek.of(1), 1).dayOfWeek();
        LocalDate tempDay;
        for (String week : weeks) {
            tempDay = startDay.with(fieldISO, Long.parseLong(week));
            if (tempDay.isBefore(startDay)) {
                tempDay = tempDay.plusWeeks(1);
            }
            while (tempDay.isBefore(endDay) || tempDay.isEqual(endDay)) {
                localDateList.add(tempDay);
                tempDay = tempDay.plusWeeks(1);
            }
        }
        return localDateList;
    }

    /**
     * 获取指定日期范围内的所有指定dayOfMonth 包含开始日期和结束日期
     *
     * @param months 1,29,31表示每月的1号，29号，31号
     * @return List<LocalDate>
     */
    public static List<LocalDate> getLocalDateListByMonth(LocalDate startDate, LocalDate endDate, List<String> months) {
        List<LocalDate> localDateList = new ArrayList<>();

        LocalDate localDate;
        for (String month : months) {
            LocalDate tempDate = startDate;
            while (tempDate.isBefore(endDate) || tempDate.getMonthValue() == endDate.getMonthValue()) {
                if (tempDate.lengthOfMonth() >= Integer.valueOf(month)) {
                    localDate = tempDate.withDayOfMonth(Integer.valueOf(month));
                    if (localDate.isAfter(startDate) || localDate.isEqual(startDate)) {
                        localDate = tempDate.withDayOfMonth(Integer.valueOf(month));
                        if (localDate.isEqual(endDate) || localDate.isBefore(endDate)) {
                            localDateList.add(localDate);
                        }
                    }
                }
                tempDate = tempDate.plusMonths(1);
            }
        }
        return localDateList;
    }

    /**
     * 获取指定范围内所有日期，包含开始日期和结束日期
     *
     * @return List<LocalDate>
     */
    public static List<LocalDate> getLocalDateByDay(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> localDateList = Stream.iterate(startDate, date -> date.plusDays(1)).
                limit(ChronoUnit.DAYS.between(startDate, endDate))
                .collect(Collectors.toList());
        localDateList.add(endDate);
        return localDateList;
    }

    /**
     * 查询日期对应的季度
     *
     * @param date
     * @return
     */
    public static int getQuarterByLocalDate(LocalDate date) {
        TemporalQuery<Integer> quarterOfYearQuery = new QuarterOfYearQuery();
        return date.query(quarterOfYearQuery);
    }

    /**
     * 是否同一周
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean ifSameWeek(LocalDate date1, LocalDate date2) {
        if (ifSameYear(date1, date2)) {
            int week1 = getWeekOfWeekBasedYear(date1);
            int week2 = getWeekOfWeekBasedYear(date2);
            return week1 == week2;
        }
        return false;
    }

    /**
     * 是否同一月
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean ifSameMonth(LocalDate date1, LocalDate date2) {
        if (ifSameYear(date1, date2)) {
            int month1 = date1.getMonthValue();
            int month2 = date2.getMonthValue();
            return month1 == month2;
        }
        return false;
    }

    /**
     * 是否同一季度
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean ifSameQuarter(LocalDate date1, LocalDate date2) {
        if (ifSameYear(date1, date2)) {
            int quarter1 = getQuarterByLocalDate(date1);
            int quarter2 = getQuarterByLocalDate(date2);
            return quarter1 == quarter2;
        }
        return false;
    }

    /**
     * 是否同一年
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean ifSameYear(LocalDate date1, LocalDate date2) {
        return date1.getYear() == date2.getYear();
    }


    /**
     * 判断当日是否处于时间范围内
     * @param startTime
     * @param endTime
     * @return
     */
    public static boolean outOfDateRange(LocalDate startTime, LocalDate endTime) {
        LocalDate today=LocalDate.now();
        if (!today.isBefore(startTime) && !today.isAfter(endTime)){
            return true;
        }
        return false;
    }

    /**
     * 判断是否过期
     * @param endDate
     * @return
     */
    public static boolean ifExpired(LocalDate endDate) {
        LocalDate today=LocalDate.now();
        if (today.isAfter(endDate)){
            return true;
        }
        return false;
    }
}


