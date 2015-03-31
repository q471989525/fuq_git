package com.tools.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.Locator;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;
import java.io.IOException;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * SAX方式解析XML文件
 * 优点：以文件流方式读取xml 树结构方式解析
 * 占用内存小
 *
 * 缺点：只能对当前节点进行操作
 * @createTime 2011-12-31
 * @author 付强
 */
public class SAXParseXml extends DefaultHandler {

    SAXParseXml() {
        super();
    }

    @Override
    public void setDocumentLocator(Locator locator) {
    }

    @Override
    public void startDocument() throws SAXException {

        System.out.println("*******开始解析文档*******");
    }

    @Override
    public void endDocument() throws SAXException {
        System.out.println("*******文档解析结束*******");
    }

    @Override
    public void startPrefixMapping(String prefix, String uri) {
        System.out.println(" 前缀映射: " + prefix + " 开始!" + " 它的URI是:" + uri);
    }

    @Override
    public void endPrefixMapping(String prefix) {
        System.out.println(" 前缀映射: " + prefix + " 结束!");
    }

    @Override
    public void processingInstruction(String target, String instruction)
            throws SAXException {
    }

    @Override
    public void ignorableWhitespace(char[] chars, int start, int length) throws SAXException {
    }

    @Override
    public void skippedEntity(String name) throws SAXException {
    }

    @Override
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) {
        System.out.println("*******开始解析元素*******");
        System.out.println("元素名" + qName);
        for (int i = 0; i < atts.getLength(); i++) {
            System.out.println("元素名" + atts.getLocalName(i) + "属性值" + atts.getValue(i));
        }
    }

    @Override
    public void endElement(String namespaceURI, String localName, String fullName) {
        System.out.println("******元素解析结束********");
    }

    @Override
    public void characters(char[] chars, int start, int length) throws SAXException {
        //将元素内容累加到StringBuffer中
        StringBuilder sb = new StringBuilder();
        sb.append(chars, start, length);
        System.out.println("节点内容:" + sb);
    }

    public static void main(String args[]) {
        try {
            SAXParserFactory sf = SAXParserFactory.newInstance();
            SAXParser sp = sf.newSAXParser();
            SAXParseXml testsax = new SAXParseXml();
            sp.parse(new InputSource("D:testsimpleTestclassessimpleTesttest.xml"), testsax);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }



    }
}
