/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.realized.duels.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateUtil {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("yy-MM-dd HH:mm:ss");

    private DateUtil() {
    }

    public static String formatDate(Date date) {
        return DATE_FORMAT.format(date);
    }

    public static String formatDatetime(long millis) {
        return TIMESTAMP_FORMAT.format(millis);
    }

    public static String format(long seconds) {
        if (seconds <= 0L) {
            return "updating...";
        }
        long years = seconds / 31556952L;
        long months = (seconds -= years * 31556952L) / 2592000L;
        long weeks = (seconds -= months * 2592000L) / 604800L;
        long days = (seconds -= weeks * 604800L) / 86400L;
        long hours = (seconds -= days * 86400L) / 3600L;
        long minutes = (seconds -= hours * 3600L) / 60L;
        seconds -= minutes * 60L;
        StringBuilder sb = new StringBuilder();
        if (years > 0L) {
            sb.append(years).append("yr");
        }
        if (months > 0L) {
            sb.append(months).append("mo");
        }
        if (weeks > 0L) {
            sb.append(weeks).append("w");
        }
        if (days > 0L) {
            sb.append(days).append("d");
        }
        if (hours > 0L) {
            sb.append(hours).append("h");
        }
        if (minutes > 0L) {
            sb.append(minutes).append("m");
        }
        if (seconds > 0L) {
            sb.append(seconds).append("s");
        }
        return sb.toString();
    }

    public static String formatMilliseconds(long ms) {
        if (ms < 1000L) {
            return "0 second";
        }
        long seconds = ms / 1000L + (long)(ms % 1000L > 0L ? 1 : 0);
        long years = seconds / 31556952L;
        long months = (seconds -= years * 31556952L) / 2592000L;
        long weeks = (seconds -= months * 2592000L) / 604800L;
        long days = (seconds -= weeks * 604800L) / 86400L;
        long hours = (seconds -= days * 86400L) / 3600L;
        long minutes = (seconds -= hours * 3600L) / 60L;
        seconds -= minutes * 60L;
        StringBuilder builder = new StringBuilder();
        if (years > 0L) {
            builder.append(years).append(years > 1L ? " years" : " year");
        }
        if (months > 0L) {
            if (years > 0L) {
                builder.append(" ");
            }
            builder.append(months).append(months > 1L ? " months" : " month");
        }
        if (weeks > 0L) {
            if (years + months > 0L) {
                builder.append(" ");
            }
            builder.append(weeks).append(weeks > 1L ? " weeks" : " week");
        }
        if (days > 0L) {
            if (years + months + weeks > 0L) {
                builder.append(" ");
            }
            builder.append(days).append(days > 1L ? " days" : " day");
        }
        if (hours > 0L) {
            if (years + months + weeks + days > 0L) {
                builder.append(" ");
            }
            builder.append(hours).append(hours > 1L ? " hours" : " hour");
        }
        if (minutes > 0L) {
            if (years + months + weeks + days + hours > 0L) {
                builder.append(" ");
            }
            builder.append(minutes).append(minutes > 1L ? " minutes" : " minute");
        }
        if (seconds > 0L) {
            if (years + months + weeks + days + hours + minutes > 0L) {
                builder.append(" ");
            }
            builder.append(seconds).append(seconds > 1L ? " seconds" : " second");
        }
        return builder.toString();
    }
}

