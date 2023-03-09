package com.eyas.framework;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Created by yixuan on 2019/6/22.
 */
@Configuration
public class RabbitConfig {

    /**
     * 定义队列名
     */
    private final static String STRING = "string";

    /**
     * 定义string队列
     * @return
     */
    @Bean
    public Queue string() {
        return new Queue(STRING);
    }
}
