/*
 * Copyright (c) [2019] [潘志勇]
 *    [pzy-opensource] is licensed under the Mulan PSL v1.
 *    You can use this software according to the terms and conditions of the Mulan PSL v1.
 *    You may obtain a copy of Mulan PSL v1 at:
 *       http://license.coscl.org.cn/MulanPSL
 *    THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 *    IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 *    PURPOSE.
 *    See the Mulan PSL v1 for more details.
 */

package org.pzy.opensource.comm.util;

import cn.hutool.core.date.SystemClock;
import cn.hutool.core.util.StrUtil;

/**
 * Twitter的Snowflake 算法<br>
 * 分布式系统中，有一些需要使用全局唯一ID的场景，有些时候我们希望能使用一种简单一些的ID，并且希望ID能够按照时间有序生成。
 *
 * <p>
 * snowflake的结构如下(每部分用-分开):<br>
 *
 * <pre>
 * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000
 * </pre>
 * <p>
 * 第一位为未使用，接下来的41位为毫秒级时间(41位的长度可以使用69年)<br>
 * 然后是5位datacenterId和5位workerId(10位的长度最多支持部署1024个节点）<br>
 * 最后12位是毫秒内的计数（12位的计数顺序号支持每个节点每毫秒产生4096个ID序号）
 * <p>
 * 并且可以通过生成的id反推出生成时间,datacenterId和workerId
 *
 * <br>
 * 用法
 *
 * <pre>
 * // 实际的数据中心id和终端id应该通过配置文件读取. 另外当前这个帮助类的开始时间戳为: 2010-11-4 9:42:54
 * // 参数1为终端ID
 * // 参数2为数据中心ID
 * WinterSnowflake snowflake = new WinterSnowflake(1, 1);
 * long id = snowflake.nextId();
 * // 反推生成时间
 * idWorker.inferGenerateDateTime(nextId)
 * // 反推数据中心id
 * idWorker.inferDataCenterId(nextId)
 * // 反推终端id
 * idWorker.inferWorkerId(nextId)
 * </pre>
 *
 * @author pan
 * @date 2019-07-07
 */
public class WinterSnowflake {

    private static final long serialVersionUID = 1L;

    // 开始时间截: Thu, 04 Nov 2010 01:42:54 GMT
    private final long twepoch = 1288834974657L;
    private final long workerIdBits = 5L;
    private final long datacenterIdBits = 5L;
    //// 最大支持机器节点数0~31，一共32个
    private final long maxWorkerId = -1L ^ (-1L << workerIdBits);
    // 最大支持数据中心节点数0~31，一共32个
    private final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
    // 序列号12位
    private final long sequenceBits = 12L;
    // 机器节点左移12位
    private final long workerIdShift = sequenceBits;
    // 数据中心节点左移17位
    private final long datacenterIdShift = sequenceBits + workerIdBits;
    // 时间毫秒数左移22位
    private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);// 4095

    private long workerId;
    private long datacenterId;
    private long sequence = 0L;
    private long lastTimestamp = -1L;
    private boolean useSystemClock;

    /**
     * 构造
     *
     * @param workerId     终端ID
     * @param dataCenterId 数据中心ID
     */
    public WinterSnowflake(long workerId, long dataCenterId) {
        this(workerId, dataCenterId, false);
    }

    /**
     * 构造
     *
     * @param workerId         终端ID
     * @param dataCenterId     数据中心ID
     * @param isUseSystemClock 是否使用{@link SystemClock} 获取当前时间戳
     */
    public WinterSnowflake(long workerId, long dataCenterId, boolean isUseSystemClock) {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(StrUtil.format("worker Id can't be greater than {} or less than 0", maxWorkerId));
        }
        if (dataCenterId > maxDatacenterId || dataCenterId < 0) {
            throw new IllegalArgumentException(StrUtil.format("datacenter Id can't be greater than {} or less than 0", maxDatacenterId));
        }
        this.workerId = workerId;
        this.datacenterId = dataCenterId;
        this.useSystemClock = isUseSystemClock;
    }

    /**
     * 根据Snowflake的ID，获取机器id
     *
     * @param id snowflake算法生成的id
     * @return 所属机器的id
     */
    public long getWorkerId(long id) {
        return id >> workerIdShift & ~(-1L << workerIdBits);
    }

    /**
     * 根据Snowflake的ID，获取数据中心id
     *
     * @param id snowflake算法生成的id
     * @return 所属数据中心
     */
    public long getDataCenterId(long id) {
        return id >> datacenterIdShift & ~(-1L << datacenterIdBits);
    }

    /**
     * 根据Snowflake的ID，获取生成时间
     *
     * @param id snowflake算法生成的id
     * @return 生成的时间
     */
    public long getGenerateDateTime(long id) {
        return (id >> timestampLeftShift & ~(-1L << 41L)) + twepoch;
    }

    /**
     * 下一个ID
     *
     * @return ID
     */
    public synchronized long nextId() {
        long timestamp = genTime();
        if (timestamp < lastTimestamp) {
            // 如果服务器时间有问题(时钟后退) 报错。
            throw new IllegalStateException(StrUtil.format("Clock moved backwards. Refusing to generate id for {}ms", lastTimestamp - timestamp));
        }
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        return ((timestamp - twepoch) << timestampLeftShift) | (datacenterId << datacenterIdShift) | (workerId << workerIdShift) | sequence;
    }

    /**
     * 反推出机器id
     *
     * @param id snowflake算法生成的id
     * @return 所属机器的id
     */
    public long inferWorkerId(long id) {
        return id >> workerIdShift & ~(-1L << workerIdBits);
    }

    /**
     * 反推出数据中心的id
     *
     * @param id snowflake算法生成的id
     * @return 所属数据中心
     */
    public long inferDataCenterId(long id) {
        return id >> datacenterIdShift & ~(-1L << datacenterIdBits);
    }

    /**
     * 反推生成时间
     *
     * @param id snowflake算法生成的id
     * @return 生成时间的时间戳
     */
    public long inferGenerateDateTime(long id) {
        return (id >> timestampLeftShift & ~(-1L << 41L)) + twepoch;
    }

    /**
     * 下一个ID（字符串形式）
     *
     * @return ID 字符串形式
     */
    public String nextIdStr() {
        return Long.toString(nextId());
    }

    // ------------------------------------------------------------------------------------------------------------------------------------ Private method start

    /**
     * 循环等待下一个时间
     *
     * @param lastTimestamp 上次记录的时间
     * @return 下一个时间
     */
    private long tilNextMillis(long lastTimestamp) {
        long timestamp = genTime();
        while (timestamp <= lastTimestamp) {
            timestamp = genTime();
        }
        return timestamp;
    }

    /**
     * 生成时间戳
     *
     * @return 时间戳
     */
    private long genTime() {
        return this.useSystemClock ? SystemClock.now() : System.currentTimeMillis();
    }
    // ------------------------------------------------------------------------------------------------------------------------------------ Private method end
}
