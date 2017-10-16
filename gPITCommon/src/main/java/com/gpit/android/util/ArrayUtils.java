package com.gpit.android.util;

/**
 * Created by Pieter Bruegel on 5/31/16.
 */
public class ArrayUtils {

    public static final int INDEX_NOT_FOUND = -1;

    public static void reverse(final Object[] array) {
        if(array == null) {
            return;
        }

        reverse(array, 0, array.length);
    }

    public static void reverse(final long[] array) {
        if(array == null) {
            return;
        }

        reverse(array, 0, array.length);
    }

    public static void reverse(final int[] array) {
        if(array == null) {
            return;
        }

        reverse(array, 0, array.length);
    }

    public static void reverse(final short[] array) {
        if(array == null) {
            return;
        }

        reverse(array, 0, array.length);
    }

    public static void reverse(final char[] array) {
        if(array == null) {
            return;
        }

        reverse(array, 0, array.length);
    }

    public static void reverse(final byte[] array) {
        if(array == null) {
            return;
        }

        reverse(array, 0, array.length);
    }

    public static void reverse(final double[] array) {
        if(array == null) {
            return;
        }

        reverse(array, 0, array.length);
    }

    public static void reverse(final float[] array) {
        if(array == null) {
            return;
        }

        reverse(array, 0, array.length);
    }

    public static void reverse(final boolean[] array) {
        if(array == null) {
            return;
        }

        reverse(array, 0, array.length);
    }

    public static void reverse(final boolean[] array, final int startIndexInclusive, final int endIndexExclusive) {
        if(array == null) {
            return;
        }
        int i = startIndexInclusive < 0 ? 0 : startIndexInclusive;
        int j = Math.min(array.length, endIndexExclusive) - 1;
        boolean tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }

    public static void reverse(final byte[] array, final int startIndexInclusive, final int endIndexExclusive) {
        if(array == null) {
            return;
        }
        int i = startIndexInclusive < 0 ? 0 : startIndexInclusive;
        int j = Math.min(array.length, endIndexExclusive) - 1;
        byte tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }

    public static void reverse(final char[] array, final int startIndexInclusive, final int endIndexExclusive) {
        if(array == null) {
            return;
        }
        int i = startIndexInclusive < 0 ? 0 : startIndexInclusive;
        int j = Math.min(array.length, endIndexExclusive) - 1;
        char tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }

    public static void reverse(final double[] array, final int startIndexInclusive, final int endIndexExclusive) {
        if(array == null) {
            return;
        }
        int i = startIndexInclusive < 0 ? 0 : startIndexInclusive;
        int j = Math.min(array.length, endIndexExclusive) - 1;
        double tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }

    public static void reverse(final float[] array, final int startIndexInclusive, final int endIndexExclusive) {
        if(array == null) {
            return;
        }
        int i = startIndexInclusive < 0 ? 0 : startIndexInclusive;
        int j = Math.min(array.length, endIndexExclusive) - 1;
        float tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }

    public static void reverse(final int[] array, final int startIndexInclusive, final int endIndexExclusive) {
        if(array == null) {
            return;
        }
        int i = startIndexInclusive < 0 ? 0 : startIndexInclusive;
        int j = Math.min(array.length, endIndexExclusive) - 1;
        int tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }

    public static void reverse(final long[] array, final int startIndexInclusive, final int endIndexExclusive) {
        if(array == null) {
            return;
        }
        int i = startIndexInclusive < 0 ? 0 : startIndexInclusive;
        int j = Math.min(array.length, endIndexExclusive) - 1;
        long tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }

    public static void reverse(final Object[] array, final int startIndexInclusive, final int endIndexExclusive) {
        if(array == null) {
            return;
        }
        int i = startIndexInclusive < 0 ? 0 : startIndexInclusive;
        int j = Math.min(array.length, endIndexExclusive) - 1;
        Object tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }

    public static void reverse(final short[] array, final int startIndexInclusive, final int endIndexExclusive) {
        if(array == null) {
            return;
        }
        int i = startIndexInclusive < 0 ? 0 : startIndexInclusive;
        int j = Math.min(array.length, endIndexExclusive) - 1;
        short tmp;
        while (j > i) {
            tmp = array[j];
            array[j] = array[i];
            array[i] = tmp;
            j--;
            i++;
        }
    }
}
