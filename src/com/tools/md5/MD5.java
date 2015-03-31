package com.tools.md5;

import java.security.*;
import org.apache.log4j.Logger;

/**
 * MD5加密
 */
public class MD5 {

    private static Logger logger = Logger.getLogger(MD5.class);

    public static String md5(String input) {
        byte[] in = input.getBytes();
        String output = "";
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(in);
            output = byte2String(md5.digest());
        } catch (NoSuchAlgorithmException ex) {
            logger.error("", ex);
        }
        return output;
    }

    private static String byte2String(byte[] digest) {
        String hash = "";
        for (int i = 0; i < digest.length; i++) {
            int temp;
            if (digest[i] < 0) {
                temp = 256 + digest[i];
            } else {
                temp = digest[i];
            }
            if (temp < 16) {
                hash += "0";
            }
            hash += Integer.toString(temp, 16);
        }
        return hash.toUpperCase();
    }
}
