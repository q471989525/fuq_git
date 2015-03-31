

package com.tools.json;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fuq
 */
public class JSONUtil {
    
/**
     * json字符串转换实体
     * @param jsonStr
     * @param cls
     * @return 
     */
    public static Object json2Bean(String jsonStr, Class<?> cls) {
        net.sf.json.JSONObject jb = net.sf.json.JSONObject.fromObject(jsonStr);
        Object obj = net.sf.json.JSONObject.toBean(jb, cls);
        return obj;
    }

    /**
     * json字符串转换list
     * @param jsonListStr jsonListStr
     * @param cls class
     * @return List&it;cls&gt;
     */
    public static List<Object> json2BeanList(String jsonListStr, Class<?> cls) {
        List<Object> list = new ArrayList<Object>();
        net.sf.json.JSONArray ja = net.sf.json.JSONArray.fromObject(jsonListStr);
        for (int i = 0; i < ja.size(); i++) {
            try {
                net.sf.json.JSONObject jb = ja.getJSONObject(i);
                Object obj = net.sf.json.JSONObject.toBean(jb, cls);
                list.add(obj);
            } catch (Exception e) {
            }
        }
        return list;
    }
    
}
