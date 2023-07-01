package com.biplus.saga.common;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

public class DateUtil {

    private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);

    public static int compareYear(Date first, Date second) {
        LocalDate firstLocalDate = first.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate secondLocalDate = second.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return firstLocalDate.getYear() - secondLocalDate.getYear();
    }

    public static Date parseToDate(String date, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        try {
            LocalDate localDate = LocalDate.parse(date, formatter);
            return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public static String parseDate2String(Date date, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        try {
            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            return formatter.format(localDate);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * @param value Date
     * @return String
     */
    public static String date2ddMMyyyyString(Date value) {
        if (value != null) {
            SimpleDateFormat ddMMyyyy = new SimpleDateFormat("dd/MM/yyyy");
            return ddMMyyyy.format(value);
        }
        return "";
    }

    public static Date localDatetoDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDate toLocalDate(Date input) {
        return input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }


    public static LocalDateTime toLocalDateTime(Date input) {
        return input.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static String date2ddMMyyyyHHMMss(Date value) {
        if (value != null) {
            SimpleDateFormat dateTimeNoSlash = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            return dateTimeNoSlash.format(value);
        }
        return "";
    }

    public static Date string2Date(String value) {
        return DateUtil.string2DateByPattern(value, "dd/MM/yyyy");
    }

    public static Date string2DateByPattern(String value, String pattern) {
        if (!DataUtil.isNullOrEmpty(value)) {
            SimpleDateFormat dateTime = new SimpleDateFormat(pattern);
            dateTime.setLenient(false);
            try {
                return dateTime.parse(value);
            } catch (ParseException ex) {
                return null;
            }
        }
        return null;
    }

    public static Date addDay(Date nowDate, int period) {
        LocalDate localDate = nowDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return Date.from(localDate.plusDays(period).atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static Date addMonth(Date nowDate, int period) {
        LocalDate localDate = nowDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return Date.from(localDate.plusMonths(period).atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static Date addMinute(Date nowDate, int period) {
        LocalDateTime localDateTime = nowDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return Date.from(localDateTime.plusMinutes(period).atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date getFirstDayOfMonth(Date date) {
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return Date.from(localDate.withDayOfMonth(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static int monthsBetween(Date date1, Date date2) {

        LocalDate dateFrom = date1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate dateTo = date2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        Period intervalPeriod = Period.between(dateFrom, dateTo);
        return intervalPeriod.getMonths();
    }

    public static LocalDateTime parseLocalDateTime(String input, String errorCode) {
        if (StringUtils.isBlank(input)) {
            return null;
        }
        try {
            DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
            return LocalDateTime.parse(input.trim(), df);
        }catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new IllegalArgumentException(errorCode);
        }
    }

    public static String format(TemporalAccessor input, String format) {
        if (input == null) {
            return "";
        }
        DateTimeFormatter df = DateTimeFormatter.ofPattern(format);
        return df.format(input);
    }

    public static String format(TemporalAccessor input) {
        return format(input, "dd/MM/yyyy");
    }

}
