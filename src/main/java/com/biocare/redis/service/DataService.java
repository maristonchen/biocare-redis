package com.biocare.redis.service;

import java.util.List;

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
     */
    void save(String waveMetaData);

    /**
     * query the value by the key
     *
     * @param caseId case id
     * @param minTime             the min time
     * @param maxTime             the max time
     * @return the list of value
     */
    List<Object> queryByTimeRange(String caseId, String minTime, String maxTime);

    /**
     * query the first wave data
     *
     * @param caseIds case id
     * @return the data
     */
    List<Object> queryFirst(String[] caseIds);

    /**
     * query the last wave data
     *
     * @param caseIds  case id
     * @return the data
     */
    List<Object> queryLast(String[] caseIds);

    /**
     * query gps info
     * @param caseId case id
     * @return the  gps data
     */
    Object getPatientGPS(String caseId);
}
