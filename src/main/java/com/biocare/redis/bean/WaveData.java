package com.biocare.redis.bean;

/**
 * please descripe this java file
 *
 * @author mariston
 * @version 1.0
 * @since 2017/11/17 12:06
 */
public class WaveData {

    private String caseId;

    private WaveObj waveObj;

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public WaveObj getWaveObj() {
        return waveObj;
    }

    public void setWaveObj(WaveObj waveObj) {
        this.waveObj = waveObj;
    }
}
