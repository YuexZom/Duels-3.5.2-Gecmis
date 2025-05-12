/*
 * Decompiled with CFR 0.153-SNAPSHOT (d6f6758-dirty).
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.StringUtils
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 */
package me.realized.duels.util;

import java.util.Collection;
import java.util.List;
import java.util.TreeMap;
import java.util.regex.Pattern;
import me.realized.duels.util.reflect.ReflectionUtil;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;

public final class StringUtil {
    private static final Pattern ALPHANUMERIC = Pattern.compile("^[a-zA-Z0-9_]+$");
    private static final TreeMap<Integer, String> ROMAN_NUMERALS = new TreeMap();
    private static final boolean COMMONS_LANG3;

    private StringUtil() {
    }

    public static String toRoman(int number) {
        if (number <= 0) {
            return String.valueOf(number);
        }
        int key = ROMAN_NUMERALS.floorKey(number);
        if (number == key) {
            return ROMAN_NUMERALS.get(number);
        }
        return ROMAN_NUMERALS.get(key) + StringUtil.toRoman(number - key);
    }

    public static String fromList(List<?> list) {
        StringBuilder builder = new StringBuilder();
        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); ++i) {
                builder.append(list.get(i).toString()).append(i + 1 != list.size() ? "\n" : "");
            }
        }
        return builder.toString();
    }

    public static String parse(Location location) {
        return "(" + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ() + ")";
    }

    public static String color(String input) {
        return ChatColor.translateAlternateColorCodes((char)'&', (String)input);
    }

    public static List<String> color(List<String> input) {
        input.replaceAll(s -> {
            s = StringUtil.color(s);
            return s;
        });
        return input;
    }

    public static boolean isAlphanumeric(String input) {
        return ALPHANUMERIC.matcher(input.replace(" ", "")).matches();
    }

    public static String join(Object[] array, String separator, int startIndex, int endIndex) {
        if (COMMONS_LANG3) {
            return org.apache.commons.lang3.StringUtils.join(array, separator, startIndex, endIndex);
        }
        return StringUtils.join((Object[])array, (String)separator, (int)startIndex, (int)endIndex);
    }

    public static String join(Collection<?> collection, String separator) {
        if (COMMONS_LANG3) {
            return org.apache.commons.lang3.StringUtils.join(collection, separator);
        }
        return StringUtils.join(collection, (String)separator);
    }

    public static String capitalize(String s) {
        if (COMMONS_LANG3) {
            return org.apache.commons.lang3.StringUtils.capitalize(s);
        }
        return StringUtils.capitalize((String)s);
    }

    public static boolean containsIgnoreCase(String str, String searchStr) {
        if (COMMONS_LANG3) {
            return org.apache.commons.lang3.StringUtils.containsIgnoreCase(str, searchStr);
        }
        return StringUtils.containsIgnoreCase((String)str, (String)searchStr);
    }

    static {
        ROMAN_NUMERALS.put(1000, "M");
        ROMAN_NUMERALS.put(900, "CM");
        ROMAN_NUMERALS.put(500, "D");
        ROMAN_NUMERALS.put(400, "CD");
        ROMAN_NUMERALS.put(100, "C");
        ROMAN_NUMERALS.put(90, "XC");
        ROMAN_NUMERALS.put(50, "L");
        ROMAN_NUMERALS.put(40, "XL");
        ROMAN_NUMERALS.put(10, "X");
        ROMAN_NUMERALS.put(9, "IX");
        ROMAN_NUMERALS.put(5, "V");
        ROMAN_NUMERALS.put(4, "IV");
        ROMAN_NUMERALS.put(1, "I");
        COMMONS_LANG3 = ReflectionUtil.getClassUnsafe(" org.apache.commons.lang3.StringUtils") != null;
    }
}

