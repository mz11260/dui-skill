package com.zm.utils;

public class StringUtils {
    private StringUtils() {}

    /**
     * 字符串倒转
     *
     * @param src src
     * @return reverse string
     */
    public static String reverse(String src) {
        StringBuilder sb = new StringBuilder(src);
        // 取字符串长度折中值（下标）
        int middleIndex = sb.length() >>> 1;

        // 循环置换高低位的值 折中循环置换
        // 低位从0开始递增循环，直到小于折中值
        // 高位从字符串长度-1开始递减，直到小于折中值
        for (int i = 0, j = sb.length() - 1; i < middleIndex; i++, j--) {
            // 先赋值高位待置换临时变量
            char temp = sb.charAt(i);
            // 取高位值置换低位值
            sb.setCharAt(i, sb.charAt(j));
            // 置换高位
            sb.setCharAt(j, temp);
        }
        return sb.toString();
    }
}
