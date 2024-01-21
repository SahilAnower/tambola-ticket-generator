package com.sahil.tambola.util;

import java.util.Arrays;
import java.util.List;

public class HelperUtils {
    public static List<Integer> getNumbersFromString(String s) {
        return Arrays.stream(s.split(",")).map(Integer::parseInt).toList();
    }

    public static String convert2DArrayToString(int[][] arr) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                stringBuilder.append(arr[i][j]).append(",");
            }
        }
        stringBuilder.setLength(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }
}
