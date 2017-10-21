package com.biocare.redis.controller;

import com.biocare.redis.service.DataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 示例控制中心
 *
 * @author mariston
 * @version V1.1.0
 * @since 2017/07/27
 */
@Controller
@RequestMapping("/redis")
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
    @RequestMapping(value = "/save", method = RequestMethod.POST, produces = {"text/html;charset=utf-8"})
    @ResponseBody
    public String save(String waveMetaData) {
        try {
            return dataService.save(waveMetaData);
        } catch (Exception e) {
            logger.error("保存波形异常[{}]:[{}]", e.getStackTrace()[0], e.getMessage());
            return "fail";
        }
    }

    /**
     * 波形范围查询
     *
     * @param medicalRecordNumber 病历号
     * @param minTime             最小时间戳
     * @param maxTime             最大时间戳
     * @return string json 格式的字符串
     */
    @RequestMapping(value = "/queryByTimeRange", method = RequestMethod.POST, produces = {"text/html;charset=utf-8"})
    @ResponseBody
    public String queryByTimeRange(String medicalRecordNumber, String minTime, String maxTime) {
        return dataService.queryByTimeRange(medicalRecordNumber, minTime, maxTime);
    }

    /**
     * 查询第一秒波形
     *
     * @param medicalRecordNumber 病历号
     * @return string json 格式的字符串
     */
    @RequestMapping(value = "/queryFirst", method = RequestMethod.POST, produces = {"text/html;charset=utf-8"})
    @ResponseBody
    public String queryFirst(String medicalRecordNumber) {
        return dataService.queryFirst(medicalRecordNumber);
    }

    /**
     * 查询最后一秒的波形
     *
     * @param medicalRecordNumber 病历号
     * @return string json 格式的字符串
     */
    @RequestMapping(value = "/queryLast", method = RequestMethod.POST, produces = {"text/html;charset=utf-8"})
    @ResponseBody
    public String queryLast(String medicalRecordNumber) {
        return dataService.queryLast(medicalRecordNumber);
    }

}
