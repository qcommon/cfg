package net.dblsaiko.qcommon.cfg.core.util;

public class ArrayUtil {

    @SafeVarargs
    public static <T> T[] arrayOf(T... values) {
        return values;
    }

    public static boolean[] arrayOfZ(boolean... values) {
        return values;
    }

}
