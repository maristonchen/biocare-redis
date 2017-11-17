package com.biocare.redis.bean;

/**
 * please descripe this java file
 *
 * @author mariston
 * @version 1.0
 * @since 2017/11/17 12:35
 */
public class CaseInfo {
    /**
     * "caseInfo": { // 病历信息
     * "caseId": "szjc0001",  // 病历ID，String
     * "deviceId": "bj-ecare-082" // 设备ID，String
     * },
     */

    private String caseId;

    private String deviceId;

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
