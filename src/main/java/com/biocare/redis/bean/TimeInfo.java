package com.biocare.redis.bean;

/**
 * please descripe this java file
 *
 * @author mariston
 * @version 1.0
 * @since 2017/11/17 12:37
 */
public class TimeInfo {
    /**
     * "timeInfo": { // 时间信息
     * "monitoredTime": 1, // 监测时间，int，说明：心电宝协议中的字段，作用暂时不知，保留字段
     * "timestamp": 16441746455 // 时间戳, long，说明：数据上传时间
     * },
     */
    private Integer monitoredTime;

    private Long timestamp;

    public Integer getMonitoredTime() {
        return monitoredTime;
    }

    public void setMonitoredTime(Integer monitoredTime) {
        this.monitoredTime = monitoredTime;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
