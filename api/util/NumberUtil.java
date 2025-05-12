/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 */
package me.realized.duels.util;

import java.util.OptionalInt;

public final class NumberUtil {
    public static OptionalInt parseInt(String s) {
        if (s == null) {
            return OptionalInt.empty();
        }
        int result = 0;
        boolean negative = false;
        int i = 0;
        int len = s.length();
        int limit = -2147483647;
        if (len > 0) {
            char firstChar = s.charAt(0);
            if (firstChar < '0') {
                if (firstChar == '-') {
                    negative = true;
                    limit = Integer.MIN_VALUE;
                } else if (firstChar != '+') {
                    return OptionalInt.empty();
                }
                if (len == 1) {
                    return OptionalInt.empty();
                }
                ++i;
            }
            int multmin = limit / 10;
            while (i < len) {
                int digit;
                if ((digit = Character.digit(s.charAt(i++), 10)) < 0) {
                    return OptionalInt.empty();
                }
                if (result < multmin) {
                    return OptionalInt.empty();
                }
                if ((result *= 10) < limit + digit) {
                    return OptionalInt.empty();
                }
                result -= digit;
            }
        } else {
            return OptionalInt.empty();
        }
        return OptionalInt.of(negative ? result : -result);
    }

    public static int getChange(int k, int winnerRating, int loserRating) {
        double wr = NumberUtil.r(winnerRating);
        double lr = NumberUtil.r(loserRating);
        return (int)Math.floor((double)k * (1.0 - wr / (wr + lr)));
    }

    private static double r(int rating) {
        return Math.pow(10.0, (double)rating / 400.0);
    }

    public static boolean isLower(String version, String otherVersion) {
        version = version.replace("-SNAPSHOT", "").replace(".", "");
        otherVersion = otherVersion.replace("-SNAPSHOT", "").replace(".", "");
        return NumberUtil.parseInt(version).orElse(0) < NumberUtil.parseInt(otherVersion).orElse(0);
    }

    private NumberUtil() {
    }
}

