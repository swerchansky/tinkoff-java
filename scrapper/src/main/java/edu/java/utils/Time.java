package edu.java.utils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class Time {
    public static Timestamp getTimestamp(OffsetDateTime updatedDate) {
        return Timestamp.valueOf(updatedDate.atZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime());
    }

    public static OffsetDateTime getOffsetDateTime(java.sql.Timestamp timestamp) {
        LocalDateTime localDateTime = timestamp.toLocalDateTime();
        return OffsetDateTime.of(localDateTime, ZoneOffset.of("Z"));
    }
}
