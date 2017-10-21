package com.biocare.redis.util;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * data handle tool
 *
 * @author mariston
 * @version V1.0
 * @since 2017/10/11
 */
public abstract class DataUtils {



    /**
     * obtain timestamp
     * @param value the value
     * @return
     */
    public static final String timestamp(String value) {

        Map<String, String> map = new HashMap<>();

        //每一秒的数据
        JSONObject json = JSONObject.parseObject(value);

        //时间信息
        JSONObject timeInfo = json.getJSONObject("TimeInfo");

        long timestamp = timeInfo.getLongValue("TimeStamp");

        //波形采集的标志位
        int waveFlag = json.getIntValue("WaveFlag");

        //病历信息
        JSONObject medicalRecordInfo = json.getJSONObject("MedicalRecordInfo");

        //病历号
        String medicalRecordNumber = medicalRecordInfo.getString("MedicalRecordNumber");






        return "";
    }
}
