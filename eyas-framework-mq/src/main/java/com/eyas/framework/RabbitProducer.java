package com.eyas.framework;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author Created by yixuan on 2019/6/22.
 */
@Component
public class RabbitProducer {

    @Autowired
    private AmqpTemplate amqpTemplate;

    public void stringSend(String i){
        Date date = new Date();
        String dateString = "i=" + i + "" + DateUtil.getCurrentDateTime();
        System.out.println("[string] send msg: " + dateString);
        // 第一个参数为刚刚定义的队列名称
        this.amqpTemplate.convertAndSend("string", dateString);
    }
}
