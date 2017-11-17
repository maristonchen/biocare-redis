package com.biocare.redis.controller;

import com.alibaba.fastjson.JSON;
import com.biocare.redis.service.DataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 示例控制中心
 *
 * @author mariston
 * @version V1.1.0
 * @since 2017/07/27
 */
@Controller
@RequestMapping("/")
public class RedisController {

    /**
     * 日志
     */
    private Logger logger = LoggerFactory.getLogger(RedisController.class);

    /**
     * data service
     */
    @Resource
    private DataService dataService;

    /**
     * 保存波形
     *
     * @param waveMetaData 每秒的波形数据 json格式的字符串
     * @return string "success" 表示成功 "fail"表示失败
     */
    @RequestMapping(value = "/saveWaveData", method = RequestMethod.POST, produces = {"text/html;charset=utf-8"})
    @ResponseBody
    public String save(String waveMetaData) {
        Map<String, Object> map = new HashMap<>();
        try {
            dataService.save(waveMetaData);
            map.put("code", 0);
            map.put("msg", "ok");
        } catch (Exception e) {
            logger.error("保存波形异常[{}]:[{}]", e.getStackTrace()[0], e.getMessage());
            map.put("code", 999);
            map.put("msg", e.getMessage());
        }
        return JSON.toJSONString(map);
    }

    /**
     * 波形范围查询
     *
     * @param caseId       病历号
     * @param minTimestamp 最小时间戳
     * @param maxTimestamp 最大时间戳
     * @return string json 格式的字符串
     */
    @RequestMapping(value = "/getRangeWaveData", method = RequestMethod.POST, produces = {"text/html;charset=utf-8"})
    @ResponseBody
    public String getRangeWaveData(String caseId, String minTimestamp, String maxTimestamp) {
        Map<String, Object> map = new HashMap<>();
        try {
            List<Object> datas = dataService.queryByTimeRange(caseId, minTimestamp, maxTimestamp);
            map.put("code", 0);
            map.put("msg", "ok");
            map.put("waveObjs", datas);
        } catch (Exception e) {
            logger.error("保存波形异常[{}]:[{}]", e.getStackTrace()[0], e.getMessage());
            map.put("code", 999);
            map.put("msg", e.getMessage());
        }
        return JSON.toJSONString(map);
    }

    /**
     * 查询第一秒波形
     *
     * @param caseIds 病历号
     * @return string json 格式的字符串
     */
    @RequestMapping(value = "/getFirstWaveData", method = RequestMethod.POST, produces = {"text/html;charset=utf-8"})
    @ResponseBody
    public String getFirstWaveData(String[] caseIds) {
        Map<String, Object> map = new HashMap<>();
        try {
            List<Object> objects = dataService.queryFirst(caseIds);
            map.put("code", 0);
            map.put("msg", "ok");
            map.put("waveObj", objects);
        } catch (Exception e) {
            logger.error("查询第一秒波形异常[{}]:[{}]", e.getStackTrace()[0], e.getMessage());
            map.put("code", 999);
            map.put("msg", e.getMessage());
        }
        return JSON.toJSONString(map);
    }

    /**
     * 查询最后一秒的波形
     *
     * @param caseIds 病历号
     * @return string json 格式的字符串
     */
    @RequestMapping(value = "/getLatestWaveData", method = RequestMethod.POST, produces = {"text/html;charset=utf-8"})
    @ResponseBody
    public String getLatestWaveData(String[] caseIds) {

        Map<String, Object> map = new HashMap<>();
        try {
            List<Object> objects = dataService.queryLast(caseIds);
            map.put("code", 0);
            map.put("msg", "ok");
            map.put("waveObj", objects);
        } catch (Exception e) {
            logger.error("查询最后一秒的波形异常[{}]:[{}]", e.getStackTrace()[0], e.getMessage());
            map.put("code", 999);
            map.put("msg", e.getMessage());
        }
        return JSON.toJSONString(map);
    }


    /**
     * 查询GPS信息
     *
     * @param caseId 病历号
     * @return string json 格式的字符串
     */
    @RequestMapping(value = "/getPatientGPS", method = RequestMethod.POST, produces = {"text/html;charset=utf-8"})
    @ResponseBody
    public String getPatientGPS(String caseId) {
        Map<String, Object> map = new HashMap<>();
        try {
            Object gps = dataService.getPatientGPS(caseId);
            map.put("code", 0);
            map.put("msg", "ok");
            map.put("gps", gps);
        } catch (Exception e) {
            logger.error("查询GPS信息[{}]:[{}]", e.getStackTrace()[0], e.getMessage());
            map.put("code", 999);
            map.put("msg", e.getMessage());
        }
        return JSON.toJSONString(map);
    }

}
