/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tools.http;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取HttpServletRequest中的信息
 *
 * @author Fuq'
 */
public class HeaderUtils {

    /**
     * 得到真实的ip
     *
     * @param request
     * @return
     */
    public String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
