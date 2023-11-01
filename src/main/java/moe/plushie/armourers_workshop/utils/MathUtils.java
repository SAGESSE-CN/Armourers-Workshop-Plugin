package moe.plushie.armourers_workshop.utils;

public class MathUtils {

    private static final int[] MULTIPLY_DE_BRUIJN_BIT_POSITION = new int[]{0, 1, 28, 2, 29, 14, 24, 3, 30, 22, 20, 15, 25, 17, 4, 8, 31, 27, 13, 23, 21, 19, 16, 7, 26, 12, 18, 6, 11, 5, 10, 9};


    public static int smallestEncompassingPowerOfTwo(int i) {
        int j = i - 1;
        j |= j >> 1;
        j |= j >> 2;
        j |= j >> 4;
        j |= j >> 8;
        j |= j >> 16;
        return j + 1;
    }

    public static boolean isPowerOfTwo(int i) {
        return i != 0 && (i & i - 1) == 0;
    }

    public static int ceillog2(int i) {
        i = isPowerOfTwo(i) ? i : smallestEncompassingPowerOfTwo(i);
        return MULTIPLY_DE_BRUIJN_BIT_POSITION[(int)((long)i * 125613361L >> 27) & 0x1F];
    }

    public static int log2(int i) {
        return ceillog2(i) - (isPowerOfTwo(i) ? 0 : 1);
    }

}
