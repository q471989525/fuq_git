package com.tools.xml;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.util.List;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.QName;
import org.dom4j.io.SAXReader;

/**
 * dom4j解析xml
 * 适用于解析比较复杂的xml
 * 需要导入的jar
 * dom4j.jar
 * jaxen.jar
 * copyright &copy;2011 www.pgia.net <br/>
 * copyright 磐基讯息 2012-2-17<br/>
 * @author fuq
 * @version 1.00
 */
public abstract class Dom4jXmlParse {

    static Element e;
    static Document document;
    static Logger logger = Logger.getLogger(Dom4jXmlParse.class);

    /**
     * 从xml中解析出某个节点，并且这个节点包含子节点
     * 并调用抽象方法processNode(Node)对每个节点进行处理
     * @param nodeName 需要处理的节点的名字
     * @param file 需要读取得file
     * @throws DocumentException
     */
    public void getNodeByNodeName(String nodeName, File file) throws DocumentException {
        createSAXReader().read(file);
    }

    /**
     * 从xml中解析出某个节点，并且这个节点包含子节点
     * 并调用抽象方法processNode(Node)对每个节点进行处理
     * @param nodeName 需要处理的节点的名字
     * @param is xml输入流
     * @throws DocumentException
     */
    public void getNodeByNodeName(String nodeName, InputStream is) throws DocumentException {
        createSAXReader().read(is);
    }

    /**
     * 从xml中解析出某个节点，并且这个节点包含子节点
     * 并调用抽象方法processNode(Node)对每个节点进行处理
     * @param nodeName 需要处理的节点的名字
     * @param reader Reader
     * @throws DocumentException
     */
    public void getNodeByNodeName(String nodeName, Reader reader) throws DocumentException {
        createSAXReader().read(reader);
    }

    /**
     * 创建一个专用的SAXReader对象
     * @return SAXReader
     */
    SAXReader createSAXReader() {
        // 读取并解析XML文档
        // SAXReader就是一个管道，用一个流的方式，把xml文件读出来
        SAXReader reader = new SAXReader();
        // 重写DocumentFactory中的createelement方法，在创建element时调用方法processNode(Node node)解析每一个element
        //然后移除掉document中的元素，减少内存占用，适用于大且复杂的xml解析
        reader.setDocumentFactory(new DocumentFactory() {

            @Override
            public Element createElement(QName qname) {
                Element createElement = null;
                try {
                    createElement = super.createElement(qname);

                    if (e == null || qname.getName().equals("PubmedArticle")) {

                        if (e != null && e.getName().equals("PubmedArticle")) {
                            document.getRootElement().content().remove(e);
                            processNode((Node) e);
                            // System.out.println(e.asXML());
                        }
                        e = createElement;
                    }
                } catch (Exception ex) {
                    logger.error("", ex);
                }
                return createElement;
            }

            @Override
            public Document createDocument() {
                Document createDocument = super.createDocument();
                document = createDocument;
                return createDocument;
            }
        });
        return reader;
    }

    /**
     * 根据一个xpath找到第一个匹配的节点
     * @param xml 字符串格式的xml
     * @param xPath xpath
     * @return 如果找到则返回节点 否则返回null
     * @throws DocumentException
     */
    public Node readStringXmlReturnNode(String xml, String xPath) throws DocumentException {
        Document doc = null;
        doc = DocumentHelper.parseText(xml);
        Node node = doc.selectSingleNode(xPath);
        return node;
    }

    /**
     * 根据一个xpath找到所有匹配的节点
     * @param xml 字符串格式的xml
     * @param xPath xpath
     * @return 如果找到则返回节点List 否则返回null
     * @throws DocumentException
     */
    public List<Node> readStringXmlReturnNodeList(String xml, String xPath) throws DocumentException {
        Document doc = null;
        doc = DocumentHelper.parseText(xml);
        List<Node> nodes = doc.selectNodes(xPath);
        return nodes;
    }

    /**
     * 实现解析一个节点的不同方法
     * @param node 包含所有子节点的节点
     * @return
     */
    public abstract Object processNode(Node node);
}
