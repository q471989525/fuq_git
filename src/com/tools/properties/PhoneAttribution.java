package com.tools.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 * 读取号码归属地模板
 */
public class PhoneAttribution {

    private static final Logger logger = Logger.getLogger(PhoneAttribution.class);
    private static final Properties initProps = new Properties();

    static {
        try {
            InputStream resourceAsStream = PhoneAttribution.class.getResourceAsStream("phone_attribution.properties");
            initProps.load(resourceAsStream);
        } catch (IOException ex) {
            logger.info("重要：读取号码归属地文件失败");
            logger.error("", ex);
        }
    }

    /**
     * 根据key得到属性文件的value
     *
     * @param key Key
     * @return
     */
    public static String getValue(String key) {
        return initProps.getProperty(key);
    }

    public static void main(String[] args) {
        System.out.println(PhoneAttribution.getValue("1333837"));
    }
}
