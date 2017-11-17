package com.biocare.redis.bean;

/**
 * please descripe this java file
 *
 * @author mariston
 * @version 1.0
 * @since 2017/11/17 12:39
 */
public class GPS {

    /**
     * "gps": {
     * "longitude": 114.21892734521, // 经度，long
     * "latitude": 29.575429778924 // 纬度，long
     * }
     */

    private Long longitude;

    private Long latitude;

    public Long getLongitude() {
        return longitude;
    }

    public void setLongitude(Long longitude) {
        this.longitude = longitude;
    }

    public Long getLatitude() {
        return latitude;
    }

    public void setLatitude(Long latitude) {
        this.latitude = latitude;
    }
}
