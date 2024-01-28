package org.xiangqian.microservices.common.util;

import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * @author xiangqian
 * @date 19:34 2023/06/28
 */
public class DateUtilTest {

    // ======================= Date ======================

    @Test
    public void dateFormat() {
        final LocalDate date = LocalDate.now();

        final String pattern = "yyyy/MM/dd";
        String format = DateUtil.Date.format(date, pattern);
        System.out.println(format);
        System.out.println(DateUtil.Date.parse(format, pattern));
    }

    @Test
    public void dateToSecond() {
        final LocalDate date = LocalDate.now();

        long second = DateUtil.Date.toSecond(date);
        System.out.println(second);
        System.out.println(DateUtil.Date.ofSecond(second));
        System.out.println();

        String zoneId = DateUtil.ZoneId.AustraliaSydney;
        second = DateUtil.Date.toSecond(date, zoneId);
        System.out.println(second);
        System.out.println(DateUtil.Date.ofSecond(second));
        System.out.println(DateUtil.Date.ofSecond(second, zoneId));
    }

    @Test
    public void dateToMillisecond() {
        final LocalDate date = LocalDate.now();

        long millisecond = DateUtil.Date.toMillisecond(date);
        System.out.println(millisecond);
        System.out.println(DateUtil.Date.ofMillisecond(millisecond));
        System.out.println();

        String zoneId = DateUtil.ZoneId.AustraliaSydney;
        millisecond = DateUtil.Date.toMillisecond(date, zoneId);
        System.out.println(millisecond);
        System.out.println(DateUtil.Date.ofMillisecond(millisecond));
        System.out.println(DateUtil.Date.ofMillisecond(millisecond, zoneId));
    }

    @Test
    public void dateToDate() {
        final LocalDate date = LocalDate.now();

        Date date1 = DateUtil.Date.toDate(date);
        System.out.println(date1);
        System.out.println(DateUtil.Date.ofDate(date1));
        System.out.println();

        String zoneId = DateUtil.ZoneId.AustraliaSydney;
        date1 = DateUtil.Date.toDate(date, zoneId);
        System.out.println(date1);
        System.out.println(DateUtil.Date.ofDate(date1));
        System.out.println(DateUtil.Date.ofDate(date1, zoneId));
    }

    // ======================= Time ======================

    @Test
    public void timeFormat() {
        final LocalTime time = LocalTime.now();

        final String pattern = "HH:mm:ss";
        String format = DateUtil.Time.format(time, pattern);
        System.out.println(format);
        System.out.println(DateUtil.Time.parse(format, pattern));
    }

    // ======================= DateTime ======================

    @Test
    public void dateTimeFormat() {
        final LocalDateTime dateTime = LocalDateTime.now();

        final String pattern = "yyyy/MM/dd HH:mm:ss.SSS";
        String format = DateUtil.DateTime.format(dateTime, pattern);
        System.out.println(format);
        System.out.println(DateUtil.DateTime.parse(format, pattern));
        System.out.println();

        String zoneId = DateUtil.ZoneId.AustraliaSydney;
        System.out.println(dateTime.atZone(DateUtil.getZoneId(zoneId)));
    }

    @Test
    public void dateTimeToSecond() {
        final LocalDateTime dateTime = LocalDateTime.now();

        long second = DateUtil.DateTime.toSecond(dateTime);
        System.out.println(second);
        System.out.println(DateUtil.DateTime.ofSecond(second));
        System.out.println();

        String zoneId = DateUtil.ZoneId.AustraliaSydney;
        second = DateUtil.DateTime.toSecond(dateTime, zoneId);
        System.out.println(second);
        System.out.println(DateUtil.DateTime.ofSecond(second));
        System.out.println(DateUtil.DateTime.ofSecond(second, zoneId));
    }

    @Test
    public void dateTimeToMillisecond() {
        final LocalDateTime dateTime = LocalDateTime.now();

        long millisecond = DateUtil.DateTime.toMillisecond(dateTime);
        System.out.println(millisecond);
        System.out.println(DateUtil.DateTime.ofMillisecond(millisecond));
        System.out.println();

        String zoneId = DateUtil.ZoneId.AustraliaSydney;
        millisecond = DateUtil.DateTime.toMillisecond(dateTime, zoneId);
        System.out.println(millisecond);
        System.out.println(DateUtil.DateTime.ofMillisecond(millisecond));
        System.out.println(DateUtil.DateTime.ofMillisecond(millisecond, zoneId));
    }

    @Test
    public void dateTimeToDate() {
        LocalDateTime dateTime = LocalDateTime.now();

        Date date1 = DateUtil.DateTime.toDate(dateTime);
        System.out.println(date1);
        System.out.println(DateUtil.DateTime.ofDate(date1));
        System.out.println();

        String zoneId = DateUtil.ZoneId.AustraliaSydney;
        date1 = DateUtil.DateTime.toDate(dateTime, zoneId);
        System.out.println(date1);
        System.out.println(DateUtil.DateTime.ofDate(date1));
        System.out.println(DateUtil.DateTime.ofDate(date1, zoneId));
    }

}
