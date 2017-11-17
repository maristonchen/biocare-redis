package com.biocare.redis.bean;

/**
 * please descripe this java file
 *
 * @author mariston
 * @version 1.0
 * @since 2017/11/17 12:07
 */
public class WaveObj {

    /**
     * {
     * "protocol": { // 协议
     * "version": "1.0",  // 版本，String
     * "maufactor": "biocare"  // 厂家，String
     * },
     * "caseInfo": { // 病历信息
     * "caseId": "szjc0001",  // 病历ID，String
     * "deviceId": "bj-ecare-082" // 设备ID，String
     * },
     * "waveInfo": { // 波形信息
     * "sampleRate": 256,  // 采样率，int
     * "leadEvent": 1,  // 导联事件，int，说明：心电宝协议中的字段，作用暂时不知，保留字段
     * "channel": 3 // 通道数（导联数目）, int
     * },
     * "waveData": "", // 波形数据，byte[]
     * "timeInfo": { // 时间信息
     * "monitoredTime": 1, // 监测时间，int，说明：心电宝协议中的字段，作用暂时不知，保留字段
     * "timestamp": 16441746455 // 时间戳, long，说明：数据上传时间
     * },
     * "waveFlag": 1, // 波形数据标识，enum，0 – 开始传输；1 – 传输中；2 – 中断传输 ,3 - 结束传输
     * "gps": {
     * "longitude": 114.21892734521, // 经度，long
     * "latitude": 29.575429778924 // 纬度，long
     * }
     * }
     */


    private Protocol protocol;

    private CaseInfo caseInfo;

    private WaveInfo waveInfo;

    private byte[] waveData;

    private TimeInfo timeInfo;

    private Integer waveFlag;

    private GPS gps;

    public Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public CaseInfo getCaseInfo() {
        return caseInfo;
    }

    public void setCaseInfo(CaseInfo caseInfo) {
        this.caseInfo = caseInfo;
    }

    public WaveInfo getWaveInfo() {
        return waveInfo;
    }

    public void setWaveInfo(WaveInfo waveInfo) {
        this.waveInfo = waveInfo;
    }

    public byte[] getWaveData() {
        return waveData;
    }

    public void setWaveData(byte[] waveData) {
        this.waveData = waveData;
    }

    public TimeInfo getTimeInfo() {
        return timeInfo;
    }

    public void setTimeInfo(TimeInfo timeInfo) {
        this.timeInfo = timeInfo;
    }

    public Integer getWaveFlag() {
        return waveFlag;
    }

    public void setWaveFlag(Integer waveFlag) {
        this.waveFlag = waveFlag;
    }

    public GPS getGps() {
        return gps;
    }

    public void setGps(GPS gps) {
        this.gps = gps;
    }
}
