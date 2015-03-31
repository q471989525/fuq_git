package com.tools.html;

import org.w3c.dom.Node;

/**
 * 过滤标签
 * @author ljg
 */
public class TagFilter extends Filter {

    private String[] tagNames;

    /**
     *
     * @param nextFilter 下一个过滤器
     * @param tagNames 需要去除的标签名
     */
    public TagFilter(Filter nextFilter, String[] tagNames) {
        super(nextFilter);
        this.tagNames = tagNames;
    }

    public TagFilter(Filter nextFilter) {
        super(nextFilter);
    }

    public TagFilter() {
    }

    public TagFilter(String[] tagNames) {
        this.tagNames = tagNames;
    }

    public void setTagName(String[] tagNames) {
        this.tagNames = tagNames;
    }

    public String[] getTagName() {
        return tagNames;
    }

    @Override
    public boolean isFilter(Node node) {
        if (tagNames != null) {
            for (int i = 0; i < tagNames.length; i++) {
                String nodeName = node.getNodeName();
                if (nodeName.equals(tagNames[i])) {
                    return true;
                }
            }
        }
        return false;
    }
}
