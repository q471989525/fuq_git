package com.tools.properties;

import java.io.InputStream;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;


import org.apache.log4j.Logger;

/**
 * <p>Title: </p>
 *
 * <p>Description: 读取properties文件</p>
 *
 * <p>Copyright: </p>
 *
 * <p>Company: unioncast</p>
 *
 * @author
 * @version 1.0
 */
public class SystemConfigPropertyFile {

    private Logger log = Logger.getLogger(getClass());
    private String fileNameWithPath = "config.properties";	//默认值
    private Properties initProps = new Properties();

    public SystemConfigPropertyFile(String fileNameWithPath) {
        String temp = fileNameWithPath.toUpperCase().trim();
        int index = temp.lastIndexOf(".");
        if (index != -1) {
            if (!temp.substring(index + 1, temp.length()).equals("PROPERTIES")) {
                log.error(fileNameWithPath + " 文件不是属性文件,没有properties为后缀名");
            }
        } else {
            log.error(fileNameWithPath + " 文件不是属性文件,没有properties为后缀名");
        }

        this.fileNameWithPath = fileNameWithPath;
        init();
    }

    public SystemConfigPropertyFile() {
        init();
    }

    private void init() {
        InputStream in = null;
        try {
            in = getClass().getClassLoader().getResourceAsStream(fileNameWithPath);
            initProps.load(in);
        } catch (Exception e) {
            log.error("Error reading " + fileNameWithPath);
            if (log.isDebugEnabled()) {
                e.printStackTrace();
            }
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
            }
        }

    }

    /**
     * 取配置文件指定键的值
     *
     * @param keys 输入键
     * @return 对应值 ,不存在则返回 null ,不是空串
     */
    public String getValue(String keys) {

        String values = null;
        if (initProps != null) {
            values = initProps.getProperty(keys);
            if (values != null) {
                values = values.trim();
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("配置项: " + keys + " 不存在!");
                }
            }

        }
        return values;
    }

    /**
     * 取配置文件指定键的值
     *
     * @param keys 输入键
     * @param forceRefresh 强制刷新
     * @return 对应值 ,不存在则返回 null ,不是空串
     */
    public String getValue(String keys, boolean forceRefresh) {
        if (forceRefresh) {
            initProps = null;
            initProps = new Properties();
            init();
        }
        String values = null;
        if (initProps != null) {
            values = initProps.getProperty(keys);
            if (values != null) {
                values = values.trim();
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("配置项: " + keys + " 不存在!");
                }
            }

        }
        return values;
    }

    public Hashtable getAllValue() {
        Hashtable<String, Object> table = new Hashtable<String, Object>();
        if (initProps != null && initProps.size() > 0) {
            Iterator it = initProps.keySet().iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                if (key != null) {
                    key = key.trim();
                } else {
                    if (log.isDebugEnabled()) {
                        log.debug("配置项: " + key + " 不存在!");
                    }
                    continue;
                }

                String value = (String) initProps.get(key);
                if (value != null) {
                    value = value.trim();
                } else if (log.isDebugEnabled()) {
                    log.debug("配置项: " + key + " 不存在!");
                }
                table.put(key, initProps.get(key));
            }

        }
        return table;

    }
}
