package org.forweb.drift.utils;

import java.util.Arrays;

public class ArrayUtils {

    public static <T> T[] concat(T[] first, T[] second) {
        try {
            T[] result = Arrays.copyOf(first, first.length + second.length);
            System.arraycopy(second, 0, result, first.length, second.length);
            return result;
        } catch (Exception e) {
            return first;
        }
    }
    public static int[] concat(int[] first, int[] second) {
        int[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

}
