package com.biocare.redis.service.impl;

import com.alibaba.fastjson.JSON;
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
     * @return the result ,success or fail
     */
    @Override
    public String save(String waveMetaData) {
        redisClient.waveIncr(waveMetaData,operIndex);
        return "success";
    }

    /**
     * query the value by the key
     *
     * @param medicalRecordNumber the medical record no
     * @param minTime             the min time
     * @param maxTime             the max time
     * @return the list of value
     */
    @Override
    public String queryByTimeRange(String medicalRecordNumber, String minTime, String maxTime) {
        List<String> datas = redisClient.queryByRange(medicalRecordNumber, minTime, maxTime, operIndex);
        return JSON.toJSONString(datas);
    }

    /**
     * query the first wave data
     *
     * @param medicalRecordNumber the medical record no
     * @return the data
     */
    @Override
    public String queryFirst(String medicalRecordNumber) {
        return redisClient.queryFirstOrLast(medicalRecordNumber,true,operIndex);
    }

    /**
     * query the last wave data
     *
     * @param medicalRecordNumber the medical record no
     * @return the data
     */
    @Override
    public String queryLast(String medicalRecordNumber) {
        return redisClient.queryFirstOrLast(medicalRecordNumber,false,operIndex);
    }
}
