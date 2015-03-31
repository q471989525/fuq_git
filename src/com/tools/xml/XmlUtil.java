package com.tools.xml;

import nu.xom.*;
import java.util.*;
import java.io.*;

/**
 * 解析插件xml
 * @author ljg
 */
public class XmlUtil {

    private InputStream inputStream;

    /**
     * 
     * @param inputStream inputStream 
     */
    public XmlUtil(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    /**
     * 获取根节点
     * @return Element
     * @throws Exception 
     */
    public Element getRootElement() throws Exception {
        Document doc = new Builder().build(inputStream);
        Element element = doc.getRootElement();
        return element;
    }

    /**
     * 获取节点指定名称的子节点
     * @param name
     * @param element
     * @return  Elements
     */
    public static Elements getElementsByName(String name, Element element) {
        return element.getChildElements(name);
    }

    /**
     * 获取节点属性
     * @param element
     * @return   Map
     */
    public static Map<String, String> getElementAttr(Element element) {
        Map<String, String> attr = new HashMap<String, String>();
        int count = element.getAttributeCount();
        for (int i = 0; i < count; i++) {
            Attribute attribute = element.getAttribute(i);
            attr.put(attribute.getLocalName(), attribute.getValue());
        }
        return attr;
    }
}
