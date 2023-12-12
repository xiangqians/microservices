package org.xiangqian.microservices.common.util;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * @author xiangqian
 * @date 19:34 2023/06/28
 */
public class DateUtilTest {

    private String shanghaiZoneId = "Asia/Shanghai";
    private String newYorkZoneId = "America/New_York";
    private String londonZoneId = "Europe/London";

    @Test
    public void nowZonedDateTime() {
        ZonedDateTime zonedDateTime = DateUtil.now(ZonedDateTime.class);
        System.out.println(zonedDateTime);

        zonedDateTime = DateUtil.now(newYorkZoneId, ZonedDateTime.class);
        System.out.println(zonedDateTime);

        zonedDateTime = DateUtil.now(londonZoneId, ZonedDateTime.class);
        System.out.println(zonedDateTime);

        // 2023-11-14T08:56:31.165162700+08:00  [Asia/Shanghai]
        // 2023-11-13T19:56:31.166165200-05:00  [America/New_York]
        // 2023-11-14T00:56:31.167168Z          [Europe/London]
    }

    @Test
    public void nowLocalDateTime() {
        // Asia/Shanghai
        LocalDateTime localDateTime = DateUtil.now(LocalDateTime.class);
        System.out.format("%s\t\t%s", ZoneId.systemDefault(), localDateTime).println();

        String zoneId = newYorkZoneId;
        localDateTime = DateUtil.now(zoneId, LocalDateTime.class);
        System.out.format("%s\t%s", zoneId, localDateTime).println();

        zoneId = londonZoneId;
        localDateTime = DateUtil.now(zoneId, LocalDateTime.class);
        System.out.format("%s\t\t%s", zoneId, localDateTime).println();

        // Asia/Shanghai		2023-11-14T09:00:05.760496900
        // America/New_York	    2023-11-13T20:00:05.762501900
        // Europe/London		2023-11-14T01:00:05.763505100
    }

    @Test
    public void format() {
        String pattern = "yyyy/MM/dd HH:mm:ss.SSS";

        String format = DateUtil.format(DateUtil.now(ZonedDateTime.class), pattern);
        System.out.format("%s\t\t%s", ZoneId.systemDefault(), format).println();

        format = DateUtil.format(DateUtil.now(LocalDateTime.class), pattern);
        System.out.format("%s\t\t%s", ZoneId.systemDefault(), format).println();

        String zoneId = newYorkZoneId;
        format = DateUtil.format(DateUtil.now(zoneId, LocalDateTime.class), pattern);
        System.out.format("%s\t%s", zoneId, format).println();

        zoneId = londonZoneId;
        format = DateUtil.format(DateUtil.now(zoneId, LocalDateTime.class), pattern);
        System.out.format("%s\t\t%s", zoneId, format).println();

        // Asia/Shanghai		2023/11/14 09:04:28.474
        // Asia/Shanghai		2023/11/14 09:04:28.479
        // America/New_York	    2023/11/13 20:04:28.480
        // Europe/London		2023/11/14 01:04:28.484
    }

    @Test
    public void parse() {
        String text = "2023/11/13 11:20:28.810";
        String pattern = "yyyy/MM/dd HH:mm:ss.SSS";

        ZonedDateTime zonedDateTime = DateUtil.parse(text, pattern, LocalDateTime.class).atZone(ZoneId.systemDefault());
        System.out.println(zonedDateTime);

        // 时区转换
        zonedDateTime = zonedDateTime.withZoneSameInstant(ZoneId.of(newYorkZoneId));
        System.out.println(zonedDateTime);

        // 2023/11/13T11:20:28.810+08:00    [Asia/Shanghai]
        // 2023/11/12T22:20:28.810-05:00    [America/New_York]

        LocalDateTime localDateTime = DateUtil.parse("2023/11/14T08:45:26.694", "yyyy/MM/dd'T'HH:mm:ss.SSS", LocalDateTime.class);
        System.out.println(localDateTime);
    }

    @Test
    public void toMillisecond() {
        LocalDateTime localDateTime = DateUtil.parse("2023/11/14T08:45:26.694", "yyyy/MM/dd'T'HH:mm:ss.SSS", LocalDateTime.class);

        String zoneId = ZoneId.systemDefault().getId();
        long ms = DateUtil.toMillisecond(localDateTime);
        System.out.format("%s\t\t%s", zoneId, ms).println();
        System.out.format("%s\t\t%s", zoneId, DateUtil.ofMillisecond(ms, LocalDateTime.class)).println();

        zoneId = newYorkZoneId;
        ms = DateUtil.toMillisecond(localDateTime, zoneId);
        System.out.format("%s\t%s", zoneId, ms).println();
        System.out.format("%s\t%s", zoneId, DateUtil.ofMillisecond(ms, zoneId, LocalDateTime.class)).println();

        zoneId = londonZoneId;
        ms = DateUtil.toMillisecond(localDateTime, zoneId);
        System.out.format("%s\t\t%s", zoneId, ms).println();
        System.out.format("%s\t\t%s", zoneId, DateUtil.ofMillisecond(ms, zoneId, LocalDateTime.class)).println();
    }

}
