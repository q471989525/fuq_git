/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tools.other;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

/**
 * copyright &copy;2011 www.pgia.net <br/>
 * copyright 磐基讯息 2012-2-6<br/>
 * @author fuq
 * @version 1.00
 */
public class Reflection {
    //需要反射的实体类

    private Class c;

    /**
     * 通过反射将提取出来的值赋到实体中
     * @param map 值的名称：值
     * @return 传入对象类别的实体
     * @throws Exception
     */
    public Object makeObject(Map<String, String> map) throws Exception {
        Object obj = c.newInstance();
        Iterator<String> iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            String className = iterator.next();
            String value = map.get(className);
            className = "set" + className.substring(0, 1).toUpperCase() + className.substring(1, className.length());
            try {
                Method method = c.getMethod(className, String.class); //反射  （方法名，参数）
                method.invoke(obj, null); //调用反射的方法
            } catch (Exception e) {
                // System.out.println(className+":"+c);
            }
        }
        return obj;
    }
}
