package com.biocare.redis.service;

/**
 * service data handle
 *
 * @author mariston
 * @version V1.0
 * @since 2017/10/11
 */
public interface DataService {

    /**
     * save key and value
     *
     * @param waveMetaData the wave meta data
     * @return the result ,success or fail
     */
    String save(String waveMetaData);

    /**
     * query the value by the key
     *
     * @param medicalRecordNumber the medical record no
     * @param minTime             the min time
     * @param maxTime             the max time
     * @return the list of value
     */
    String queryByTimeRange(String medicalRecordNumber, String minTime, String maxTime);

    /**
     * query the first wave data
     *
     * @param medicalRecordNumber the medical record no
     * @return the data
     */
    String queryFirst(String medicalRecordNumber);

    /**
     * query the last wave data
     *
     * @param medicalRecordNumber the medical record no
     * @return the data
     */
    String queryLast(String medicalRecordNumber);
}
