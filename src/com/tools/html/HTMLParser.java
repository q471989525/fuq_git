package com.tools.html;

import javax.xml.transform.TransformerException;
import org.apache.xpath.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.*;
import org.xml.sax.*;
import org.cyberneko.html.parsers.DOMParser;

/**
 * html解析器 xPath取出元素
 *
 * @author
 */
public class HTMLParser extends DOMParser {

    //private Logger logger = Logger.getLogger(HTMLParser.class);
    public HTMLParser() {
    }

    /**
     * 获取html解析器，并加载输入内容
     *
     * @param is InputSource
     * @return 返回加载解析内容之后的解析器
     * @throws SAXException
     * @throws IOException
     */
    public static HTMLParser getParse(InputSource is) throws SAXException, IOException {
        HTMLParser dp = new HTMLParser();
        // dp.parse(is);
        return dp;
    }

    /**
     * 加载输入内容
     *
     * @param is InputSource
     * @return 返回加载解析内容之后的解析器
     * @throws SAXException
     * @throws IOException
     */
    public HTMLParser load(InputSource is) throws SAXException, IOException {
//        this.parse(is);
        return this;
    }

    /**
     * 获取html解析器
     *
     * @param content 网页全文
     * @return 返回加载解析内容之后的解析器
     * @throws SAXException
     * @throws IOException
     */
    public static HTMLParser getParse(String content) throws SAXException, IOException {
        return getParse(new InputSource(new StringReader(content)));
    }

    /**
     * 加载输入内容
     *
     * @param content 网页全文
     * @return
     * @throws SAXException
     * @throws IOException
     */
    public HTMLParser load(String content) throws SAXException, IOException {
        this.load(new InputSource(new StringReader(content)));
        return this;
    }

    /**
     * 遍历所有节点并过滤
     *
     * @param node
     * @param filter
     * @return 返回过滤之后的节点
     */
    public static Node filterNode(Node node, Filter filter) {
        NodeList nl = node.getChildNodes();
        int len = nl.getLength();
        for (int i = 0; i < len; i++) {
            Node child = nl.item(i);
            if (isFilter(child, filter)) {
                node.removeChild(child);
                len--;
                i--;
            } else {
                filterNode(child, filter);
            }
        }
        return node;
    }

    /**
     * 判断该节点是否被过滤
     *
     * @param node
     * @param filter
     * @return
     */
    public static boolean isFilter(Node node, Filter filter) {
        boolean isFilter = false;
        Filter cur = filter;
        while (cur != null) {
            if (cur.isFilter(node)) {
                isFilter = true;
                break;
            }
            cur = cur.getNextFilter();
        }
        return isFilter;
    }

    /**
     * 换行、
     *
     * @param node
     * @return
     */
    private static boolean line(Node node) {
        Node pn = node.getParentNode();
        if (pn != null && node.getNodeType() == Node.TEXT_NODE) {
            String pname = pn.getNodeName();
            if (pname.equals("P") || pname.equals("DIV")) {
                node.setTextContent(node.getTextContent() + "\r\n");
            }
        }
        if (node.getNodeName().equals("BR")) {
            Node previousSibling = node.getPreviousSibling();
            if (previousSibling != null) {
                previousSibling.setTextContent(previousSibling.getTextContent() + "\r\n");
            }
        }
        return false;
    }

    /**
     * 匹配xPath
     *
     * @param productsXpath
     * @param context
     * @return
     */
    public static String getXpathContent(String productsXpath, String context) throws SAXNotRecognizedException, SAXNotSupportedException, SAXException, IOException, TransformerException {
        String t = "";
        if (productsXpath.indexOf('@') == -1) {
            productsXpath = productsXpath.toUpperCase();
        }
        HTMLParser parser = new HTMLParser();
        //   parser.setFeature("http://xml.org/sax/features/namespaces", false);
        parser.load(context);
//        Document doc = parser.getDocument();
        //  NodeList products = XPathAPI.selectNodeList(doc, productsXpath);
        String[] filterTags = {"SCRIPT", "STYLE", "NOSCRIPT", "TEXTAREA", "#comment", "A", "INPUT", "SELECT"};
        TagFilter tf = new TagFilter(filterTags);
        //添加换行
        tf.setNextFilter(new Filter() {

            @Override
            public boolean isFilter(Node node) {
                return line(node);
            }
        });
//        if (products.getLength() > 0) {
//            for (int i = 0; i < products.getLength(); i++) {
//                Node node = filterNode(products.item(i), tf);//过滤节点
//                t += node.getTextContent();
//            }
//        }
        return t.trim();
    }

    /**
     * xpath取指定属性值
     *
     * @param productsXpath
     * @param context
     * @param attributes 属性名称
     * @return
     */
    public static String getXpathContentB(String productsXpath, String context, String attributes) throws SAXNotRecognizedException, SAXNotSupportedException, SAXException, IOException, TransformerException {
        String t = "";
        if (productsXpath.indexOf('@') == -1) {
            productsXpath = productsXpath.toUpperCase();
        }
        HTMLParser parser = new HTMLParser();
        //      parser.setFeature("http://xml.org/sax/features/namespaces", false);
        parser.load(context);
        //    Document doc = parser.getDocument();
        //    NodeList products = XPathAPI.selectNodeList(doc, productsXpath);
        String[] filterTags = {"SCRIPT", "STYLE", "NOSCRIPT", "TEXTAREA", "#comment", "INPUT", "SELECT"};
        TagFilter tf = new TagFilter(filterTags);
        //添加换行
        tf.setNextFilter(new Filter() {

            @Override
            public boolean isFilter(Node node) {
                return line(node);
            }
        });
        // if (products.getLength() > 0) {
        //    for (int i = 0; i < products.getLength(); i++) {
        //    Node node = filterNode(products.item(i), tf);//过滤节点
        //       t += node.getAttributes().getNamedItem(attributes);
        //   }
        //   }
        return t.trim();
    }

    /**
     * 匹配xPath 不过滤A标签
     *
     * @param productsXpath
     * @param context
     * @return
     */
    public static String getXpathContentA(String productsXpath, String context) throws SAXNotRecognizedException, SAXNotSupportedException, SAXException, IOException, TransformerException {
        String t = "";
        if (productsXpath.indexOf('@') == -1) {
            productsXpath = productsXpath.toUpperCase();
        }
        HTMLParser parser = new HTMLParser();
        //   parser.setFeature("http://xml.org/sax/features/namespaces", false);
        parser.load(context);
//        Document doc = parser.getDocument();
//        NodeList products = XPathAPI.selectNodeList(doc, productsXpath);
        String[] filterTags = {"SCRIPT", "STYLE", "NOSCRIPT", "TEXTAREA", "#comment", "INPUT", "SELECT"};
        TagFilter tf = new TagFilter(filterTags);
        //添加换行
        tf.setNextFilter(new Filter() {

            @Override
            public boolean isFilter(Node node) {
                return line(node);
            }
        });
//        if (products.getLength() > 0) {
//            for (int i = 0; i < products.getLength(); i++) {
//                Node node = filterNode(products.item(i), tf);//过滤节点
//                t += node.getTextContent();
//            }
//        }
        return t.trim();
    }

    /**
     * g根据xPath得到所有当前路径下所有元素集合
     *
     * @param productsXpath xPaht
     * @param context 全文
     * @return list 当前路径下所有子元素集合
     * @throws SAXNotRecognizedException
     * @throws SAXNotSupportedException
     * @throws SAXException
     * @throws IOException
     * @throws TransformerException
     */
    public static List getXpathContentC(String productsXpath, String context) throws SAXNotRecognizedException, SAXNotSupportedException, SAXException, IOException, TransformerException {
        List list = new ArrayList();
        if (productsXpath.indexOf('@') == -1) {
            productsXpath = productsXpath.toUpperCase();
        }
        HTMLParser parser = new HTMLParser();
//        parser.setFeature("http://xml.org/sax/features/namespaces", false);
        parser.load(context);
        //      Document doc = parser.getDocument();
        //   NodeList products = XPathAPI.selectNodeList(doc, productsXpath);
        String[] filterTags = {"SCRIPT", "STYLE", "NOSCRIPT", "TEXTAREA", "#comment", "INPUT", "SELECT"};
        TagFilter tf = new TagFilter(filterTags);
        //添加换行
        tf.setNextFilter(new Filter() {

            @Override
            public boolean isFilter(Node node) {
                return line(node);
            }
        });
//        if (products.getLength() > 0) {
//            for (int i = 0; i < products.getLength(); i++) {
//                Node node = filterNode(products.item(i), tf);//过滤节点
//                list.add(node.getTextContent());
//
//            }
//        }
        return list;
    }
//    public static void main(String[] args) throws Exception {
//        WebPage fetch = FetcherUtil.fetch("http://www.nbd.com.cn/articles/2011-12-11/622557.html");
//        String str = new String(fetch.getContent());
//        System.out.println(HTMLParser.getXpathContent("//*[@id=\"articleContent\"]", str));
//    }
}
