package com.biocare.redis.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lambdaworks.redis.RedisAsyncConnection;
import com.lambdaworks.redis.RedisFuture;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.connection.lettuce.DefaultLettucePool;
import org.springframework.util.Assert;
import org.xerial.snappy.Snappy;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * redis client
 *
 * @author mariston
 * @version V1.0
 * @since 2017/09/21
 */
public final class RedisClient implements InitializingBean, DisposableBean {

    /**
     * 日志
     */
    private Logger logger = LoggerFactory.getLogger(RedisClient.class);

    /**
     * 连接池
     */
    private DefaultLettucePool defaultLettucePool;

    /**
     * 默认数据库索引 为 15
     */
    private final int DEFAULT_DB_INDEX = 15;

    /**
     * default the sum of databases is 16
     */
    private int databases = 16;

    /**
     * 默认字符集
     */
    private final String DEFAULT_CHARSET = "UTF-8";

    /**
     * start time key tail
     */
    private static String START_TIME_KEY_TAIL = "_start_time_key";

    /**
     * end time key tail
     */
    private static String END_TIME_KEY_TAIL = "_end_time_key";

    /**
     * GPS key tail
     */
    private static String GPS_KEY_TAIL = "_gps_key";

    /**
     * save wave info by second
     *
     * @param waveMetaData wave meta data
     * @param index        the index of redis databases
     */
    public void waveIncr(String waveMetaData, int index) {
        Assert.isTrue(index >= 0 && index < databases, "the index of database range must be between 0 and " + databases);
        RedisAsyncConnection<byte[], byte[]> connection = null;
        try {
            //每一秒的数据
            JSONObject json = JSONObject.parseObject(waveMetaData);

            JSONObject waveObj = json.getJSONObject("waveObj");

            //时间信息
            JSONObject timeInfo = waveObj.getJSONObject("timeInfo");

            String timestamp = timeInfo.getString("timestamp");

            //波形采集的标志位
            int waveFlag = waveObj.getIntValue("waveFlag");

            //病历号
            String caseId = json.getString("caseId");

            //GPS data
            JSONObject gps = waveObj.getJSONObject("gps");

            //get redis connection
            connection = getConn(index);

            //score
            double score = 0;

            //开始波形时间key
            String startTimeKey = caseId + START_TIME_KEY_TAIL;
            //最后波形时间key
            String endTimeKey = caseId + END_TIME_KEY_TAIL;
            //gps key
            String gpsKey = caseId + GPS_KEY_TAIL;

            boolean changeEndTime = true;

            if (waveFlag == 0) {
                connection.set(startTimeKey.getBytes(Charset.forName(DEFAULT_CHARSET)), timestamp.getBytes(Charset.forName(DEFAULT_CHARSET)));
            } else {
                RedisFuture<byte[]> startTimeBytes = connection.get(startTimeKey.getBytes(Charset.forName(DEFAULT_CHARSET)));
                String startTime = new String(startTimeBytes.get(), Charset.forName(DEFAULT_CHARSET));
                score = BigDecimalUtil.sub(timestamp, startTime);

                RedisFuture<byte[]> endTimeBytes = connection.get(endTimeKey.getBytes(Charset.forName(DEFAULT_CHARSET)));
                String endTime = new String(endTimeBytes.get(), Charset.forName(DEFAULT_CHARSET));
                // 如果存储顺序有问题，不更新最后时间
                if (BigDecimalUtil.compareTo(timestamp, endTime) < 0) {
                    changeEndTime = false;
                }
            }

            //压缩数据
            byte[] metaData = Snappy.compress(waveMetaData.getBytes(Charset.forName(DEFAULT_CHARSET)));

            connection.zadd(caseId.getBytes(Charset.forName(DEFAULT_CHARSET)), score, metaData);

            if (changeEndTime) {
                connection.set(endTimeKey.getBytes(Charset.forName(DEFAULT_CHARSET)), timestamp.getBytes(Charset.forName(DEFAULT_CHARSET)));
            }

            //更新GPS数据
            connection.set(gpsKey.getBytes(Charset.forName(DEFAULT_CHARSET)), gps.toJSONString().getBytes(Charset.forName(DEFAULT_CHARSET)));

        } catch (Exception e) {
            logger.error("====save wave info by second, occur an error that is [{}]:{}", e.getStackTrace()[0], e.getMessage());
            throw new IllegalArgumentException(e.getMessage());
        } finally {
            if (connection != null) {
                defaultLettucePool.returnResource(connection);
            }
        }
    }

    /**
     * Return a range of members in a sorted set, by score.
     *
     * @param caseId the medical record no
     * @param minTime             min time
     * @param maxTime             max time
     * @return list
     */
    public List<Object> queryByRange(String caseId, String minTime, String maxTime, int index) {
        Assert.hasText(caseId, "caseId is empty");
        Assert.hasText(minTime, "minTime is empty");
        Assert.hasText(maxTime, "maxTime is empty");
        Assert.isTrue(index >= 0 && index < databases, "the index of database range must be between 0 and " + databases);
        RedisAsyncConnection<byte[], byte[]> connection = null;
        try {
            connection = getConn(index);
            //开始波形时间key
            String startTimeKey = caseId + START_TIME_KEY_TAIL;
            RedisFuture<byte[]> startTimeBytes = connection.get(startTimeKey.getBytes(Charset.forName(DEFAULT_CHARSET)));
            String startTime = new String(startTimeBytes.get(), Charset.forName(DEFAULT_CHARSET));
            //min score
            double min = BigDecimalUtil.sub(minTime, startTime);

            //max score
            double max = BigDecimalUtil.sub(maxTime, startTime);

            //operation
            RedisFuture<List<byte[]>> bytes = connection.zrangebyscore(caseId.getBytes(Charset.forName(DEFAULT_CHARSET)), min, max);
            List<Object> values = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(bytes.get())) {
                for (byte[] data : bytes.get()) {
                    //解压
                    byte[] unzipData = Snappy.uncompress(data);
                    String value = new String(unzipData, Charset.forName(DEFAULT_CHARSET));
                    values.add(JSON.parseObject(value).getJSONObject("waveObj"));
                }
            }
            return values;
        } catch (Exception e) {
            logger.error("===query a range of members  by score,occur an error that is [{}]:{}", e.getStackTrace()[0], e.getMessage());
        } finally {
            if (connection != null) {
                defaultLettucePool.returnResource(connection);
            }
        }
        return null;
    }

    /**
     * Return a range of members in a sorted set, by score.
     *
     * @param caseIds the medical record no
     * @param firstOrLast         first->true  last ->false
     * @return list
     */
    public List<Object> queryFirstOrLast(String[] caseIds, boolean firstOrLast, int index) {
        Assert.isTrue(ArrayUtils.isNotEmpty(caseIds), "caseIds is empty");
        Assert.isTrue(index >= 0 && index < databases, "the index of database range must be between 0 and " + databases);
        RedisAsyncConnection<byte[], byte[]> connection = null;
        List<Object> list = new ArrayList<>();
        try {
            connection = getConn(index);

            for (String caseId : caseIds) {
                //score
                double score = 0;

                //first or last
                if (!firstOrLast) {
                    //开始波形时间key
                    String startTimeKey = caseId + START_TIME_KEY_TAIL;
                    RedisFuture<byte[]> startTimeBytes = connection.get(startTimeKey.getBytes(Charset.forName(DEFAULT_CHARSET)));
                    String startTime = new String(startTimeBytes.get(), Charset.forName(DEFAULT_CHARSET));

                    //最后波形时间key
                    String endTimeKey = caseId + END_TIME_KEY_TAIL;
                    RedisFuture<byte[]> endTimeBytes = connection.get(endTimeKey.getBytes(Charset.forName(DEFAULT_CHARSET)));
                    String endTime = new String(endTimeBytes.get(), Charset.forName(DEFAULT_CHARSET));

                    score = BigDecimalUtil.sub(endTime, startTime);
                }

                //operation
                RedisFuture<List<byte[]>> bytes = connection.zrangebyscore(caseId.getBytes(Charset.forName(DEFAULT_CHARSET)), score, score);
                if (CollectionUtils.isNotEmpty(bytes.get())) {
                    //解压
                    byte[] unzipData = Snappy.uncompress(bytes.get().get(0));
                    String value = new String(unzipData, Charset.forName(DEFAULT_CHARSET));
                    list.add(JSON.parseObject(value));
                }
            }

        } catch (Exception e) {
            logger.error("===query a range of members  by score,occur an error that is [{}]:{}", e.getStackTrace()[0], e.getMessage());
        } finally {
            if (connection != null) {
                defaultLettucePool.returnResource(connection);
            }
        }
        return list;
    }

    /**
     * get patient gps info
     *
     * @param caseId case id
     * @param index  database index
     * @return info
     */
    public Object getPatientGPS(String caseId, int index) {
        Assert.hasText(caseId, "caseId is empty");
        Assert.isTrue(index >= 0 && index < databases, "the index of database range must be between 0 and " + databases);
        RedisAsyncConnection<byte[], byte[]> connection = null;
        try {
            connection = getConn(index);

            //gps key
            String gpsKey = caseId + GPS_KEY_TAIL;

            RedisFuture<byte[]> gpsBytes = connection.get(gpsKey.getBytes(Charset.forName(DEFAULT_CHARSET)));
            String value =  new String(gpsBytes.get(), Charset.forName(DEFAULT_CHARSET));
            return JSON.parseObject(value);
        } catch (Exception e) {
            logger.error("===get patient gps info,occur an error that is [{}]:{}", e.getStackTrace()[0], e.getMessage());
        } finally {
            if (connection != null) {
                defaultLettucePool.returnResource(connection);
            }
        }
        return null;
    }

    /**
     * 删除键值对
     *
     * @param key 键
     */
    public void delete(String key) {
        delete(key, DEFAULT_DB_INDEX);
    }

    /**
     * 删除键值对
     *
     * @param key   键
     * @param index 数据库
     */
    public void delete(String key, int index) {
        Assert.hasText(key, "key is empty");
        Assert.isTrue(index >= 0 && index < databases, "the index of database range must be between 0 and " + databases);
        RedisAsyncConnection<byte[], byte[]> connection = null;
        try {
            connection = getConn(index);
            connection.del(key.getBytes(Charset.forName(DEFAULT_CHARSET)));
        } catch (Exception e) {
            logger.error("===删除键值对异常[{}]:{}", e.getStackTrace()[0], e.getMessage());
        } finally {
            if (connection != null) {
                defaultLettucePool.returnResource(connection);
            }
        }
    }


    /**
     * Remove all members in a sorted set within the given scores.
     *
     * @param key   the key
     * @param min   min score
     * @param max   max score
     * @param index the index of databases
     */
    public void zrem(String key, String min, String max, int index) {
        Assert.hasText(key, "key is empty");
        Assert.hasText(min, "min is empty");
        Assert.hasText(max, "max is empty");
        Assert.isTrue(index >= 0 && index < databases, "the index of database range must be between 0 and " + databases);
        RedisAsyncConnection<byte[], byte[]> connection = null;
        try {
            connection = getConn(index);
            connection.zremrangebyscore(key.getBytes(Charset.forName(DEFAULT_CHARSET)), min, max);
        } catch (Exception e) {
            logger.error("===删除集合数据异常[{}]:{}", e.getStackTrace()[0], e.getMessage());
        } finally {
            if (connection != null) {
                defaultLettucePool.returnResource(connection);
            }
        }
    }

    /**
     * 清空数据库
     *
     * @param index 数据库
     */
    public void flushdb(int index) {
        Assert.isTrue(index >= 0 && index < databases, "the index of database range must be between 0 and " + databases);
        RedisAsyncConnection<byte[], byte[]> connection = null;
        try {
            connection = getConn(index);
            connection.flushdb();
        } catch (Exception e) {
            logger.error("===清空数据库异常[{}]:{}", e.getStackTrace()[0], e.getMessage());
        } finally {
            if (connection != null) {
                defaultLettucePool.returnResource(connection);
            }
        }
    }

    /**
     * 设置超时时间
     *
     * @param key     键
     * @param seconds 时长
     * @return boolean
     */
    public boolean expire(String key, long seconds) {
        return expire(key, seconds, DEFAULT_DB_INDEX);
    }

    /**
     * 设置超时时间
     *
     * @param key     键
     * @param seconds 时长
     * @param index   索引
     * @return boolean
     */
    public boolean expire(String key, long seconds, int index) {
        Assert.hasText(key, "key is empty");
        Assert.isTrue(index >= 0 && index < databases, "the index of database range must be between 0 and " + databases);
        RedisAsyncConnection<byte[], byte[]> connection = null;
        try {
            connection = getConn(index);
            RedisFuture<Boolean> bool = connection.expire(key.getBytes(Charset.forName(DEFAULT_CHARSET)), seconds);
            return bool.get();
        } catch (Exception e) {
            logger.error("===设置超时时间异常[{}]:{}", e.getStackTrace()[0], e.getMessage());
        } finally {
            if (connection != null) {
                defaultLettucePool.returnResource(connection);
            }
        }
        return false;
    }

    /**
     * Invoked by a BeanFactory on destruction of a singleton.
     *
     * @throws Exception in case of shutdown errors.
     *                   Exceptions will get logged but not rethrown to allow
     *                   other beans to release their resources too.
     */
    @Override
    public void destroy() throws Exception {
        if (defaultLettucePool != null) {
            defaultLettucePool.destroy();
        }
    }

    /**
     * Invoked by a BeanFactory after it has set all bean properties supplied
     * (and satisfied BeanFactoryAware and ApplicationContextAware).
     * <p>This method allows the bean instance to perform initialization only
     * possible when all bean properties have been set and to throw an
     * exception in the event of misconfiguration.
     *
     * @throws Exception in the event of misconfiguration (such
     *                   as failure to set an essential property) or if initialization fails.
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(defaultLettucePool, "the default lettuce pool is null ");
    }

    /**
     * 获取连接
     *
     * @param index 数据库索引
     * @return {@link RedisAsyncConnection}
     */
    private RedisAsyncConnection<byte[], byte[]> getConn(int index) {
        RedisAsyncConnection<byte[], byte[]> connection = defaultLettucePool.getResource();
        connection.select(index);
        return connection;
    }

    /**
     * 设置连接池
     *
     * @param defaultLettucePool 连接池
     */
    public void setDefaultLettucePool(DefaultLettucePool defaultLettucePool) {
        this.defaultLettucePool = defaultLettucePool;
    }

    /**
     * set the sum of databases
     *
     * @param databases int
     */
    public void setDatabases(int databases) {
        this.databases = databases;
    }
}
