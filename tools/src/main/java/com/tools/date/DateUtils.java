package com.tools.date;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DateUtils {

    /**
     * 将LocalDateTime从一个时区转换到另一个时区
     *
     * @param sourceDateTime 源日期时间(无时区信息)
     * @param sourceZone     源时区(认为sourceDateTime属于此时区)
     * @param targetZone     目标时区
     * @return 转换后的LocalDateTime(目标时区的时间)
     */
    public static LocalDateTime convertBetweenTimeZones(
            LocalDateTime sourceDateTime,
            ZoneId sourceZone,
            ZoneId targetZone) {

        // 1. 将源LocalDateTime与源时区结合，创建ZonedDateTime
        ZonedDateTime sourceZonedDateTime = ZonedDateTime.of(sourceDateTime, sourceZone);

        // 2. 转换为目标时区的ZonedDateTime
        ZonedDateTime targetZonedDateTime = sourceZonedDateTime.withZoneSameInstant(targetZone);

        // 3. 返回目标时区的LocalDateTime
        return targetZonedDateTime.toLocalDateTime();
    }

    /**
     * 重载方法，使用字符串形式的时区ID
     *
     * @param sourceDateTime 源日期时间
     * @param sourceZoneId   源时区ID(如"Asia/Shanghai")
     * @param targetZoneId   目标时区ID(如"America/New_York")
     * @return 转换后的LocalDateTime
     */
    public static LocalDateTime convertBetweenTimeZones(
            LocalDateTime sourceDateTime,
            String sourceZoneId,
            String targetZoneId) {

        return convertBetweenTimeZones(
                sourceDateTime,
                ZoneId.of(sourceZoneId),
                ZoneId.of(targetZoneId)
        );
    }
}
