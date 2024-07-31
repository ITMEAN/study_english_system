package org.minh.testservice.util;

import java.time.LocalTime;

public class TimeUtil {
    public static long getCurrentTime() {
        return System.currentTimeMillis();
    }
    public static long convertToMMSS(String time) {
        String[] parts = time.split(":");
        int minutes = Integer.parseInt(parts[0]);
        int seconds = Integer.parseInt(parts[1]);
        return minutes * 60L + seconds;
    }

    public static String convertToHHMMSS(long time) {
        return LocalTime.MIN.plusSeconds(time).toString();
    }
}
