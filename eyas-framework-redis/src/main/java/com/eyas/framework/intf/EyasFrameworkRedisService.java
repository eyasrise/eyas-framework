package com.eyas.framework.intf;

import java.util.concurrent.TimeUnit;

public interface EyasFrameworkRedisService<Dto, D, Q> {

    Integer createRedisElement(Dto dto, String key, long time);

    Integer updateRedisElement(Dto dto, String key, long time, Long id);

    Object getRedisElement(String key, Long waitTime, String elementKeyId, TimeUnit timeUnit);

    Object getRedisElement(String element, Long waitTime, Long failureTime, String elementKeyId, TimeUnit timeUnit);

    Object getRedisElementLogs(String element, Long waitTime, Long failureTime, String elementKeyId, TimeUnit timeUnit);
}
