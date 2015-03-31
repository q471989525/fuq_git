/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tools.玩玩;

/**
 *
 * @author Fuq
 * @date 2013-6-28
 */
public class 生成实体类啊 {

    public static void main(String[] args) {
        String str = "ID,PROJ_ID,PROJ_CODE,BID_ID,BID_CODE,CONT_ID,CONT_CODE,CORP_B_ID,CORP_B,WORK_AREA,APPLY_TIME,APPLY_NO,ALTER_ORDER_NO,MONEY,REASON,ANNEX_COUNT,SUM_FLAG,SUM_TIME";
        String[] split = str.split(",");
        for (int i = 0; i < split.length; i++) {
            String t = split[i];
            if (!t.contains("_")) {
                System.out.println("private String " + t.toLowerCase() + ";");
            } else {

                String s = t.substring(t.indexOf("_"), t.indexOf("_") + 2);
                System.out.println("private String " + t.toLowerCase().replace(s.toLowerCase(), s.toUpperCase()).replaceAll("_", "") + ";");
            }
        }
        System.out.println(str.toLowerCase());
    }
}
