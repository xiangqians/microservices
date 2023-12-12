package org.xiangqian.microservices.common.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiangqian
 * @date 21:24 2022/08/01
 */
public class DateUtil {

    // 时区
    private static final Map<String, ZoneId> zoneIdMap;

    // 日期时间格式化器
    private static final Map<String, DateTimeFormatter> formatterMap;

    static {
        zoneIdMap = new ConcurrentHashMap<>(8, 1f);
        formatterMap = new ConcurrentHashMap<>(128, 1f);
    }

    static ZoneId getZoneId(String zoneId) {
        if (Objects.isNull(zoneId)) {
            return null;
        }

        ZoneId z = zoneIdMap.get(zoneId);
        if (Objects.nonNull(z)) {
            return z;
        }

        synchronized (zoneIdMap) {
            z = zoneIdMap.get(zoneId);
            if (Objects.isNull(z)) {
                z = ZoneId.of(zoneId);
                zoneIdMap.put(zoneId, z);
            }
        }
        return z;
    }

    static DateTimeFormatter getFormatter(String pattern) {
        DateTimeFormatter formatter = formatterMap.get(pattern);
        if (Objects.nonNull(formatter)) {
            return formatter;
        }

        synchronized (formatterMap) {
            formatter = formatterMap.get(pattern);
            if (Objects.isNull(formatter)) {
                formatter = DateTimeFormatter.ofPattern(pattern);
                formatterMap.put(pattern, formatter);
            }
        }
        return formatter;
    }

    /**
     * 当前时间
     *
     * @param zoneId 时区
     * @param type   时间类型
     *               1、{@link ZonedDateTime} 类表示了一个带有时区信息的日期和时间。它提供了更多的功能，例如时区转换、跨时区计算等。
     *               由于其涉及到时区的处理，创建和操作 {@link ZonedDateTime} 对象可能会比 {@link LocalDateTime} 稍微慢一些。
     *               然而，这个性能差异通常是微小的，对于大多数情况来说不会对性能产生显著影响。
     *               2、{@link LocalDateTime} 类表示了一个没有时区信息的日期和时间，并且它的内部实现是基于本地时钟的，不涉及时区转换。因此，{@link LocalDateTime} 对象的创建和操作通常比较高效。
     *               3、{@link LocalDate}
     *               4、{@link LocalTime}
     * @param <T>
     * @return
     */
    public static <T extends Temporal> T now(ZoneId zoneId, Class<T> type) {
        if (Objects.isNull(zoneId)) {
            zoneId = ZoneId.systemDefault();
        }
        if (type == ZonedDateTime.class) {
            return (T) ZonedDateTime.now(zoneId);
        }
        if (type == LocalDateTime.class) {
            return (T) LocalDateTime.now(zoneId);
        }
        if (type == LocalDate.class) {
            return (T) LocalDate.now(zoneId);
        }
        if (type == LocalTime.class) {
            return (T) LocalTime.now(zoneId);
        }
        throw new UnsupportedOperationException();
    }

    public static <T extends Temporal> T now(String zoneId, Class<T> type) {
        return now(getZoneId(zoneId), type);
    }

    public static <T extends Temporal> T now(Class<T> type) {
        return now((ZoneId) null, type);
    }

    /**
     * 格式化 {@link ZonedDateTime}（时区日期时间）、{@link LocalDateTime}（本地日期时间）、{@link LocalDate}（本地日期）、{@link LocalTime}（本地时间）
     *
     * @param temporal 日期时间
     * @param pattern  日期时间格式
     *                 "yyyy"：年份
     *                 "MM"  ：月份
     *                 "dd"  ：日期
     *                 "HH"  ：小时（24小时制）
     *                 "mm"  ：分钟
     *                 "ss"  ：秒钟
     *                 "SSS" ：毫秒
     *                 例如：
     *                 "yyyy/MM/dd"：年份、月份和日期（例如：2023/11/13）
     *                 "HH:mm:ss"  ：小时、分钟和秒钟（例如：10:31:38）
     *                 "yyyy/MM/dd HH:mm:ss.SSS"：完整的日期时间（例如：2023/11/13 10:31:38.491）
     * @return
     */
    public static String format(Temporal temporal, String pattern) {
        return getFormatter(pattern).format(temporal);
    }

    /**
     * 解析时间
     *
     * @param text
     * @param pattern 日期时间格式
     * @param type    时间类型
     *                1、{@link ZonedDateTime}
     *                2、{@link LocalDateTime}
     *                3、{@link LocalDate}
     *                4、{@link LocalTime}
     * @param <T>
     * @return
     */
    public static <T extends Temporal> T parse(String text, String pattern, Class<T> type) {
        if (type == ZonedDateTime.class) {
            return (T) ZonedDateTime.parse(text, getFormatter(pattern));
        }
        if (type == LocalDateTime.class) {
            return (T) LocalDateTime.parse(text, getFormatter(pattern));
        }
        if (type == LocalDate.class) {
            return (T) LocalDate.parse(text, getFormatter(pattern));
        }
        if (type == LocalTime.class) {
            return (T) LocalTime.parse(text, getFormatter(pattern));
        }
        throw new UnsupportedOperationException();
    }

    public static long toMillisecond(Temporal temporal, ZoneId zoneId) {
        if (Objects.isNull(zoneId)) {
            zoneId = ZoneId.systemDefault();
        }
        if (temporal instanceof LocalDateTime) {
            LocalDateTime localDateTime = (LocalDateTime) temporal;
            return localDateTime.atZone(zoneId).toInstant().toEpochMilli();
        }
        if (temporal instanceof LocalDate) {
            LocalDate localDate = (LocalDate) temporal;
            return localDate.atStartOfDay(zoneId).toInstant().toEpochMilli();
        }
        throw new UnsupportedOperationException();
    }

    public static long toMillisecond(Temporal temporal, String zoneId) {
        return toMillisecond(temporal, getZoneId(zoneId));
    }

    public static long toMillisecond(Temporal temporal) {
        return toMillisecond(temporal, ZoneId.systemDefault());
    }

    public static long toSecond(Temporal temporal, ZoneId zoneId) {
        if (Objects.isNull(zoneId)) {
            zoneId = ZoneId.systemDefault();
        }
        if (temporal instanceof LocalDateTime) {
            LocalDateTime dateTime = (LocalDateTime) temporal;
            return dateTime.atZone(zoneId).toEpochSecond();
        }
        if (temporal instanceof LocalDate) {
            LocalDate date = (LocalDate) temporal;
            return date.atStartOfDay(zoneId).toEpochSecond();
        }
        throw new UnsupportedOperationException();
    }

    public static long toSecond(Temporal temporal, String zoneId) {
        return toSecond(temporal, getZoneId(zoneId));
    }

    public static long toSecond(Temporal temporal) {
        return toSecond(temporal, ZoneId.systemDefault());
    }

    public static <T extends Temporal> T ofMillisecond(long timestamp, ZoneId zoneId, Class<T> type) {
        if (Objects.isNull(zoneId)) {
            zoneId = ZoneId.systemDefault();
        }
        if (type == LocalDateTime.class) {
            return (T) LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), zoneId);
        }
        if (type == LocalDate.class) {
            return (T) LocalDate.ofInstant(Instant.ofEpochMilli(timestamp), zoneId);
        }
        throw new UnsupportedOperationException();
    }

    public static <T extends Temporal> T ofMillisecond(long timestamp, String zoneId, Class<T> type) {
        return ofMillisecond(timestamp, getZoneId(zoneId), type);
    }

    public static <T extends Temporal> T ofMillisecond(long timestamp, Class<T> type) {
        return ofMillisecond(timestamp, ZoneId.systemDefault(), type);
    }

    public static <T> T ofSecond(long timestamp, ZoneId zoneId, Class<T> type) {
        if (Objects.isNull(zoneId)) {
            zoneId = ZoneId.systemDefault();
        }
        if (type == LocalDateTime.class) {
            return (T) LocalDateTime.ofInstant(Instant.ofEpochSecond(timestamp), zoneId);
        }
        if (type == LocalDate.class) {
            return (T) LocalDate.ofInstant(Instant.ofEpochSecond(timestamp), zoneId);
        }
        throw new UnsupportedOperationException();
    }

    public static <T> T ofSecond(long timestamp, String zoneId, Class<T> type) {
        return ofSecond(timestamp, getZoneId(zoneId), type);
    }

    public static <T> T ofSecond(long timestamp, Class<T> type) {
        return ofSecond(timestamp, ZoneId.systemDefault(), type);
    }

    public static LocalDateTime dateToLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

}
