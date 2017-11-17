package com.biocare.redis.bean;

/**
 * please descripe this java file
 *
 * @author mariston
 * @version 1.0
 * @since 2017/11/17 12:33
 */
public class Protocol {
    /**
     *      "protocol": { // 协议
     "version": "1.0",  // 版本，String
     "maufactor": "biocare"  // 厂家，String
     },
     */

    private String version;

    private String maufactor;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMaufactor() {
        return maufactor;
    }

    public void setMaufactor(String maufactor) {
        this.maufactor = maufactor;
    }
}
