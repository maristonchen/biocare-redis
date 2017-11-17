package com.biocare.redis.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
     * @param jsonData 每秒的波形数据 json格式的字符串
     * @return string "success" 表示成功 "fail"表示失败
     */
    @RequestMapping(value = "/saveWaveData", method = RequestMethod.POST, produces = {"text/html;charset=utf-8"})
    @ResponseBody
    public String save(String jsonData) {
        Map<String, Object> map = new HashMap<>();
        try {
            dataService.save(jsonData);
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
     * @param jsonData json
     * @return string json 格式的字符串
     */
    @RequestMapping(value = "/getRangeWaveData", method = RequestMethod.POST, produces = {"text/html;charset=utf-8"})
    @ResponseBody
    public String getRangeWaveData(String jsonData) {
        Map<String, Object> map = new HashMap<>();
        try {
            //{"caseId":"BJ654321","minTimestamp":1507770366560,"maxTimestamp":1507770379560}
            JSONObject jsonObject = JSON.parseObject(jsonData);
            List<Object> datas = dataService.queryByTimeRange(jsonObject.getString("caseId"), jsonObject.getString("minTimestamp"), jsonObject.getString("maxTimestamp"));
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
     * @param jsonData 病历号
     * @return string json 格式的字符串
     */
    @RequestMapping(value = "/getFirstWaveData", method = RequestMethod.POST, produces = {"text/html;charset=utf-8"})
    @ResponseBody
    public String getFirstWaveData(String jsonData) {
        Map<String, Object> map = new HashMap<>();
        //{"caseIds":["BJ654321","BJ654322"]}
        try {
            JSONArray array = JSON.parseObject(jsonData).getJSONArray("caseIds");
            List<Object> objects = dataService.queryFirst(array.toJavaList(String.class));
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
     * @param jsonData 病历号
     * @return string json 格式的字符串
     */
    @RequestMapping(value = "/getLatestWaveData", method = RequestMethod.POST, produces = {"text/html;charset=utf-8"})
    @ResponseBody
    public String getLatestWaveData(String jsonData) {
        Map<String, Object> map = new HashMap<>();
        try {
            JSONArray array = JSON.parseObject(jsonData).getJSONArray("caseIds");
            List<Object> objects = dataService.queryLast(array.toJavaList(String.class));
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
     * @param jsonData 病历号
     * @return string json 格式的字符串
     */
    @RequestMapping(value = "/getPatientGPS", method = RequestMethod.POST, produces = {"text/html;charset=utf-8"})
    @ResponseBody
    public String getPatientGPS(String jsonData) {
        Map<String, Object> map = new HashMap<>();
        try {
            //{"caseId":"BJ654321"}
            JSONObject jsonObject = JSON.parseObject(jsonData);
            Object gps = dataService.getPatientGPS(jsonObject.getString("caseId"));
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
