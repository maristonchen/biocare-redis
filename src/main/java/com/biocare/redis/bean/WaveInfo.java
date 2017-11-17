package com.biocare.redis.bean;

/**
 * please descripe this java file
 *
 * @author mariston
 * @version 1.0
 * @since 2017/11/17 12:36
 */
public class WaveInfo {
    /**
     * "waveInfo": { // 波形信息
     * "sampleRate": 256,  // 采样率，int
     * "leadEvent": 1,  // 导联事件，int，说明：心电宝协议中的字段，作用暂时不知，保留字段
     * "channel": 3 // 通道数（导联数目）, int
     * },
     */

    private Integer sampleRate;

    private Integer leadEvent;

    private Integer channel;

    public Integer getSampleRate() {
        return sampleRate;
    }

    public void setSampleRate(Integer sampleRate) {
        this.sampleRate = sampleRate;
    }

    public Integer getLeadEvent() {
        return leadEvent;
    }

    public void setLeadEvent(Integer leadEvent) {
        this.leadEvent = leadEvent;
    }

    public Integer getChannel() {
        return channel;
    }

    public void setChannel(Integer channel) {
        this.channel = channel;
    }
}
