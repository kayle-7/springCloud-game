package com.springboot.workflow.util.guid;

import com.springboot.workflow.exception.WorkflowException;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class SnowflakeIdGenerator implements GUIDGenerator {
    private final static long startTimestamp = 1546272000000L; //起始时间戳，2019/1/1 00:00:00
    private final static long workerIdBits = 16;//16(0~65535)
    private final static long sequenceBits = 5;//5(0~31)
    private final static long randomBits = 2;//2(0~4)
    private final static long workerIdLeftShift = sequenceBits;
    private final static long randomLeftShift = sequenceBits + workerIdBits;
    private final static long timestampLeftShift = randomBits + sequenceBits + workerIdBits;//39(17年)
    private final static long maxWorkerId = ~(-1L << workerIdBits);
    private final static int maxRandomBit = (int)~(-1L << randomBits);
    private final static long sequenceMask = ~(-1L << sequenceBits);

    private final long workerId;
    private final long frequencyMillis; //更新一次随机数，默认1分钟
    private volatile int random;
    private long sequence = 0L;
    private long lastTimestamp = -1L;
    private final static ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

    SnowflakeIdGenerator(final Map<String, Object> args) {
        this.workerId = (Long) args.get("workerId");
        if (this.workerId > maxWorkerId || this.workerId < 0) {
            throw new IllegalArgumentException(String.format(
                    "worker Id can't be greater than %d or less than 0",
                    maxWorkerId));
        }
        this.random = 0;
        this.frequencyMillis = (Long)args.getOrDefault("frequencyMillis", 60 * 1000L);
        singleThreadExecutor.execute(() -> {
            while (true) {
                try {
                    Thread.sleep(this.frequencyMillis);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                this.random = new Random().nextInt(maxRandomBit);
                LoggerFactory.getLogger(SnowflakeIdGenerator.class).warn("singleThreadExecutor : SnowflakeIdGenerator change random " + this.random);
            }
        });
    }

    @Override
    public synchronized long nextId() {
        long timestamp = System.currentTimeMillis();
        if (timestamp < this.lastTimestamp) {
            throw new WorkflowException(
                    String.format("InvalidSystemClockException. clock moved backwards. refusing to generate transitionId for %d milliseconds",
                            this.lastTimestamp - timestamp));
        }
        if (this.lastTimestamp == timestamp) {
            this.sequence = (this.sequence + 1) & sequenceMask;
            if (this.sequence == 0) {
                timestamp = nextTimeMillis(this.lastTimestamp);
            }
        } else {
            this.sequence = 0;
        }
        this.lastTimestamp = timestamp;
        return ((timestamp - startTimestamp) << timestampLeftShift)
                | (random << randomLeftShift)
                | (workerId << workerIdLeftShift)
                | (this.sequence);
    }

    private static long nextTimeMillis(final long lastTimestamp) {
        long timestamp = System.currentTimeMillis();
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis();
        }
        return timestamp;
    }
}