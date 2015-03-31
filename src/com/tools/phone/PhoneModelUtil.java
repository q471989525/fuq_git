package com.tools.phone;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

/**
 * 手机型号判断
 *
 * @author Fuq 2012-10-17
 */
public class PhoneModelUtil {

    private Logger logger = Logger.getLogger(getClass());

    /**
     * 得到访问的设备信息
     *
     * @param request
     * @return PhoneInfo实体 未取到则返回null;
     * @see PhoneInfo
     */
    public PhoneInfo getPhoneInfoByRequest(HttpServletRequest request) {
        String agent = request.getHeader("user-agent");
        if (agent == null || agent == "") {
            return null;
        }
        PhoneInfo phoneInfo = new PhoneInfo();
        phoneInfo.setBrowserInfo(getBrowserTyle(agent));
        phoneInfo.setPhoneModel(getPhoneTyle(agent));
        phoneInfo.setSystemInfo(getSystemInfo(agent));
        return phoneInfo;
    }

    /**
     * 判断浏览器类型
     *
     * @param agent 头信息中的user-agent
     * @return {Firefox,WebKit,QQ,UC,IE,未知}
     */
    private String getBrowserTyle(String agent) {

        if (agent.contains("Firefox")) {
            return "Firefox";
        } else if (agent.contains("AppleWebKit")) {
            return "WebKit";
        } else if (agent.contains("MQQBrowser")) {
            return "QQ";
        } else if (agent.contains("UC")) {
            return "UC";
        } else if (agent.contains("MSIE")) {
            return "IE";
        } else {
            return "未知";
        }
    }

    /**
     * 判断手机类型
     *
     * @param agent 头信息中的user-agent
     * @return {iPhone,Windows,MAC,设备名称}
     */
    private String getPhoneTyle(String agent) {
        if (agent.contains("iOS") || agent.contains("iPhone")) {
            return "iPhone";
        } else if (agent.contains("Windows CE")) {
            return "Windows Phone";
        } else if (agent.contains("Windows")) {
            return "Windows";
        } else if (agent.contains("Macintosh")) {
            return "MAC";
        } else {
            try {
                Pattern p = Pattern.compile(".+?zh-cn;(.+?)Build.*?");
                Matcher m1 = p.matcher(agent);
                if (m1.matches()) {
                    String trim = m1.group(1).trim();
                    trim=trim.replaceAll(";", "");
                    return trim;
                } else {
                    return "未知设备";
                }
            } catch (Exception ex) {
                logger.error("", ex);
                return "未知设备";
            }
        }
    }

    /**
     * 得到设备操作系统
     *
     * @param agent
     * @return {iPhone OS,iMac OS X,Android,Windows CE,Windows
     * NT,Windows,Linux,未知，Acnroid *.*.*}
     */
    private String getSystemInfo(String agent) {
        if (agent.contains("iPhone OS")) {
            return "iPhone OS";
        } else if (agent.contains("Mac OS")) {
            return "iMac OS X";
        } else if (agent.contains("Android")) {
            try {
                Pattern p = Pattern.compile(".+?U;(.+?)zh-cn;.*?");
                Matcher m1 = p.matcher(agent);
                if (m1.matches()) {
                    String trim = m1.group(1).trim();
                    trim=trim.replaceAll(";", "");
                    return trim;
                } else {
                    return "Android";
                }
            } catch (Exception ex) {
                logger.error("", ex);
                return "Android";
            }
        } else if (agent.contains("Windows CE")) {
            return "Windows CE";
        } else if (agent.contains("Windows NT")) {
            return "Windows NT";
        } else if (agent.contains("Windows")) {
            return "Windows";
        } else if (agent.contains("Linux")) {
            return "Linux";
        } else {
            return "未知";
        }
    }

    public static void main(String[] args) {
        String agent = "Mozilla/5.0 (Linux; U; Android 2.3.3; zh-cn; HTC Legend Build/GRI40) UC AppleWebKit/534.31 (KHTML, like Gecko) Mobile Safari/534.31";
        String agent1 = "Mozilla/5.0 (Linux; U; Android 2.3.5; zh-cn; MI-ONE Plus Build/GINGERBREAD) AppleWebKit/533.1 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1";
        String agent2 = "MQQBrowser/37 (iOS 4S; U; CPU like Mac OS X; zh-cn)";
        String agent3 = "Mozilla/5.0 (iPhone; CPU iPhone OS 5_0_1 like Mac OS X) AppleWebKit/534.46 (KHTML, like Gecko) Version/5.1 Mobile/9A406 Safari/7534.48.3";
        String agent4 = "Mozilla/5.0 (Linux; U; Android 4.0.3; zh-cn; HUAWEI C8812 Build/HuaweiC8812) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30 MicroMessenger/4.2.192";
        String agent5 = "Mozilla/4.0 (compatible; MSIE 4.01; Windows CE; PPC)/UCWEB8.2.0.116/31/800";
        String[] agentArray = {agent, agent1, agent2, agent3, agent4, agent5};
        PhoneModelUtil pmu = new PhoneModelUtil();
        for (int i = 0; i < agentArray.length; i++) {
            PhoneInfo phoneInfo = new PhoneInfo();
            phoneInfo.setBrowserInfo(pmu.getBrowserTyle(agentArray[i]));
            phoneInfo.setPhoneModel(pmu.getPhoneTyle(agentArray[i]));
            phoneInfo.setSystemInfo(pmu.getSystemInfo(agentArray[i]));
            System.out.println(phoneInfo.toString());
        }

    }
}
