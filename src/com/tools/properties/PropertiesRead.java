/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tools.properties;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 *
 * @author Fuq 2012-7-27
 */
public class PropertiesRead {

    ResourceBundle rb = null;

    PropertiesRead() throws Exception {

        //URL resource = getClass().getResource("mwts_error_cord.properties");
        //  String path = null;
        //  if (resource != null) {
        // path = resource.getPath();
        try {
            InputStream in = getClass().getResourceAsStream("mwts_error_cord.properties");
            rb = new PropertyResourceBundle(in);
        } catch (Exception ex) {
            throw new Exception("文件 mwts_error_cord.properties 不存在!");
        }

        //  } else {

        //}

    }

    /**
     * 根据错误码得到错误信息
     *
     * @param code
     * @return
     */
    public String getMessageByCode(String code) {
        String string = "";
        try {
            string = rb.getString(code);
        } catch (MissingResourceException e) {
            string = "错误码未找到";
        }
        return string;
    }

    public static void main(String[] args) throws Exception {
        PropertiesRead gmwtsm = new PropertiesRead();
        System.out.println(gmwtsm.getMessageByCode("10001010"));
    }
}
