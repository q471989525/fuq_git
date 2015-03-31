package com.tools.json;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * ListToTreeJSON 将有树形结构的List转换为json
 *
 * @author Fuq
 * @date 2013-5-22
 */
public abstract class ListToTreeJSON<T> {

    Logger logger = Logger.getLogger(getClass());

    /**
     * 设置实体的id字段
     *
     * @param entity
     * @return
     */
    public abstract int getId(T entity);

    /**
     * 设置实体的Pid字段
     *
     * @param entity
     * @return
     */
    public abstract int getPid(T entity);
    /**
     * List
     */
    private List<T> list;

    /**
     * 将List转换为树形结构的JSON
     * <b>需要先实现getId和getPid抽象方法</b>
     *
     * @param list list
     * @return JSON String
     */
    public String List2Json(List<T> list) {
        this.list = list;
        String jsonStr = "";
        try {
            jsonStr = createJson(getRootList()).toString();
        } catch (Exception ex) {
            logger.error("", ex);
        }
        return jsonStr;
    }

    /**
     * 递归组合json
     * @param list 当前节点下的子节点Json
     * @return JSONArray
     * @throws JSONException 
     */
    private JSONArray createJson(List<T> list) throws JSONException {
        JSONArray jsonArray = new JSONArray();
        if (list == null) {
            return null;
        }
        for (T root : list) {
            JSONObject job = new JSONObject(root);
            List<T> cldList = getCldList(root);
            if (cldList != null) {
                job.put("children", createJson(cldList));
            }
            jsonArray.put(job);
        }
        return jsonArray;
    }

    /**
     * 得到所以根节点-（PID&ge;0）
     *
     * @param list 树List
     * @return list 根节点
     */
    private List<T> getRootList() {
        List<T> pList = null;
        for (T t : list) {
            if (getPid(t) <= 0) {
                if (pList == null) {
                    pList = new ArrayList<T>();
                }
                pList.add(t);
            }
        }
        return pList;
    }

    /**
     * 得到某个节点下的所有子节点 如果当前节点为子节点 返回null
     *
     * @param rootT 节点
     * @return
     */
    private List<T> getCldList(T rootT) {
        List<T> cldList = null;
        for (T t : list) {
            if (getPid(t) == getId(rootT)) {
                if (cldList == null) {
                    cldList = new ArrayList<T>();
                }
                cldList.add(t);
            }
        }
        return cldList;
    }

    /**
     * list转换为树形Map(只转换当前一级)
     *
     * @return map
     */
    private Map<Integer, List<T>> list2Map(List<T> list) {
        Map<Integer, List<T>> map = new TreeMap<Integer, List<T>>();
        for (T t : list) {
            map.put(new Integer(getId(t)), getCldList(t));
        }
        return map;
    }
}
