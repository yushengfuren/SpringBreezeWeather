package com.elysia.springbreezeweather.util;

public class StringUtil {
    public static String extractTime(String datetime) {
        // 使用正则表达式匹配时间部分
        String regex = "T(\\d{2}:\\d{2})";
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(regex);
        java.util.regex.Matcher matcher = pattern.matcher(datetime);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            return null; // 如果不匹配，返回null或其他合适的默认值
        }
    }
}
