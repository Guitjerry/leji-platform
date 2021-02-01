package com.macro.mall.common.util;

import java.security.SecureRandom;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author paida.lei
 * @date 2020/8/21
 */
public class CustomUUID {

    private static final Logger logger = LoggerFactory.getLogger(CustomUUID.class);

    /**
     * 区域标志位数
     */
    private final static long REGION_ID_BITS = 3L;

    /**
     * 机器标识位数
     */
    private final static long WORKER_ID_BITS = 10L;

    /**
     * 序列号识位数
     */
    private final static long SEQUENCE_BITS = 10L;

    /**
     * 区域标志ID最大值
     */
    private final static long MAX_REGION_ID = ~(-1L << REGION_ID_BITS);

    /**
     * 机器ID最大值
     */
    private final static long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);

    /**
     * 序列号ID最大值
     */
    private final static long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);

    /**
     * 机器ID偏左移10位
     */
    private final static long WORKER_ID_SHIFT = SEQUENCE_BITS;

    /**
     * 业务ID偏左移20位
     */
    private final static long REGION_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;

    /**
     * 时间毫秒左移23位
     */
    private final static long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + REGION_ID_BITS;

    private static long lastTimestamp = -1L;

    private static CustomUUID customUUID;

    private final long workerId;

    private final long regionId;

    /**
     * 基准时间 Thu, 04 Nov 2010 01:42:54 GMT
     */
    private long twepoch = 1288834974657L;

    private long sequence = 0L;

    private CustomUUID(long workerId, long regionId) {
        // 如果超出范围就抛出异常
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new IllegalArgumentException("worker Id can't be greater than %d or less than 0");
        }
        if (regionId > MAX_REGION_ID || regionId < 0) {
            throw new IllegalArgumentException("datacenter Id can't be greater than %d or less than 0");
        }
        this.workerId = workerId;
        this.regionId = regionId;
    }

    private CustomUUID(long workerId) {
        // 如果超出范围就抛出异常
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new IllegalArgumentException("worker Id can't be greater than %d or less than 0");
        }
        this.workerId = workerId;
        this.regionId = 0;
    }

    public static CustomUUID getInstance(long workerId, long regionId) {
        if (customUUID == null) {
            customUUID = new CustomUUID(workerId, regionId);
        }
        return customUUID;
    }

    public long generate() {
        return this.nextId(false, 0);
    }

    /**
     * 实际产生代码的
     */
    private synchronized long nextId(boolean isPadding, long busId) {
        long timestamp = timeGen();
        long paddingnum = regionId;
        if (isPadding) {
            paddingnum = busId;
        }
        if (timestamp < lastTimestamp) {
            try {
                throw new Exception("Clock moved backwards.  Refusing to generate id for " + (lastTimestamp - timestamp)
                    + " milliseconds");
            } catch (Exception e) {
                logger.error("时间戳异常", e);
            }
        }
        // 如果上次生成时间和当前时间相同,在同一毫秒内
        if (lastTimestamp == timestamp) {
            // sequence自增，因为sequence只有10bit，所以和SEQUENCE_MASK相与一下，去掉高位
            sequence = (sequence + 1) & SEQUENCE_MASK;
            // 判断是否溢出,也就是每毫秒内超过1024，当为1024时，与SEQUENCE_MASK相与，sequence就等于0
            if (sequence == 0) {
                // 自旋等待到下一毫秒
                timestamp = tailNextMillis(lastTimestamp);
            }
        } else {
            // 如果和上次生成时间不同,重置sequence，就是下一毫秒开始，sequence计数重新从0开始累加,
            // 为了保证尾数随机性更大一些,最后一位设置一个随机数
            sequence = new SecureRandom().nextInt(10);
        }
        lastTimestamp = timestamp;
        return ((timestamp - twepoch) << TIMESTAMP_LEFT_SHIFT) | (paddingnum << REGION_ID_SHIFT) | (workerId
            << WORKER_ID_SHIFT) | sequence;
    }

    /**
     * 防止产生的时间比之前的时间还要小（由于NTP回拨等问题）,保持增量的趋势.
     */
    private long tailNextMillis(final long lastTimestamp) {
        long timestamp = this.timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = this.timeGen();
        }
        return timestamp;
    }

    /**
     * 获取当前的时间戳
     */
    private long timeGen() {
        return Instant.now().toEpochMilli();
    }
}
