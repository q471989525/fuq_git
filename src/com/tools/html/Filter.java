package com.tools.html;

import org.w3c.dom.Node;

/**
 * 过滤无用的标签
 * @author ljg
 */
public abstract class Filter {

    protected Filter nextFilter;

    public Filter() {
    }

    /**
     *
     * @param nextFilter 下一个过滤器
     */
    public Filter(Filter nextFilter) {
        this.nextFilter = nextFilter;
    }

    public void setNextFilter(Filter nextFilter) {
        this.nextFilter = nextFilter;
    }

    public Filter getNextFilter() {
        return nextFilter;
    }

    /**
     * 去除node里面某些符合条件的节点
     * @param node
     * @return 若可以去除该节点返回true，否则返回false
     */
    public abstract boolean isFilter(Node node);
}
