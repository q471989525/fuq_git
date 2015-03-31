package com.tools.phone;

/**
 * 手机信息实体
 *
 * @author Fuq 2012-10-17
 */
public class PhoneInfo {

    /**
     * 浏览器信息
     */
    private String browserInfo;
    /**
     * 手机型号
     */
    private String phoneModel;
    /**
     * 系统信息
     */
    private String systemInfo;

    /**
     * @return the browserInfo
     */
    public String getBrowserInfo() {
        return browserInfo;
    }

    /**
     * @param browserInfo the browserInfo to set
     */
    public void setBrowserInfo(String browserInfo) {
        this.browserInfo = browserInfo;
    }

    /**
     * @return the phoneModel
     */
    public String getPhoneModel() {
        return phoneModel;
    }

    /**
     * @param phoneModel the phoneModel to set
     */
    public void setPhoneModel(String phoneModel) {
        this.phoneModel = phoneModel;
    }

    /**
     * @return the systemInfo
     */
    public String getSystemInfo() {
        return systemInfo;
    }

    /**
     * @param systemInfo the systemInfo to set
     */
    public void setSystemInfo(String systemInfo) {
        this.systemInfo = systemInfo;
    }

    @Override
    public String toString() {
        return "PhoneInfo{" + "browserInfo=" + browserInfo + ", phoneModel=" + phoneModel + ", systemInfo=" + systemInfo + '}';
    }
}
