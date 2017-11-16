package com.biocare.redis.service.impl;

import com.biocare.redis.service.DataService;
import com.biocare.redis.util.RedisClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * service data handle
 *
 * @author mariston
 * @version V1.0
 * @since 2017/10/11
 */
@Service("dataService")
public class DataServiceImpl implements DataService {

    /**
     * redis client
     */
    @Resource
    private RedisClient redisClient;

    @Value("${redis.oper.index}")
    private int operIndex;

    @Value("${redis.backup.index}")
    private int backupIndex;

    /**
     * save key and value
     *
     * @param waveMetaData the wave meta data
     */
    @Override
    public void save(String waveMetaData) {
        redisClient.waveIncr(waveMetaData, operIndex);
    }

    /**
     * query the value by the key
     *
     * @param caseId  case id
     * @param minTime the min time
     * @param maxTime the max time
     * @return the list of value
     */
    @Override
    public List<Object> queryByTimeRange(String caseId, String minTime, String maxTime) {
        return redisClient.queryByRange(caseId, minTime, maxTime, operIndex);
    }

    /**
     * query the first wave data
     *
     * @param caseIds case id
     * @return the data
     */
    @Override
    public List<Object> queryFirst(String[] caseIds) {
        return redisClient.queryFirstOrLast(caseIds, true, operIndex);
    }

    /**
     * query the last wave data
     *
     * @param caseIds case id
     * @return the data
     */
    @Override
    public List<Object> queryLast(String[] caseIds) {
        return redisClient.queryFirstOrLast(caseIds, false, operIndex);
    }

    /**
     * query gps info
     *
     * @param caseId case id
     * @return the  gps data
     */
    @Override
    public Object getPatientGPS(String caseId) {
        return redisClient.getPatientGPS(caseId, operIndex);
    }
}
