package com.example.demo.utils;

import com.alibaba.fastjson.JSON;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author zzs
 * @version 1.0
 * @date 2018-12-29 12:43
 */
public class CommonConverter {

    private CommonConverter() {
    }

    public static String stringValueOf(Object val) {
        if (val == null) {
            return null;
        }
        try {
            return String.valueOf(val);
        } catch (Exception e) {
            return null;
        }
    }

    public static Long longValueOf(Object val) {
        Double valueOf = doubleValueOf(stringValueOf(val));
        if (valueOf != null) {
            return valueOf.longValue();
        } else {
            return null;
        }
    }

    public static Integer integerValueOf(Object val) {
        Double valueOf = doubleValueOf(stringValueOf(val));
        if (valueOf != null) {
            return valueOf.intValue();
        } else {
            return null;
        }
    }

    public static Double doubleValueOf(String val) {
        try {
            return Double.parseDouble(val);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Converts a String object in JDBC timestamp escape format to a Timestamp value.
     *
     * @param val with this pattern: yyyy-mm-dd hh:mm:ss[.fffffffff]
     * @return
     */
    public static Timestamp timestampOf(Object val) {
        try {
            return Timestamp.valueOf(stringValueOf(val));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Converts a String object in JDBC timestamp escape format to a Unix Timestamp.
     *
     * @param val with this pattern: yyyy-mm-dd hh:mm:ss[.fffffffff]
     * @return
     */
    public static Long millisTimeOf(Object val) {
        Timestamp timestamp = timestampOf(val);
        if (timestamp == null) {
            return null;
        }
        return timestamp.getTime();
    }

    public static String jsonStringOf(Object val) {
        //Gson gson = new Gson();
        //return gson.toJson(val);
        return JSON.toJSONString(val);
    }

    /**
     * Formats an object to produce a string.
     *
     * @param val     The object to format (receive Date or Unix Timestamp)
     * @param pattern the pattern describing the date and time format
     * @return
     */
    public static String formatObjToStrTime(Object val, String pattern) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.format(val);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Parses text from the beginning of the given string to produce a Unix Timestamp.
     *
     * @param val     字符串型日期
     * @param pattern the pattern describing the date and time format
     * @return
     */
    public static Long parseLongFromStrTime(Object val, String pattern) {
        String value = stringValueOf(val);
        if (value == null) {
            return null;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.parse(value).getTime();
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * Parses text from the beginning of the given string to produce a date.
     *
     * @param val     日期对象
     * @param pattern 日期格式
     * @return
     */
    public static Date parseDateFromStrTime(Object val, String pattern) {
        String value = stringValueOf(val);
        if (value == null) {
            return null;
        }
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            return simpleDateFormat.parse(value);
            //ZonedDateTime zonedDateTime = LocalDateTime.parse(value, DateTimeFormatter.ofPattern(pattern)).atZone(ZoneId.systemDefault());
            //Instant instant = zonedDateTime.toInstant();
            //return Date.from(instant);
        } catch (Exception e) {
            return null;
        }
    }

}
