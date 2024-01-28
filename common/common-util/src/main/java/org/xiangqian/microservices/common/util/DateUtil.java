package org.xiangqian.microservices.common.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * {@link LocalDateTime} 本地日期时间：表示了一个没有时区信息的日期和时间，并且它的内部实现是基于本地时钟的，不涉及时区转换。因此，{@link LocalDateTime} 对象的创建和操作通常比较高效。
 * <p>
 * {@link ZonedDateTime} 时区日期时间：类表示了一个带有时区信息的日期和时间。它提供了更多的功能，例如时区转换、跨时区计算等。由于其涉及到时区的处理，创建和操作 {@link ZonedDateTime} 对象可能会比 {@link LocalDateTime} 稍微慢一些。然而，这个性能差异通常是微小的，对于大多数情况来说不会对性能产生显著影响。
 * <p>
 * 日期时间格式
 * "yyyy"：年份
 * "MM"  ：月份
 * "dd"  ：日期
 * "HH"  ：小时（24小时制）
 * "mm"  ：分钟
 * "ss"  ：秒钟
 * "SSS" ：毫秒
 * 例如：
 * "yyyy/MM/dd"：年份、月份和日期（例如：2023/11/13）
 * "HH:mm:ss"  ：小时、分钟和秒钟（例如：10:31:38）
 * "yyyy/MM/dd HH:mm:ss.SSS"：完整的日期时间（例如：2023/11/13 10:31:38.491）
 *
 * @author xiangqian
 * @date 21:24 2022/08/01
 */
public class DateUtil {

    public static final I<LocalDate> Date = new I<LocalDate>() {
        @Override
        public String format(LocalDate date, String pattern) {
            return getFormatter(pattern).format(date);
        }

        @Override
        public LocalDate parse(String text, String pattern) {
            return LocalDate.parse(text, getFormatter(pattern));
        }

        @Override
        public long toSecond(LocalDate date) {
            return toSecond(date, java.time.ZoneId.systemDefault());
        }

        @Override
        public long toSecond(LocalDate date, String zoneId) {
            return toSecond(date, getZoneId(zoneId));
        }

        private long toSecond(LocalDate date, java.time.ZoneId zoneId) {
            return date.atStartOfDay(zoneId).toEpochSecond();
        }

        @Override
        public LocalDate ofSecond(long second) {
            return ofSecond(second, java.time.ZoneId.systemDefault());
        }

        @Override
        public LocalDate ofSecond(long second, String zoneId) {
            return ofSecond(second, getZoneId(zoneId));
        }

        private LocalDate ofSecond(long second, java.time.ZoneId zoneId) {
            return LocalDate.ofInstant(Instant.ofEpochSecond(second), zoneId);
        }

        @Override
        public long toMillisecond(LocalDate date) {
            return toMillisecond(date, java.time.ZoneId.systemDefault());
        }

        @Override
        public long toMillisecond(LocalDate date, String zoneId) {
            return toMillisecond(date, getZoneId(zoneId));
        }

        private long toMillisecond(LocalDate date, java.time.ZoneId zoneId) {
            return date.atStartOfDay(zoneId).toInstant().toEpochMilli();
        }

        @Override
        public LocalDate ofMillisecond(long millisecond) {
            return ofMillisecond(millisecond, java.time.ZoneId.systemDefault());
        }

        @Override
        public LocalDate ofMillisecond(long millisecond, String zoneId) {
            return ofMillisecond(millisecond, getZoneId(zoneId));
        }

        private LocalDate ofMillisecond(long millisecond, java.time.ZoneId zoneId) {
            return LocalDate.ofInstant(Instant.ofEpochMilli(millisecond), zoneId);
        }

        @Override
        public java.util.Date toDate(LocalDate date) {
            return toDate(date, java.time.ZoneId.systemDefault());
        }

        @Override
        public java.util.Date toDate(LocalDate date, String zoneId) {
            return toDate(date, getZoneId(zoneId));
        }

        private Date toDate(LocalDate date, java.time.ZoneId zoneId) {
            return java.util.Date.from(date.atStartOfDay(zoneId).toInstant());
        }

        @Override
        public LocalDate ofDate(java.util.Date date) {
            return ofDate(date, java.time.ZoneId.systemDefault());
        }

        @Override
        public LocalDate ofDate(java.util.Date date, String zoneId) {
            return ofDate(date, getZoneId(zoneId));
        }

        private LocalDate ofDate(Date date, java.time.ZoneId zoneId) {
            return date.toInstant().atZone(zoneId).toLocalDate();
        }
    };

    public static final I<LocalTime> Time = new I<LocalTime>() {
        @Override
        public String format(LocalTime time, String pattern) {
            return getFormatter(pattern).format(time);
        }

        @Override
        public LocalTime parse(String text, String pattern) {
            return LocalTime.parse(text, getFormatter(pattern));
        }
    };

    public static final I<LocalDateTime> DateTime = new I<LocalDateTime>() {
        @Override
        public String format(LocalDateTime dateTime, String pattern) {
            return getFormatter(pattern).format(dateTime);
        }

        @Override
        public LocalDateTime parse(String text, String pattern) {
            return LocalDateTime.parse(text, getFormatter(pattern));
        }

        @Override
        public long toSecond(LocalDateTime dateTime) {
            return toSecond(dateTime, java.time.ZoneId.systemDefault());
        }

        @Override
        public long toSecond(LocalDateTime dateTime, String zoneId) {
            return toSecond(dateTime, getZoneId(zoneId));
        }

        private long toSecond(LocalDateTime dateTime, java.time.ZoneId zoneId) {
            return dateTime.atZone(zoneId).toEpochSecond();
        }

        @Override
        public LocalDateTime ofSecond(long second) {
            return ofSecond(second, java.time.ZoneId.systemDefault());
        }

        @Override
        public LocalDateTime ofSecond(long second, String zoneId) {
            return ofSecond(second, getZoneId(zoneId));
        }

        private LocalDateTime ofSecond(long second, java.time.ZoneId zoneId) {
            return LocalDateTime.ofInstant(Instant.ofEpochSecond(second), zoneId);
        }

        @Override
        public long toMillisecond(LocalDateTime dateTime) {
            return toMillisecond(dateTime, java.time.ZoneId.systemDefault());
        }

        @Override
        public long toMillisecond(LocalDateTime dateTime, String zoneId) {
            return toMillisecond(dateTime, getZoneId(zoneId));
        }

        private long toMillisecond(LocalDateTime dateTime, java.time.ZoneId zoneId) {
            return dateTime.atZone(zoneId).toInstant().toEpochMilli();
        }

        @Override
        public LocalDateTime ofMillisecond(long millisecond) {
            return ofMillisecond(millisecond, java.time.ZoneId.systemDefault());
        }

        @Override
        public LocalDateTime ofMillisecond(long millisecond, String zoneId) {
            return ofMillisecond(millisecond, getZoneId(zoneId));
        }

        private LocalDateTime ofMillisecond(long millisecond, java.time.ZoneId zoneId) {
            return LocalDateTime.ofInstant(Instant.ofEpochMilli(millisecond), zoneId);
        }

        @Override
        public java.util.Date toDate(LocalDateTime dateTime) {
            return toDate(dateTime, java.time.ZoneId.systemDefault());
        }

        @Override
        public java.util.Date toDate(LocalDateTime dateTime, String zoneId) {
            return toDate(dateTime, getZoneId(zoneId));
        }

        private Date toDate(LocalDateTime dateTime, java.time.ZoneId zoneId) {
            return java.util.Date.from(dateTime.atZone(zoneId).toInstant());
        }

        @Override
        public LocalDateTime ofDate(java.util.Date date) {
            return ofDate(date, java.time.ZoneId.systemDefault());
        }

        @Override
        public LocalDateTime ofDate(java.util.Date date, String zoneId) {
            return ofDate(date, getZoneId(zoneId));
        }

        private LocalDateTime ofDate(Date date, java.time.ZoneId zoneId) {
            return date.toInstant().atZone(zoneId).toLocalDateTime();
        }
    };

    // 时区
    public interface ZoneId {
        /**
         * 【亚洲】中国标准时间
         */
        String AsiaShanghai = "Asia/Shanghai";

        /**
         * 【欧洲】格林尼治标准时间
         */
        String EuropeLondon = "Europe/London";

        /**
         * 【美洲】东部标准时间
         */
        String AmericaNewYork = "America/New_York";

        /**
         * 【非洲】南非标准时间
         */
        String AfricaJohannesburg = "Africa/Johannesburg";

        /**
         * 【大洋洲】澳大利亚东部标准时间
         */
        String AustraliaSydney = "Australia/Sydney";
    }

    // 时区
    private static final Map<String, java.time.ZoneId> zoneIdMap = new HashMap<>(16, 1f);

    // 日期时间格式化器
    private static final Map<String, DateTimeFormatter> formatterMap = new HashMap<>(128, 1f);

    public static java.time.ZoneId getZoneId(String zoneId) {
        java.time.ZoneId z = zoneIdMap.get(zoneId);
        if (Objects.nonNull(z)) {
            return z;
        }

        synchronized (zoneIdMap) {
            z = zoneIdMap.get(zoneId);
            if (Objects.isNull(z)) {
                z = java.time.ZoneId.of(zoneId);
                zoneIdMap.put(zoneId, z);
            }
        }
        return z;
    }

    public static DateTimeFormatter getFormatter(String pattern) {
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

    public static interface I<T> {
        default String format(T t, String pattern) {
            throw new UnsupportedOperationException();
        }

        default T parse(String text, String pattern) {
            throw new UnsupportedOperationException();
        }

        default long toSecond(T t) {
            throw new UnsupportedOperationException();
        }

        default long toSecond(T t, String zoneId) {
            throw new UnsupportedOperationException();
        }

        default T ofSecond(long second) {
            throw new UnsupportedOperationException();
        }

        default T ofSecond(long second, String zoneId) {
            throw new UnsupportedOperationException();
        }

        default long toMillisecond(T t) {
            throw new UnsupportedOperationException();
        }

        default long toMillisecond(T t, String zoneId) {
            throw new UnsupportedOperationException();
        }

        default T ofMillisecond(long millisecond) {
            throw new UnsupportedOperationException();
        }

        default T ofMillisecond(long millisecond, String zoneId) {
            throw new UnsupportedOperationException();
        }

        default Date toDate(T t) {
            throw new UnsupportedOperationException();
        }

        default Date toDate(T t, String zoneId) {
            throw new UnsupportedOperationException();
        }

        default T ofDate(Date date) {
            throw new UnsupportedOperationException();
        }

        default T ofDate(Date date, String zoneId) {
            throw new UnsupportedOperationException();
        }
    }

}
