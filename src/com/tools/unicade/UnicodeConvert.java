package com.tools.unicade;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Unicode编码字符和可读字符之间转换
 * @author ljg
 */
public class UnicodeConvert {

    /**
     * 把包含Unicodes编码的String
     * 转成可读String
     * @param str
     * @return String
     */
    public static String unicode2String(String str) {

        Pattern p = Pattern.compile("\\\\u(\\w{4})");
        Matcher m = p.matcher(str);
        while (true) {
            if (m.find()) {
                str = m.replaceFirst(Character.toString((char) Integer.parseInt(m.group(1), 16)));
                m.reset(str);
            } else {
                break;
            }
        }
        return str;
    }

    /**
     * 转换html转义符
     * @param str
     * @return String
     */
    public static String htmlCode2String(String str) {
        Pattern p = Pattern.compile("\\&#(\\w+);");
        Matcher m = p.matcher(str);
        while (true) {
            if (m.find()) {
                str = m.replaceFirst(Character.toString((char) Integer.parseInt(m.group(1))));
                m.reset(str);
            } else {
                break;
            }
        }
        return str;
    }

    /**
     * 字符串转换html转义符
     * @param str
     * @return String
     */
    public static String String2Unicode(String str) {
        if (str == null) {
            return "";
        }
        int len = str.length();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            String s = Integer.toHexString((int) str.charAt(i));
            int length = s.length();
            switch (length) {
                case 1:
                    s = "000" + s;
                    break;
                case 2:
                    s = "00" + s;
                    break;
                case 3:
                    s = "0" + s;
                    break;
            }
            sb.append("\\u").append(s);
        }
        return sb.toString();
    }
}
