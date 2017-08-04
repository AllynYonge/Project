package com.king.reading.common.utils;

import java.io.File;
import java.util.Collection;
import java.util.Map;

/**
 * 辅助判断
 */
public class Check {

    public static boolean isNull(Object o) {
        return o == null;
    }

    public static boolean isNotNull(Object o) {
        return !isNull(o);
    }

    public static boolean isEmpty(CharSequence str) {
        return isNull(str) || str.length() == 0;
    }

    public static boolean isNotEmpty(CharSequence str) {
        return !isEmpty(str);
    }

    public static boolean isEmpty(File file) {
        return isNull(file) || !file.exists();
    }

    public static boolean isNotEmpty(File file) {
        return !isEmpty(file);
    }

    public static boolean isEmpty(Object[] os) {
        return isNull(os) || os.length == 0;
    }

    public static boolean isEmpty(Collection<?> l) {
        return isNull(l) || l.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> l) {
        return !isEmpty(l);
    }

    public static boolean isEmpty(Map<?, ?> m) {
        return isNull(m) || m.isEmpty();
    }

    public static boolean isNotEmpty(Map<?, ?> m) {
        return !isEmpty(m);
    }

    public static boolean isNumeric(String str) {
        if (Check.isEmpty(str)) {
            return false;
        }
        if (str.matches("[0-9]*")) {
            return true;
        }
        return false;
    }

}
